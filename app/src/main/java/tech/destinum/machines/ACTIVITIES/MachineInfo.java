package tech.destinum.machines.ACTIVITIES;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import java.text.DecimalFormat;

import tech.destinum.machines.ADAPTERS.ListAdapter;
import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.R;

public class MachineInfo extends AppCompatActivity {

    private TextView mName, mMoney;
    private DBHelpter mDBHelpter;
    private RecyclerView mNotesList;
    private FloatingActionButton mFAB;
    private ListAdapter mAdapter;
    private Context mContext;

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
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IncomeCreation.class);
                startActivity(i);
            }
        });
    }
}

