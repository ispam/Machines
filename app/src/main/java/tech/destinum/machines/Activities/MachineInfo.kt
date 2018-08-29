package tech.destinum.machines.Activities

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.support.design.widget.FloatingActionButton
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
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


import com.opencsv.CSVWriter

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.LinkedHashMap

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.machines.Adapters.DateItem
import tech.destinum.machines.Adapters.IncomeItem
import tech.destinum.machines.Adapters.InfoItems
import tech.destinum.machines.Adapters.ListAdapter
import tech.destinum.machines.R
import tech.destinum.machines.UTILS.NumberTextWatcher
import tech.destinum.machines.Data.Local.ViewModel.IncomeViewModelFactory
import tech.destinum.machines.Data.Local.Entities.Income
import tech.destinum.machines.Data.Local.ViewModel.IncomeViewModel
import tech.destinum.machines.Data.Local.ViewModel.MachineViewModel

class MachineInfo : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    companion object {
        private val TAG = MachineInfo::class.java.simpleName
    }

    private lateinit var mName: TextView
    private lateinit var mMoney: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFAB: FloatingActionButton
    private lateinit var mAdapter: ListAdapter
    private lateinit var mCalendar: Calendar
    private lateinit var info_date: TextView
    private var mDay: Int = 0
    private var mMonth: Int = 0
    private var mYear: Int = 0
    private lateinit var name: String
    private var total_amount2: String? = null
    private var id: Long = 0
    private var date: Long = 0
    private lateinit var check: ImageView
    private var showMenu: Boolean? = false
    private val mInfoItems = ArrayList<InfoItems>()
    private var mIncomeList = ArrayList<Income>()
    private var value: Double = 0.toDouble()
    private val totalMonth: Double = 0.toDouble()
    private var notes: String? = null

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var incomeViewModel: IncomeViewModel

    @Inject
    lateinit var machineViewModel: MachineViewModel

    @Inject
    lateinit var mIncomeViewModelFactory: IncomeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machine_info)

        App.component.inject(this)
        incomeViewModel = ViewModelProviders.of(this, mIncomeViewModelFactory).get(IncomeViewModel::class.java)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mName = findViewById(R.id.machine_info_name)
        mMoney = findViewById(R.id.machine_info_money)

        mRecyclerView = findViewById(R.id.rvNotes)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mFAB = findViewById(R.id.fabAddIncome)

        getFABClick()

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = calendar.time
        Log.v("calendar", "${sdf.format(date)}")
    }

    override fun onStart() {
        super.onStart()

        val bundle = intent.extras
        if (bundle != null) {
            val location = bundle.getString("name")
            val id = bundle.getLong("id")
            this.id = id
            name = location
            mName.text = location
        } else {
            finish()
        }


        disposable.add(incomeViewModel.getIncomeOfMachine(id)
                .subscribeOn(Schedulers.io())
                .defaultIfEmpty(0.0)
                .doOnError { e -> Log.e("getIncomeOfMachine", e.message) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Log.v("getIncomeOfMachine", it.toString())
                    val formatter = DecimalFormat("$#,##0.000")
                    val formatted = formatter.format(it)
                    mMoney.text = formatted
                    total_amount2 = formatted
                    showMenu = true
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMapCompletable { machineViewModel.updateByID(id, it) }
                .subscribe())


        disposable.add(incomeViewModel.getAllIncomesOfMachine(id)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e("getAllIncomesOfMachine", e.localizedMessage) }
                .doOnNext {
                    mInfoItems.clear()
                    mIncomeList.clear()
                    mIncomeList = it as ArrayList<Income>

                    val hashMap = toMap(mIncomeList) as HashMap<String, Any>
                    for (header in hashMap.keys) {
                        val dateItem = DateItem(header)
                        mInfoItems.add(dateItem)

                        for (income in hashMap[header] as List<Income>) {
                            val incomeItem = IncomeItem(income)
                            mInfoItems.add(incomeItem)
                        }

                    }
                    mAdapter = ListAdapter(this@MachineInfo, mInfoItems, incomeViewModel, name, id)
                    mRecyclerView.adapter = mAdapter

                    disposable.add(mAdapter.clickEvent
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe { income_id -> incomeViewModel.deleteIncomeByID(income_id!!) })
                }
                .subscribe())

    }

    private fun toMap(incomes: MutableList<Income>): Map<String, List<Income>> {
        val map = LinkedHashMap<String, MutableList<Income>>()

        for (income in incomes) {

            val dateLong = income.date
            val sdf = SimpleDateFormat("MMMM")
            val dateString = sdf.format(Date(dateLong))

            var value: MutableList<Income>? = map[dateString]
            if (value == null) {
                value = ArrayList()
                map[dateString] = value
            }
            value.add(income)

        }

        return map
    }

    private fun getFABClick() {
        mFAB.setOnClickListener { v ->
            val dialog = AlertDialog.Builder(v.context)
            val inflater = v.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.dialog_add_income, null, true)

            val editText = view.findViewById<EditText>(R.id.dialog_info_et)
            editText.addTextChangedListener(NumberTextWatcher(editText))
            check = view.findViewById(R.id.check)
            val editText2 = view.findViewById<EditText>(R.id.dialog_info_notes_et)
            info_date = view.findViewById(R.id.dialog_info_date_tv)
            val button = view.findViewById<Button>(R.id.dialog_info_date_btn)
            button.setOnClickListener {

                mCalendar = Calendar.getInstance()
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
                mMonth = mCalendar.get(Calendar.MONTH)
                mYear = mCalendar.get(Calendar.YEAR)

                val datePickerDialog = DatePickerDialog(this@MachineInfo, R.style.datepicker, this@MachineInfo, mYear, mMonth, mDay)
                datePickerDialog.show()

                hideSoftKeyboard(v)

            }

            dialog.setNegativeButton("Cancelar", null)
                    .setPositiveButton("Agregar") { dialog1, which ->

                        notes = editText2.text.toString()
                        val money = editText.text.toString()

                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val dateString = sdf.format(Date(date))

                        if (money.isEmpty() || money == "" || dateString == null || dateString == "31/12/1969") {
                            Toast.makeText(v.context, "Fecha y Dinero SON OBLIGATORIOS", Toast.LENGTH_SHORT).show()
                            hideSoftKeyboard(v)
                        } else {

                            if (notes!!.isEmpty() || notes == "") {
                                notes = "Sin Observaciones"
                            }

                            value = java.lang.Double.parseDouble(money)
                            disposable.add(incomeViewModel.addIncome(date, notes!!, value, id, mMonth)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnError { throwable -> Log.e(TAG, "MachineInfo: ", throwable) }
                                    .doOnComplete { Log.d(TAG, "MachineInfo: INCOME ADDED") }
                                    .subscribe())
                            hideSoftKeyboard(v)
                        }
                    }.setView(view).show()
        }
    }

    private fun hideSoftKeyboard(v: View) {
        invalidateOptionsMenu()
        //Hide Softkeyboard
        val view1 = v.rootView
        if (view1 != null) {
            val inputManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view1.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onStop()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {

        mMonth = month + 1
        println(month)

        val calendar = GregorianCalendar(year, month, dayOfMonth)
        date = calendar.timeInMillis
        //        date = mDayFinal+"/"+mMonthFinal+"/"+mYearFinal;

        val dateLong = calendar.timeInMillis
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dateString = sdf.format(Date(dateLong))

        info_date.text = dateString
        check.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        return if (showMenu!!) {
            menuInflater.inflate(R.menu.menu_line_chart, menu)
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                val spanString = SpannableString(menu.getItem(i).title.toString())
                spanString.setSpan(ForegroundColorSpan(Color.BLACK), 0, spanString.length, 0) //fix the color to white
                item.title = spanString
            }
            true
        } else {
            false
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_line_chart -> {

                val intent = Intent(this@MachineInfo, LineChart::class.java)
                intent.putExtra("id", id)
                intent.putExtra("name", name)
                startActivity(intent)
            }
            R.id.share_info -> {

                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, "$name ha recaudado en total: $total_amount2")
                startActivity(Intent.createChooser(sendIntent, "Compartir"))
            }
            R.id.export_csv_incomes -> {

                val headers = arrayOf("ID Ingreso", "Maquina", "Nota", "Fecha", "Dinero Recoletado")
                val iterator2 = mIncomeList.iterator()

                val file = File(Environment.getExternalStorageDirectory(), "/Maquinas")
                val exportFile = File(file, "Ingresos - $name.csv")

                if (!file.exists()) {
                    file.mkdir()
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
                        val income = iterator2.next()
                        val id = income._id
                        val note = income.note

                        val dateLong = income.date
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        val dateString = sdf.format(Date(dateLong))


                        val total_amount = income.money
                        val formatter = DecimalFormat("$#,##0.000")
                        val formatted = formatter.format(total_amount)

                        writer.writeNext(arrayOf<String>(id.toString(), name, note, dateString, formatted))
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

