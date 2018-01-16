package tech.destinum.machines.ACTIVITIES;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ADAPTERS.ListAdapter;
import tech.destinum.machines.R;
import tech.destinum.machines.data.POJO.Income;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.data.ViewModel.IncomeViewModel;

public class MachineInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView mName, mMoney;
    private RecyclerView mNotesList;
    private FloatingActionButton mFAB;
    private ListAdapter mAdapter;
    private Context mContext;
    private Calendar mCalendar;
    private TextView info_date;
    private int mDay, mMonth, mYear, mDayFinal, mMonthFinal, mYearFinal;
    private String date, name;
    private long id;
    private Boolean showMenu = false;

    private CompositeDisposable disposable;
    private MaybeObserver<Income> observer;
    private Maybe<List<Income>> maybe;

    @Inject
    IncomeViewModel incomeViewModel;

    private static final String TAG = MachineInfo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = findViewById(R.id.machine_info_name);
        mMoney = findViewById(R.id.machine_info_money);
        mFAB =  findViewById(R.id.fabAddIncome);
        mNotesList = findViewById(R.id.lvNotes);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String location = bundle.getString("name");
            final long id = bundle.getLong("id");
            this.id = id;
            name = location;
//
//            maybe = Maybe.create(new MaybeOnSubscribe<List<Income>>() {
//                @Override
//                public void subscribe(MaybeEmitter<List<Income>> e) throws Exception {
//
//                }
//            });
//            observer = new MaybeObserver<Income>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//
//                }
//
//                @Override
//                public void onSuccess(Income income) {
//                    double total_amount = income.getMoney();
//                    DecimalFormat formatter = new DecimalFormat("$#,##0.000");
//                    String formatted = formatter.format(total_amount);
//                    mMoney.setText(formatted);
//
//                    if (total_amount <= 0){
//                        showMenu = false;
//                    } else {
//                        showMenu = true;
//                    }
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            };
//
//            maybe.subscribe(observer);
//            disposable.add(Maybe.fromCallable(new Callable<Maybe>() {
//                @Override
//                public Maybe call() throws Exception {
//                    return incomeViewModel.getIncomeOfMachine(id);
//                }
//            })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(observer));

            Maybe.fromCallable(new Callable<Maybe<Income>>() {
                @Override
                public Maybe call() throws Exception {
                    return incomeViewModel.getIncomeOfMachine(id);
                }
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(money -> {

                        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                        String formatted = formatter.format(money);
                        mMoney.setText(formatted);

//                        if (money <= 0){
//                            showMenu = false;
//                        } else {
//                            showMenu = true;
//                        }
                }, throwable -> {
                    Log.e(TAG, "MachineInfo: ERROR GETTING INCOME",  throwable);
                });

//            double total_amount = mDB.getInstance(this).getIncomeDAO().getIncomeOfMachine(id).getMoney();



            mName.setText(location);

        }else{
            finish();
        }


//        mAdapter = new ListAdapter(this, mDB.getInstance(this).getIncomeDAO().getInfoOfMachine(id));
//        mNotesList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        mNotesList.setAdapter(mAdapter);

        mFAB.setOnClickListener(v -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_info, null, true);
                final EditText editText = view.findViewById(R.id.dialog_info_et);
//                editText.addTextChangedListener(new NumberTextWatcher(editText));
                final EditText editText2 = view.findViewById(R.id.dialog_info_notes_et);
                info_date = view.findViewById(R.id.dialog_info_date_tv);
                Button button = view.findViewById(R.id.dialog_info_date_btn);
                button.setOnClickListener(button1 ->{
                        mCalendar = Calendar.getInstance();
                        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                        mMonth = mCalendar.get(Calendar.MONTH);
                        mYear = mCalendar.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(MachineInfo.this, MachineInfo.this, mYear, mMonth, mDay);
                        datePickerDialog.show();

                });

                dialog.setNegativeButton("Cancelar", null).setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String notes = editText2.getText().toString();
                        Double money;
                        try {
                            money = new Double(editText.getText().toString());
                        } catch (NumberFormatException e){
                            money = 0.0;
                        }

//                        mDB.getIncomeDAO().addIncome(new Income(date, notes, money, id));
//                        mDBHelpter.insertNewIncome(money, date, notes, id);
//                        mAdapter.refreshAdapter(mDBHelpter.getInfoOfMachine(id));
//                        mDB.getInstance(v.getContext()).getIncomeDAO().getIncomeOfMachine(id).getMoney();
                        double total_amount = 222.222;
                        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                        String formatted = formatter.format(total_amount);
                        mMoney.setText(formatted);

                        invalidateOptionsMenu();
                        //Hide Softkeyboard
                        View view = v.getRootView();
                        if (view != null) {
                            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                    }
                }).setView(view).show();
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mDayFinal = dayOfMonth;
        mMonthFinal = month + 1;
        mYearFinal = year;

        date = mDayFinal+"/"+mMonthFinal+"/"+mYearFinal;
        info_date.setText(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenu == true){
            getMenuInflater().inflate(R.menu.menu_line_chart, menu);
            return super.onCreateOptionsMenu(menu);
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

//                double total_amount = mDB.getInstance(this).getIncomeDAO().getIncomeOfMachine(id).getMoney();
                double total_amount = 222.222;
                DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                String formatted = formatter.format(total_amount);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, name + " ha recaudado en total: "+formatted);
                startActivity(Intent.createChooser(sendIntent, "Compartir"));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

