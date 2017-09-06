package tech.destinum.machines.ACTIVITIES;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.Calendar;

import tech.destinum.machines.ADAPTERS.ListAdapter;
import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.R;

public class MachineInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView mName, mMoney;
    private DBHelpter mDBHelpter;
    private RecyclerView mNotesList;
    private FloatingActionButton mFAB;
    private ListAdapter mAdapter;
    private Context mContext;
    private Calendar mCalendar;
    private TextView info_date;
    private int mDay, mMonth, mYear, mDayFinal, mMonthFinal, mYearFinal;
    private static final String PREFS_NAME = "MachineInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDBHelpter = new DBHelpter(getApplicationContext());

        mName = (TextView) findViewById(R.id.machine_info_name);
        mMoney = (TextView) findViewById(R.id.machine_info_money);
        mFAB = (FloatingActionButton) findViewById(R.id.fabAddIncome);
        mNotesList = (RecyclerView) findViewById(R.id.lvNotes);

        Bundle bundle = getIntent().getExtras();
        String location = bundle.getString("name");
        long id = bundle.getLong("id");
        double total_amount = mDBHelpter.getIncomeOfMachine(id);

        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(total_amount);

        mMoney.setText(formatted);
        mName.setText(location);

        mAdapter = new ListAdapter(this, mDBHelpter.getInfoOfMachine(id));
        mNotesList.setAdapter(mAdapter);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_info, null, true);
                EditText editText = (EditText) view.findViewById(R.id.dialog_info_et);
                EditText editText2 = (EditText) view.findViewById(R.id.dialog_info_notes_et);
                info_date = (TextView) view.findViewById(R.id.dialog_info_date_tv);
                Button button = (Button) view.findViewById(R.id.dialog_info_date_btn);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCalendar = Calendar.getInstance();
                        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                        mMonth = mCalendar.get(Calendar.MONTH);
                        mYear = mCalendar.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(MachineInfo.this, MachineInfo.this, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });
                dialog.setNegativeButton("Cancelar", null).setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        mDBHelpter.insertNewIncome();

//                        View view = v.getRootView();
//                        if (view != null) {
//                            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                        }

                    }
                }).setView(view).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mDayFinal = dayOfMonth;
        mMonthFinal = month + 1;
        mYearFinal = year;

        SharedPreferences mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String date = mDayFinal+"/"+mMonthFinal+"/"+mYearFinal;
        mEditor.putString("date", date);
        mEditor.commit();

        info_date.setText(date);
    }
}

