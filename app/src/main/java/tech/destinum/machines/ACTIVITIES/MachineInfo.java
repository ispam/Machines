package tech.destinum.machines.ACTIVITIES;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ADAPTERS.DateItem;
import tech.destinum.machines.ADAPTERS.IncomeItem;
import tech.destinum.machines.ADAPTERS.InfoItems;
import tech.destinum.machines.ADAPTERS.ListAdapter;
import tech.destinum.machines.R;
import tech.destinum.machines.UTILS.NumberTextWatcher;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModelFactory;
import tech.destinum.machines.data.local.POJO.Income;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;

public class MachineInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView mName, mMoney;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFAB;
    private ListAdapter mAdapter;
    private Calendar mCalendar;
    private TextView info_date;
    private int mDay, mMonth, mYear;
    private String name, total_amount2;
    private long id, date;
    private ImageView check;
    private Boolean showMenu = false;
    private List<InfoItems> mInfoItems = new ArrayList<>();
    private List<Income> mIncomeList = new ArrayList<>();
    private double value, totalMonth;
    private String notes;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    IncomeViewModel incomeViewModel;

    @Inject
    MachineViewModel machineViewModel;

    @Inject
    IncomeViewModelFactory mIncomeViewModelFactory;

    private static final String TAG = MachineInfo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        ((App) getApplication()).getComponent().inject(this);
        incomeViewModel = ViewModelProviders.of(this, mIncomeViewModelFactory).get(IncomeViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = findViewById(R.id.machine_info_name);
        mMoney = findViewById(R.id.machine_info_money);

        mRecyclerView = findViewById(R.id.rvNotes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFAB =  findViewById(R.id.fabAddIncome);

        getFABClick();

    }



    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String location = bundle.getString("name");
            final long id = bundle.getLong("id");
            this.id = id;
            name = location;
            mName.setText(location);
        }else{
            finish();
        }


        disposable.add(incomeViewModel.getIncomeOfMachine(id)
                .defaultIfEmpty(0.0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(total_amount -> {
                            if (total_amount != null) {

                                disposable.add(machineViewModel.updateByID(id, total_amount)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .subscribe(
                                                () -> Log.d(TAG, "MachineInfo: UPDATE COMPLETED"),
                                                throwable -> Log.e(TAG, "MachineInfo: ERROR", throwable )));

                                DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                                String formatted = formatter.format(total_amount);
                                mMoney.setText(formatted);

                                total_amount2 = formatted;
                                showMenu = true;
                            } else {
                                mMoney.setText("$0.0");
                            }
                        }, throwable -> Log.d(TAG, "MachineInfo 2: ERROR")));


        disposable.add(incomeViewModel.getAllIncomesOfMachine(id)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(incomes -> {

                    mInfoItems.clear();
                    mIncomeList.clear();
                    mIncomeList = incomes;

                    Map<String, List<Income>> hashMap = toMap(mIncomeList);

                    for (String header : hashMap.keySet()){
                        DateItem dateItem = new DateItem(header);
                        mInfoItems.add(dateItem);

                        for (Income income : hashMap.get(header)){
                            IncomeItem incomeItem = new IncomeItem(income);
                            mInfoItems.add(incomeItem);
                        }

                    }

                    mAdapter = new ListAdapter(MachineInfo.this, mInfoItems, incomeViewModel, name, id);
                    mRecyclerView.setAdapter(mAdapter);

                    disposable.add(mAdapter.clickEvent
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(income_id -> incomeViewModel.deleteIncomeByID(income_id)));

                }, throwable -> Log.e(TAG, "onCreate: Unable to get machines", throwable)));

    }

    private Map<String,List<Income>> toMap(List<Income> incomes) {
        Map<String, List<Income>> map = new LinkedHashMap<>();

        for (Income income : incomes){

            long dateLong = income.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
            String dateString = sdf.format(new Date(dateLong));

            List<Income> value = map.get(dateString);
            if (value == null){
                value = new ArrayList<>();
                map.put(dateString, value);
            }
            value.add(income);

        }

        return map;
    }

    private void getFABClick() {
        mFAB.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_add_income, null, true);

            EditText editText = view.findViewById(R.id.dialog_info_et);
            editText.addTextChangedListener(new NumberTextWatcher(editText));
            check = view.findViewById(R.id.check);
            EditText editText2 = view.findViewById(R.id.dialog_info_notes_et);
            info_date = view.findViewById(R.id.dialog_info_date_tv);
            Button button = view.findViewById(R.id.dialog_info_date_btn);
            button.setOnClickListener(button1 -> {

                mCalendar = Calendar.getInstance();
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                mMonth = mCalendar.get(Calendar.MONTH);
                mYear = mCalendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MachineInfo.this,  R.style.datepicker, MachineInfo.this,  mYear, mMonth, mDay);
                datePickerDialog.show();

                hideSoftKeyboard(v);

            });

            dialog.setNegativeButton("Cancelar", null)
                    .setPositiveButton("Agregar", (dialog1, which) -> {

                        notes = editText2.getText().toString();
                        String money = editText.getText().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = sdf.format(new Date(date));

                        if(money.isEmpty() || money.equals("") || dateString == null || dateString.equals("31/12/1969")) {
                            Toast.makeText(v.getContext(), "Fecha y Dinero SON OBLIGATORIOS", Toast.LENGTH_SHORT).show();
                            hideSoftKeyboard(v);
                        } else {

                            if (notes.isEmpty() || notes.equals("")){
                                notes = "Sin Observaciones";
                            }

                            value = Double.parseDouble(money);
                            disposable.add(incomeViewModel.addIncome(date, notes, value, id, mMonth)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> Log.d(TAG, "MachineInfo: INCOME ADDED"),
                                            throwable -> Log.e(TAG, "MachineInfo: ", throwable)));
                        }

                        hideSoftKeyboard(v);


                    }).setView(view).show();
        });
    }

    @NonNull
    public void hideSoftKeyboard(View v){
        invalidateOptionsMenu();
        //Hide Softkeyboard
        View view1 = v.getRootView();
        if (view1 != null) {
            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onStop() {
        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
        super.onStop();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        mMonth = month + 1;

        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        date = calendar.getTimeInMillis();
//        date = mDayFinal+"/"+mMonthFinal+"/"+mYearFinal;

        long dateLong = calendar.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(new Date(dateLong));

        info_date.setText(dateString);
        check.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (showMenu){
            getMenuInflater().inflate(R.menu.menu_line_chart, menu);
            for(int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); //fix the color to white
                item.setTitle(spanString);
            }
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_line_chart:

                Intent intent = new Intent(MachineInfo.this, LineChart.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);

                break;
            case R.id.share_info:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, name + " ha recaudado en total: "+total_amount2);
                startActivity(Intent.createChooser(sendIntent, "Compartir"));

                break;
            case R.id.export_csv_incomes:

                String[] headers = new String[]{"ID Ingreso", "Maquina", "Nota", "Fecha","Dinero Recoletado"};
                Iterator<Income> iterator2 = mIncomeList.iterator();

                File file = new File(Environment.getExternalStorageDirectory(), "/Maquinas");
                File exportFile = new File(file, name + " Ingresos.csv");

                if (!file.exists()) {
                    file.mkdir();
                }

                try {
                    if (exportFile.exists()){
                        exportFile.delete();
                    }
                    exportFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    CSVWriter writer = new CSVWriter(new FileWriter(exportFile, true));
                    writer.writeNext(headers);

                    while (iterator2.hasNext()){
                        Income income = iterator2.next();
                        long id = income.get_id();
                        String note = income.getNote();

                        long dateLong = income.getDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = sdf.format(new Date(dateLong));


                        double total_amount = income.getMoney();
                        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                        String formatted = formatter.format(total_amount);

                        writer.writeNext(new String[]{String.valueOf(id), name, note, dateString, formatted});
                    }

                    writer.close();

                    Intent export = new Intent();
                    export.setAction(Intent.ACTION_SEND);
                    export.setType("text/csv");
                    export.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));
                    export.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(Intent.createChooser(export, "Exportar CSV"), 512);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

