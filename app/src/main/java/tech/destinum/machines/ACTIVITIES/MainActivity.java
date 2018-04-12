package tech.destinum.machines.ACTIVITIES;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ADAPTERS.MachinesAdapter;
import tech.destinum.machines.R;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton mFAB;
    private RecyclerView mRecyclerView;
    private MachinesAdapter mAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Boolean showMenu = false;

    @Inject
    MachineViewModel machineViewModel;

    @Inject
    IncomeViewModel incomeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mRecyclerView = findViewById(R.id.recycler_view_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFAB = findViewById(R.id.fabAddMachine);
        mFAB.setOnClickListener(v -> {

            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_add_machine, null, true);
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

                        disposable.add(mAdapter.clickEvent
                                .observeOn(Schedulers.io())
                                .subscribeOn(Schedulers.io())
                                .subscribe(machine -> machineViewModel.deleteByID(machine)));

                        mAdapter.notifyDataSetChanged();
                        showMenu = true;
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

        if (showMenu){
            getMenuInflater().inflate(R.menu.menu_graph, menu);
            for(int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); //fix the color to white
                item.setTitle(spanString);
            }
            return true;
        } else {
            return false;
        }
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