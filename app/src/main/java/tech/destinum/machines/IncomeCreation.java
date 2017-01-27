package tech.destinum.machines;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncomeCreation extends AppCompatActivity {

    private TextView mLocation;
    private EditText mMoney, mNotes;
    private DatePicker mDate;
    private Calendar mCalendar;
    private DBHelpter mDBHelpter;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_creation);

        mDBHelpter = new DBHelpter(this);

        mMoney = (EditText) findViewById(R.id.etMoney);
        mNotes = (EditText) findViewById(R.id.etNotes);
        mDate = (DatePicker) findViewById(R.id.datePicker);
        mLocation = (TextView) findViewById(R.id.tvMachineLocation);

        SharedPreferences mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String location = mSharedPreferences.getString("location", "ejemplo");

        mLocation.setText(location);

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
                String notes = mNotes.getText().toString();
                Double money;
                try {
                    money = new Double(mMoney.getText().toString());
                } catch (NumberFormatException e){
                    money = 0.0;
                }
                mCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCalendar.set(year, month, dayOfMonth);
                    }
                };
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = sdf.format(mCalendar.getTime());

//                mDBHelpter.insertNewIncome(money, formattedDate, notes, );
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
