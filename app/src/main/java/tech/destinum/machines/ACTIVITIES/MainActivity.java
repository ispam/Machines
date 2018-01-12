package tech.destinum.machines.ACTIVITIES;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ADAPTERS.MachinesAdapter;
import tech.destinum.machines.R;
import tech.destinum.machines.data.MachinesDB;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.data.ViewModel.MachineViewModel;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFAB;
    private RecyclerView mRecyclerView;
    private MachinesAdapter mAdapter;

    @Inject
    MachinesDB mDB;

    @Inject
    MachineViewModel viewModel;

    private CompositeDisposable disposable = new CompositeDisposable();

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getComponent().inject(this);

        mRecyclerView = findViewById(R.id.recycler_view_main);
        mFAB = findViewById(R.id.fabAddMachine);

        disposable.add(mDB.getMachineDAO().getAllMachines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(machines -> {
                    if (machines != null) {
                        mAdapter = new MachinesAdapter(machines, MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }, throwable -> {
                    Log.e(TAG, "onCreate: Unable to get machines", throwable);
                }));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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


        mFAB.setOnClickListener(v -> {
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_main, null, true);
                final EditText mEditText =   view.findViewById(R.id.dialog_et);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setNegativeButton("Cancelar", null)
                        .setPositiveButton("Crear", (dialog, which) -> {
                            String machine = mEditText.getText().toString();
                            disposable.add(viewModel.addMachine(machine)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(emitter -> {
                                        mAdapter.notifyDataSetChanged();
                                    }, throwable -> {
                                        Log.e(TAG, "onCreate: MACHINE NOT CREATED");
                                    }));

                }).setView(view).show();
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
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