package tech.destinum.machines;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static tech.destinum.machines.MachinesAdapter.PREFS_NAME;

public class MachineInfo extends AppCompatActivity {

    private TextView mLocation, mMoney, mNotes;
    private DBHelpter mDBHelpter;
    private ListView mNotesList;
    private FloatingActionButton mFAB;
    private SQLiteDatabase db;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDBHelpter = new DBHelpter(getApplicationContext());
        db = mDBHelpter.getWritableDatabase();

        mLocation = (TextView) findViewById(R.id.tvLocation);
        mMoney = (TextView) findViewById(R.id.tvMoney);
        mNotes = (TextView) findViewById(R.id.tvNotes);
        mFAB = (FloatingActionButton) findViewById(R.id.fabAddIncome);
        mNotesList = (ListView) findViewById(R.id.lvNotes);

        SharedPreferences mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Long machines_id = mSharedPreferences.getLong("machines_id", 0);

        double total_amount = mDBHelpter.getIncomeOfMachine(machines_id);
        mMoney.setText(String.format("%.3f",total_amount));

        String location = mSharedPreferences.getString("location", null);
        mLocation.setText(location);

        mCursor = db.rawQuery("SELECT _id, note, date FROM income WHERE machines_id = "+machines_id+"",null);
        ListAdapter adapter = new ListAdapter(this, mCursor);
        mNotesList.setAdapter(adapter);
        db.close();

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IncomeCreation.class);
                startActivity(i);
            }
        });

    }
}

