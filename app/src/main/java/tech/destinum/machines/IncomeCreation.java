package tech.destinum.machines;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IncomeCreation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView mLocation, mDateHint;
    private EditText mMoney, mNotes;
    private Button mPickDate;
    private Calendar mCalendar;
    private DBHelpter mDBHelpter;
    private int mDay, mMonth, mYear, mDayFinal, mMonthFinal, mYearFinal;

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_creation);

        mDBHelpter = new DBHelpter(this);

        mMoney = (EditText) findViewById(R.id.etMoney);
        mNotes = (EditText) findViewById(R.id.etNotes);
        mLocation = (TextView) findViewById(R.id.tvMachineLocation);
        mDateHint = (TextView) findViewById(R.id.tvDateHint);
        mPickDate = (Button) findViewById(R.id.btnPickDate);

        SharedPreferences mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String location = mSharedPreferences.getString("location", "ejemplo");
        mLocation.setText(location);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar = Calendar.getInstance();
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                mMonth = mCalendar.get(Calendar.MONTH);
                mYear = mCalendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(IncomeCreation.this, IncomeCreation.this, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_confirmation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.confirmation:
                SharedPreferences mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                Long machines_id = mSharedPreferences.getLong("machines_id", 0);

                String notes = mNotes.getText().toString();
                Double money;
                try {
                    money = new Double(mMoney.getText().toString());
                } catch (NumberFormatException e){
                    money = 0.0;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = sdf.format(mCalendar.getTime());

                mDBHelpter.insertNewIncome(money, formattedDate, notes, machines_id);
                Log.d("INCOME:", mDBHelpter.getIncomeOfMachine(machines_id).toString());

                Bundle bundle = new Bundle();
                bundle.putDouble("money", money);
                Intent i = new Intent(getApplicationContext(), MachineInfo.class);
                i.putExtras(bundle);
                startActivity(i);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mDayFinal = dayOfMonth;
        mMonthFinal = month + 1;
        mYearFinal = year;
        mDateHint.setText(mDayFinal+"/"+mMonthFinal+"/"+mYearFinal);
    }
}
