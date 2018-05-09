package tech.destinum.machines.ACTIVITIES;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.ADAPTERS.MachinesAdapter;
import tech.destinum.machines.R;
import tech.destinum.machines.data.local.POJO.Machine;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton mFAB;
    private RecyclerView mRecyclerView;
    private MachinesAdapter mAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<Machine> machineList = new ArrayList<>();
    private Context context;
    private TextView mTotal, mTotalText;

    @Inject
    MachineViewModel machineViewModel;

    @Inject
    IncomeViewModel incomeViewModel;

    private final static int PERMISSION_WRITE_EXTERNAL_STORAGE = 112;
    private int grantResults[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mTotal = findViewById(R.id.total_obtain);
        mTotalText = findViewById(R.id.dinero_total);

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "MainActivity: PERMISSION GRANTED");

                } else {
                    Toast.makeText(context, "SIN ESTE PERMISO NO PUEDE FUNCIONAR ADECUADAMENTE LA APP", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> onDestroy(), 3000);

                }
                break;
        }
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
                        machineList.clear();

                        mAdapter = new MachinesAdapter(machines, MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);

                        machineList.addAll(machines);


                        disposable.add(mAdapter.clickEvent
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(machine -> machineViewModel.deleteByID(machine)));

                        mAdapter.notifyDataSetChanged();
                    }
                }, throwable -> Log.e(TAG, "onCreate: Unable to get machines", throwable)));

        disposable.add(incomeViewModel.totalObtained()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                            mTotal.setVisibility(View.VISIBLE);
                            mTotalText.setVisibility(View.VISIBLE);
                            DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                            String formatted = formatter.format(success);
                            mTotal.setText(formatted);
                        }, throwable -> Log.e(TAG, "MainActivity: ERROR"),
                        () -> Log.d(TAG, "MainActivity: COMPLETED")));

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
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_graph:
                Intent intent = new Intent(MainActivity.this, Graph.class);
                startActivity(intent);

                break;

            case R.id.share_machines:

                List<String> machines = new ArrayList<>();
                Iterator<Machine> iterator = machineList.iterator();

                while (iterator.hasNext()){
                    Machine machine = iterator.next();
                    String name = machine.getName();

                    double total_amount = machine.getTotal_income();
                    DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                    String formatted = formatter.format(total_amount);

                    machines.add(name + " = " + formatted);
                }
                String separated = machines.toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace(", ", "\n");
                
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Maquinas: \n" + separated );
                startActivity(Intent.createChooser(sendIntent, "Compartir"));

                break;

            case R.id.export_csv_machines:

                String[] headers = new String[]{"ID", "Maquina", "Ingreso Total"};
                Iterator<Machine> iterator2 = machineList.iterator();

                File file = new File(Environment.getExternalStorageDirectory(), "/Maquinas");
                File exportFile = new File(file, "Maquinas.csv");

                if (!file.exists()) {
                    file.mkdirs();
                }

                try {
                    if (exportFile.exists()){
                        exportFile.delete();
                    }
                    exportFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    CSVWriter writer = new CSVWriter(new FileWriter(exportFile, true));
                    writer.writeNext(headers);

                    while (iterator2.hasNext()) {
                        Machine machine = iterator2.next();
                        long id = machine.getId();
                        String name = machine.getName();
                        double total_amount = machine.getTotal_income();
                        DecimalFormat formatter = new DecimalFormat("$#,##0.000");
                        String formatted = formatter.format(total_amount);

                        writer.writeNext(new String[]{String.valueOf(id), name, formatted});
                    }

                    writer.close();

                    Intent export = new Intent();
                    export.setAction(Intent.ACTION_SEND);
                    export.setType("text/csv");
                    export.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));
                    export.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(Intent.createChooser(export, "Exportar CSV"), 512);

                } catch (IOException e) {
                    e.printStackTrace();
                }



                break;

        }
        return super.onOptionsItemSelected(item);
    }

}