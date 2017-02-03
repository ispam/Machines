package tech.destinum.machines;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import java.text.DecimalFormat;

import static tech.destinum.machines.MachinesAdapter.PREFS_NAME;

public class MachineInfo extends AppCompatActivity {

    private TextView mLocation, mMoney;
    private DBHelpter mDBHelpter;
    private ListView mNotesList;
    private FloatingActionButton mFAB;
    private SQLiteDatabase db;
    private Cursor mCursor;
    private ListAdapter adapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDBHelpter = new DBHelpter(getApplicationContext());
        db = mDBHelpter.getWritableDatabase();

        mLocation = (TextView) findViewById(R.id.tvLocation);
        mMoney = (TextView) findViewById(R.id.tvMoney);
        mFAB = (FloatingActionButton) findViewById(R.id.fabAddIncome);
        mNotesList = (ListView) findViewById(R.id.lvNotes);

        SharedPreferences mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final Long machines_id = mSharedPreferences.getLong("machines_id", 0);

        double total_amount = mDBHelpter.getIncomeOfMachine(machines_id);
        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(total_amount);
        mMoney.setText(formatted);

        String location = mSharedPreferences.getString("location", null);
        mLocation.setText(location);

        adapter = new ListAdapter(this, mDBHelpter.getInfoOfMachine(machines_id));
        adapter.notifyDataSetChanged();
        mNotesList.setAdapter(adapter);


        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IncomeCreation.class);
                startActivity(i);
            }
        });
    }
}

