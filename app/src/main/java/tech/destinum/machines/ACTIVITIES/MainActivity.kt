package tech.destinum.machines.ACTIVITIES

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.opencsv.CSVWriter

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DecimalFormat
import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.machines.ADAPTERS.MachinesAdapter
import tech.destinum.machines.R
import tech.destinum.machines.data.local.POJO.Machine
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel
import tech.destinum.machines.data.local.ViewModel.MachineViewModel


class MainActivity : AppCompatActivity() {
    private var mFAB: FloatingActionButton? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MachinesAdapter? = null
    private val disposable = CompositeDisposable()
    private val machineList = ArrayList<Machine>()
    private val context: Context? = null
    private var mTotal: TextView? = null
    private var mTotalText: TextView? = null

    @Inject
    lateinit var machineViewModel: MachineViewModel

    @Inject
    lateinit var incomeViewModel: IncomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component.inject(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        mTotal = findViewById(R.id.total_obtain)
        mTotalText = findViewById(R.id.dinero_total)

        mRecyclerView = findViewById(R.id.recycler_view_main)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mFAB = findViewById(R.id.fabAddMachine)
        mFAB!!.setOnClickListener { v ->

            val inflater = v.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.dialog_add_machine, null, true)
            val mEditText = view.findViewById<EditText>(R.id.dialog_et)

            val alertDialog = AlertDialog.Builder(v.context)
            alertDialog
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Crear") { dialog, which ->

                        val machine = mEditText.text.toString()
                        disposable.add(machineViewModel.addMachine(machine, 0.0)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        { mAdapter!!.notifyDataSetChanged() }
                                ) { throwable -> Log.e(TAG, "MachineInfo: ", throwable) })
                    }.setView(view).show()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_WRITE_EXTERNAL_STORAGE)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_WRITE_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "MainActivity: PERMISSION GRANTED")

            } else {
                Toast.makeText(context, "SIN ESTE PERMISO NO PUEDE FUNCIONAR ADECUADAMENTE LA APP", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({ onDestroy() }, 3000)

            }
        }
    }

    override fun onStart() {
        super.onStart()

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

        disposable.add(machineViewModel!!.allMachines
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ machines ->
                    if (machines != null) {
                        machineList.clear()

                        mAdapter = MachinesAdapter(machines, this@MainActivity)
                        mRecyclerView!!.adapter = mAdapter

                        machineList.addAll(machines)


                        disposable.add(mAdapter!!.clickEvent
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe { machine -> machineViewModel!!.deleteByID(machine!!) })

                        mAdapter!!.notifyDataSetChanged()
                    }
                }) { throwable -> Log.e(TAG, "onCreate: Unable to get machines", throwable) })

        disposable.add(incomeViewModel!!.totalObtained()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ success ->
                    mTotal!!.visibility = View.VISIBLE
                    mTotalText!!.visibility = View.VISIBLE
                    val formatter = DecimalFormat("$#,##0.000")
                    val formatted = formatter.format(success)
                    mTotal!!.text = formatted
                }, { throwable -> Log.e(TAG, "MainActivity: ERROR") }
                ) { Log.d(TAG, "MainActivity: COMPLETED") })

    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_graph, menu)
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val spanString = SpannableString(menu.getItem(i).title.toString())
            spanString.setSpan(ForegroundColorSpan(Color.BLACK), 0, spanString.length, 0) //fix the color to white
            item.title = spanString
        }
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_graph -> {
                val intent = Intent(this@MainActivity, Graph::class.java)
                startActivity(intent)
            }

            R.id.share_machines -> {

                val machines = ArrayList<String>()
                val iterator = machineList.iterator()

                while (iterator.hasNext()) {
                    val machine = iterator.next()
                    val name = machine.name

                    val total_amount = machine.total_income
                    val formatter = DecimalFormat("$#,##0.000")
                    val formatted = formatter.format(total_amount)

                    machines.add("$name = $formatted")
                }
                val separated = machines.toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace(", ", "\n")

                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Maquinas: \n$separated")
                startActivity(Intent.createChooser(sendIntent, "Compartir"))
            }

            R.id.export_csv_machines -> {

                val headers = arrayOf("ID", "Maquina", "Ingreso Total")
                val iterator2 = machineList.iterator()

                val file = File(Environment.getExternalStorageDirectory(), "/Maquinas")
                val exportFile = File(file, "Maquinas.csv")

                if (!file.exists()) {
                    file.mkdirs()
                }

                try {
                    if (exportFile.exists()) {
                        exportFile.delete()
                    }
                    exportFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {

                    val writer = CSVWriter(FileWriter(exportFile, true))
                    writer.writeNext(headers)

                    while (iterator2.hasNext()) {
                        val machine = iterator2.next()
                        val id = machine.id
                        val name = machine.name
                        val total_amount = machine.total_income
                        val formatter = DecimalFormat("$#,##0.000")
                        val formatted = formatter.format(total_amount)

                        writer.writeNext(arrayOf(id.toString(), name, formatted))
                    }

                    writer.close()

                    val export = Intent()
                    export.action = Intent.ACTION_SEND
                    export.type = "text/csv"
                    export.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile))
                    export.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(Intent.createChooser(export, "Exportar CSV"), 512)

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private val TAG = MainActivity::class.java!!.getSimpleName()

        private val PERMISSION_WRITE_EXTERNAL_STORAGE = 112
    }

}