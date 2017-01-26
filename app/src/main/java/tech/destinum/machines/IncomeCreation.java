package tech.destinum.machines;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncomeCreation extends AppCompatActivity {

    private EditText mMoney, mNotes;
    private DatePicker mDate;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_creation);

        mMoney = (EditText) findViewById(R.id.etMoney);
        mNotes = (EditText) findViewById(R.id.etNotes);
        mDate = (DatePicker) findViewById(R.id.datePicker);
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
                Double money = Double.valueOf(mMoney.getText().toString());
                DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCalendar = Calendar.getInstance();
                        mCalendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = sdf.format(mCalendar.getTime());
                    }
                };

                if (money.equals("")|| money <= 0){

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
