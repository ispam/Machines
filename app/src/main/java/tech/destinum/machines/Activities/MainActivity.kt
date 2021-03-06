package tech.destinum.machines.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opencsv.CSVWriter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import tech.destinum.machines.Adapters.MachinesAdapter
import tech.destinum.machines.Data.Local.Entities.Machine
import tech.destinum.machines.Data.Local.ViewModel.IncomeViewModel
import tech.destinum.machines.Data.Local.ViewModel.MachineViewModel
import tech.destinum.machines.R
import tech.destinum.machines.UTILS.MoneyFormatter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val PERMISSION_WRITE_EXTERNAL_STORAGE = 112
    }

    private val disposable = CompositeDisposable()
    private val machineList = ArrayList<Machine>()

    @Inject
    lateinit var machineViewModel: MachineViewModel

    @Inject
    lateinit var incomeViewModel: IncomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component.inject(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        recycler_view_main.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        setUpFAB()
        getDbInfo()

    }

    private fun getDbInfo() {
        val cal: Calendar = Calendar.getInstance()
        val month = (cal.time.month) + 1
        val monthText = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

        main_month.text = monthText.capitalize()

        disposable.add(machineViewModel.allMachines
                .subscribeOn(Schedulers.io())
                .switchIfEmpty {
                    val emptyList = mutableListOf<Machine>()
                    Observable.just(emptyList)
                }
                .filter { it.isNotEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { Log.e("allMachines", it.message) }
                .map {
                    machineList.clear()
                    val adapter = MachinesAdapter(it, this@MainActivity)
                    recycler_view_main.adapter = adapter
                    machineList.addAll(it)
                    deleteByID(adapter)
                    adapter.notifyDataSetChanged()
                }.subscribe())

        disposable.add(incomeViewModel.totalObtained()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    total_obtain.visibility = View.VISIBLE
                    dinero_total.visibility = View.VISIBLE
                    total_obtain.text = MoneyFormatter.moneyFormat(it)
                }
                .doOnComplete { Log.d(TAG, "getDbInfo: COMPLETED") }
                .doOnError { e -> Log.e("getDbInfo", e.message) }
                .subscribe())

        disposable.add(incomeViewModel.getTotalMonth(month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e("getTotalMonth", e.message) }
                .onErrorReturnItem(0.0)
                .doOnSuccess {
                    main_month.visibility = View.VISIBLE
                    main_total_month.visibility = View.VISIBLE
                    main_total_month.text = MoneyFormatter.moneyFormat(it)
                }
                .subscribe())

    }

    private fun deleteByID(mAdapter: MachinesAdapter) {
        disposable.add(mAdapter.clickEvent
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext { machineViewModel.deleteByID(it) }
                .subscribe())
    }

    private fun setUpFAB() {
        fabAddMachine.setOnClickListener { v ->

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
                                .doOnError { e -> Log.e(TAG, e.message) }
                                .subscribe { getDbInfo() })
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
                Toast.makeText(this, "SIN ESTE PERMISO NO PUEDE FUNCIONAR ADECUADAMENTE LA APP", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ onDestroy() }, 3000)
            }
        }
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


}