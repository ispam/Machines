package tech.destinum.machines;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;

public class IncomeCreation extends AppCompatActivity {

    private EditText mMoney, mNotes;
    private DatePicker mDate;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_creation);

        mMoney = (EditText) findViewById(R.id.etMoney);
        mNotes = (EditText) findViewById(R.id.etNotes);
        mDate = (DatePicker) findViewById(R.id.datePicker);
        mFAB = (FloatingActionButton) findViewById(R.id.fabAddIncome2);
    }
}
