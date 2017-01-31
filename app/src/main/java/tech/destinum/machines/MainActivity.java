package tech.destinum.machines;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private DBHelpter mDBHelpter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelpter = new DBHelpter(this);

        mFAB = (FloatingActionButton) findViewById(R.id.fabAddMachine);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvNew);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new MachinesAdapter(getApplicationContext(), mDBHelpter.getAllMachines()));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MachineCreation.class);
                startActivity(i);
            }
        });

    }
}
