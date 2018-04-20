package tech.destinum.machines.ACTIVITIES;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import tech.destinum.machines.ADAPTERS.ListAdapter;
import tech.destinum.machines.R;
import tech.destinum.machines.UTILS.NumberTextWatcher;
import tech.destinum.machines.data.ExportCSV;
import tech.destinum.machines.data.local.POJO.Income;
import tech.destinum.machines.data.local.POJO.Machine;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;

public class MachineInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView mName, mMoney;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFAB;
    private ListAdapter mAdapter;
    private Calendar mCalendar;
    private TextView info_date;
    private int mDay, mMonth, mYear, mDayFinal, mMonthFinal, mYearFinal;
    private String date, name, total_amount2;
    private long id;
    private ImageView check;
    private Boolean showMenu = false;
    private List<Income> incomeList = new ArrayList<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    IncomeViewModel incomeViewModel;

    @Inject
    MachineViewModel machineViewModel;

    private static final String TAG = MachineInfo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = findViewById(R.id.machine_info_name);
        mMoney = findViewById(R.id.machine_info_money);

        mRecyclerView = findViewById(R.id.rvNotes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFAB =  findViewById(R.id.fabAddIncome);
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

                            String notes = editText2.getText().toString();
                            String money = editText.getText().toString();

                            double value;
                            if(money.isEmpty() || money.equals("") || date == null || date.equals("")) {
                                Toast.makeText(v.getContext(), "Fecha y Dinero SON OBLIGATORIOS", Toast.LENGTH_SHORT).show();
                                hideSoftKeyboard(v);
                            } else {

                                if (notes.isEmpty() || notes.equals("")){
                                    notes = "Sin Observaciones";
                                }

                                value = Double.parseDouble(money);
                                disposable.add(incomeViewModel.addIncome(date, notes, value, id)
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


//                                machineViewModel.updateByID(id, total_amount)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(Schedulers.io())
//                                        .subscribe(
//                                                () -> Log.d(TAG, "MachineInfo: UPDATE COMPLETED"),
//                                                throwable -> Log.e(TAG, "MachineInfo: ERROR", throwable ));

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
                    if (incomes != null){
                        mAdapter = new ListAdapter(MachineInfo.this, incomes, machineViewModel, incomeViewModel, name);
                        mRecyclerView.setAdapter(mAdapter);

                        incomeList.addAll(incomes);

                        disposable.add(mAdapter.clickEvent
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(income_id -> incomeViewModel.deleteIncomeByID(income_id)));

                        mAdapter.notifyDataSetChanged();
                    }
                }, throwable -> Log.e(TAG, "onCreate: Unable to get machines", throwable)));



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
        mDayFinal = dayOfMonth;
        mMonthFinal = month + 1;
        mYearFinal = year;

        date = mDayFinal+"/"+mMonthFinal+"/"+mYearFinal;
        info_date.setText(date);
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

                String[] headers = new String[]{"ID", "Maquina", "Nota", "Dinero Recoletado"};
                Iterator<Income> iterator2 = incomeList.iterator();

                File file = new File(Environment.getExternalStorageDirectory(), "/Maquinas");
                File exportFile = new File(file, "Ingresos.csv");

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
                        double total_amount = income.getMoney();
                        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                        String formatted = formatter.format(total_amount);

                        writer.writeNext(new String[]{String.valueOf(id), name, note, formatted});
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

