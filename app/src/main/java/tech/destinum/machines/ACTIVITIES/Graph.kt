package tech.destinum.machines.ACTIVITIES

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.machines.R
import tech.destinum.machines.data.local.POJO.Machine
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel
import tech.destinum.machines.data.local.ViewModel.MachineViewModel

class Graph : AppCompatActivity() {

    private val disposable = CompositeDisposable()
    private lateinit var mChart: BarChart

    @Inject
    lateinit var incomeViewModel: IncomeViewModel

    @Inject
    lateinit var mMachineViewModel: MachineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        App.component.inject(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mChart = findViewById(R.id.graph)

    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onStop()
    }

    override fun onStart() {
        super.onStart()

        disposable.add(
                incomeViewModel.cursor
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { cursor ->
                            val entries = ArrayList<BarEntry>()

                            val xAxis = mChart.xAxis
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.setDrawGridLines(false)
                            xAxis.isGranularityEnabled = true
                            xAxis.granularity = 1f

                            val xList = ArrayList<Machine>()

                            while (cursor.moveToNext()) {
                                val total = cursor.getDouble(0)
                                val id = cursor.getLong(1).toFloat()
                                val newTotal = total.toFloat()

                                disposable.add(mMachineViewModel.allMachines
                                        .takeLast(1)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe { machines ->
                                            xList.addAll(machines)
                                            xAxis.setValueFormatter { value, axis ->
                                                //                                                switch (xList.get((int) value)){
                                                //
                                                //                                                }
                                                xList[value.toInt()].name
                                            }
                                        })

                                entries.add(BarEntry(id, newTotal))
                            }
                            cursor.close()


                            val set = BarDataSet(entries, "Maquinas")
                            val data = BarData(set)

                            mChart.isDragEnabled = true
                            mChart.setScaleEnabled(true)
                            mChart.setFitBars(true)
                            data.setValueTextSize(16f)
                            set.setColors(*ColorTemplate.COLORFUL_COLORS)

                            val leftAxis = mChart.axisLeft
                            leftAxis.setLabelCount(8, false)
                            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                            leftAxis.spaceTop = 10f
                            leftAxis.axisMinimum = 0f

                            val rightAxis = mChart.axisRight
                            rightAxis.setDrawGridLines(false)
                            rightAxis.setLabelCount(8, false)
                            rightAxis.spaceTop = 10f
                            rightAxis.axisMinimum = 0f


                            mChart.description.isEnabled = false
                            mChart.data = data
                            mChart.invalidate()
                        })
    }
}
