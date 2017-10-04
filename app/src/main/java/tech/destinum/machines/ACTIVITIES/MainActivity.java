package tech.destinum.machines.ACTIVITIES;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tech.destinum.machines.ADAPTERS.MachinesAdapter;
import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.R;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFAB;
    private DBHelpter mDBHelpter;
    private RecyclerView mRecyclerView;
    private MachinesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelpter = new DBHelpter(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mFAB = (FloatingActionButton) findViewById(R.id.fabAddMachine);

        mAdapter = new MachinesAdapter(this, mDBHelpter.getAllMachines());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

//        if (mDBHelpter.getAllMachines().size() == 0){
//            mLayout.setVisibility(View.VISIBLE);
//            mRecyclerViewDetails.setVisibility(View.GONE);
//
//            mAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//
//                    Intent intent = new Intent(Home.this, Selection.class);
//                    startActivity(intent);
//
//                }
//            });
//
//        } else {
//            mLayout.setVisibility(View.GONE);
//            mRecyclerViewDetails.setVisibility(View.VISIBLE);
//        }


        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_main, null, true);
                final EditText mEditText = (EditText) view.findViewById(R.id.dialog_et);
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setNegativeButton("Cancelar", null).setPositiveButton("Crear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDBHelpter.insertNewMachine(mEditText.getText().toString());
                        mAdapter.refreshAdapter(mDBHelpter.getAllMachines());
                    }
                }).setView(view).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_graph:
                Intent intent = new Intent(MainActivity.this, Graph.class);
                startActivity(intent);

                break;

            case R.id.share_machines:

//                DecimalFormat formatter = new DecimalFormat("$#,##0.000");
//                String formatted = formatter.format(total_amount);
//
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.setType("text/plain");
//                sendIntent.putExtra(Intent.EXTRA_TEXT, name + " ha recaudado en total: "+formatted);
//                startActivity(Intent.createChooser(sendIntent, "Compartir"));

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}