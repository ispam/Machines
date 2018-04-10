package tech.destinum.machines.ACTIVITIES;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ADAPTERS.MachinesAdapter;
import tech.destinum.machines.R;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Income;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.data.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.ViewModel.MachineViewModel;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton mFAB;
    private RecyclerView mRecyclerView;
    private MachinesAdapter mAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    MachineViewModel machineViewModel;

    @Inject
    IncomeViewModel incomeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getComponent().inject(this);

        mRecyclerView = findViewById(R.id.recycler_view_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFAB = findViewById(R.id.fabAddMachine);
        mFAB.setOnClickListener(v -> {

            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_main, null, true);
            EditText mEditText =   view.findViewById(R.id.dialog_et);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
            alertDialog
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Crear", (dialog, which) -> {

                        String machine = mEditText.getText().toString();
                        disposable.add(machineViewModel.addMachine(machine, 0.0)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> mAdapter.notifyDataSetChanged(),
                                        throwable -> Log.e(TAG, "MachineInfo: ", throwable)));
            }).setView(view).show();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

//        disposable.add(machineViewModel.getAllMachines()
//                .zipWith(incomeViewModel.getAllMachinesIncome(), Pair::new)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(machinesAndIncomePair -> {
//                    List<Machine> machines = machinesAndIncomePair.first;
//                    List<Double> incomes = machinesAndIncomePair.second;
//                    if (machines != null && incomes != null) {
//                        mAdapter = new MachinesAdapter(machines, incomes, MainActivity.this);
//                        mRecyclerView.setAdapter(mAdapter);
//                    }
//                }, throwable -> Log.e(TAG, "onCreate: Unable to get machines", throwable)));

        disposable.add(machineViewModel.getAllMachines()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(machines -> {
                    if (machines != null) {
                        mAdapter = new MachinesAdapter(machines, MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.clickEvent.subscribe(machine -> {
                            machineViewModel.deleteByID(machine);
                            Toast.makeText(this, "PublishSubject " + machine, Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                        });
                    }

                }, throwable -> Log.e(TAG, "onCreate: Unable to get machines", throwable)));

    }

    @Override
    protected void onStop() {
        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
        super.onStop();
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