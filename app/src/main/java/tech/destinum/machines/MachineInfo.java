package tech.destinum.machines;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import static tech.destinum.machines.MachinesAdapter.PREFS_NAME;

public class MachineInfo extends AppCompatActivity {

    private TextView mLocation, mMoney, mNotes;
    private DBHelpter mDBHelpter;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        mDBHelpter = new DBHelpter(getApplicationContext());

        mLocation = (TextView) findViewById(R.id.tvLocation);
        mMoney = (TextView) findViewById(R.id.tvMoney);
        mNotes = (TextView) findViewById(R.id.tvNotes);
        mFAB = (FloatingActionButton) findViewById(R.id.fabAddIncome);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mMoney.setText("$" + String.valueOf(bundle.getDouble("money")));
        }

        SharedPreferences mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String location = mSharedPreferences.getString("location", null);
        mLocation.setText(location);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IncomeCreation.class);
                startActivity(i);
            }
        });
    }
}

