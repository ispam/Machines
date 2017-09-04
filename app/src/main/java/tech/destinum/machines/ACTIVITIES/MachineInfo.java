package tech.destinum.machines.ACTIVITIES;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import java.text.DecimalFormat;

import tech.destinum.machines.ADAPTERS.ListAdapter;
import tech.destinum.machines.ADAPTERS.MachinesAdapter;
import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.IncomeProvider;
import tech.destinum.machines.R;

public class MachineInfo extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView mLocation, mMoney;
    private DBHelpter mDBHelpter;
    private ListView mNotesList;
    private FloatingActionButton mFAB;
    private SQLiteDatabase db;
    private Cursor mCursor;
    private ListAdapter mAdapter;
    private Context mContext;
    private static final int LOADER_INTEGER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(LOADER_INTEGER, null, this);

        mDBHelpter = new DBHelpter(getApplicationContext());
        db = mDBHelpter.getWritableDatabase();

        mLocation = (TextView) findViewById(R.id.tvLocation);
        mMoney = (TextView) findViewById(R.id.tvMoney);
        mFAB = (FloatingActionButton) findViewById(R.id.fabAddIncome);
        mNotesList = (ListView) findViewById(R.id.lvNotes);

        SharedPreferences mSharedPreferences = getSharedPreferences(MachinesAdapter.PREFS_NAME, Context.MODE_PRIVATE);
        final Long machines_id = mSharedPreferences.getLong("machines_id", 0);

        double total_amount = mDBHelpter.getIncomeOfMachine(machines_id);
        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
        String formatted = formatter.format(total_amount);
        mMoney.setText(formatted);

//        Intent i = new Intent(this, MachinesAdapter.class);
//        ((Activity) mContext).startActivityForResult(i, 1);

        String location = mSharedPreferences.getString("location", null);
        mLocation.setText(location);

        mAdapter = new ListAdapter(this, mDBHelpter.getInfoOfMachine(machines_id));
        mNotesList.setAdapter(mAdapter);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IncomeCreation.class);
                startActivity(i);
            }
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 1){
//            if (resultCode == Activity.RESULT_OK){
//                String total_amount = data.getStringExtra("total_amount");
//                mMoney.setText(total_amount);
//
//            }
//        }
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, IncomeProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

