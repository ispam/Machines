package tech.destinum.machines.Activities

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.destinum.machines.R
import tech.destinum.machines.UTILS.DayAxisValueFormatter
import tech.destinum.machines.Data.Local.ViewModel.IncomeViewModel

class LineChart : AppCompatActivity() {

    private val disposable = CompositeDisposable()
    private lateinit var mLineChart: com.github.mikephil.charting.charts.LineChart
    private var name: String = ""
    private var id: Long = 0


    @Inject
    lateinit var incomeViewModel: IncomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)

        App.component.inject(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mLineChart = findViewById(R.id.line_chart)
        mLineChart.description.isEnabled = false

    }

    override fun onStop() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onStop()
    }

    override fun onStart() {
        super.onStart()

        val bundle = intent.extras
        if (bundle != null) {
            val name = bundle.getString("name")
            val id = bundle.getLong("id")
            this.id = id
            this.name = name
            title = name
        } else {
            finish()
        }

        disposable.add(
                incomeViewModel.getCursorByID(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { cursor ->

                            val entries = ArrayList<Entry>()

                            val xAxisFormatter = DayAxisValueFormatter()
                            while (cursor.moveToNext()) {
                                val total = cursor.getDouble(0)
                                val id = cursor.getLong(1).toFloat()

                                val dateLong = cursor.getLong(2)

                                val newTotal = total.toFloat()
                                entries.add(Entry(dateLong.toFloat(), newTotal))
                            }
                            cursor.close()

                            val xAxis = mLineChart.xAxis
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.typeface = Typeface.DEFAULT
                            xAxis.setDrawGridLines(false)
                            xAxis.isGranularityEnabled = false
                            //                            xAxis.setGranularity(1f); // only intervals of 1 day
                            xAxis.valueFormatter = xAxisFormatter
                            //                            xAxis.setValueFormatter((value, axis) -> {
                            //                                DateFormat dateFormat = new SimpleDateFormat("dd/MM");
                            //                                Date date = null;
                            //                                try {
                            //                                    date = dateFormat.parse(Float.valueOf(value).toString());
                            //                                } catch (ParseException e) {
                            //                                    e.printStackTrace();
                            //                                }
                            //                                return new Date(new Long(date.getTime()).longValue()).toString();
                            ////                                return new Date(new Float(value).longValue()).toString();
                            //                            });


                            val set = LineDataSet(entries, name)
                            set.axisDependency = YAxis.AxisDependency.LEFT

                            val dataSets = ArrayList<ILineDataSet>()
                            dataSets.add(set)

                            val data = LineData(dataSets)
                            data.setValueTextSize(16f)
                            set.setDrawFilled(true)
                            set.fillColor = ColorTemplate.LIBERTY_COLORS[3]
                            set.circleRadius = 8f
                            //                            set.setCircleHoleRadius(4f);
                            set.setCircleColors(*ColorTemplate.MATERIAL_COLORS)

                            val leftAxis = mLineChart.axisLeft
                            leftAxis.setLabelCount(8, false)
                            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                            leftAxis.spaceTop = 10f
                            leftAxis.axisMinimum = 0f

                            val rightAxis = mLineChart.axisRight
                            rightAxis.setDrawGridLines(false)
                            rightAxis.setLabelCount(8, false)
                            rightAxis.spaceTop = 10f
                            rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

                            //                            Legend l = mLineChart.getLegend();
                            //                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                            //                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                            //                            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                            //                            l.setDrawInside(false);
                            //                            l.setForm(Legend.LegendForm.SQUARE);
                            //                            l.setFormSize(9f);
                            //                            l.setTextSize(11f);
                            //                            l.setXEntrySpace(4f);


                            mLineChart.data = data
                            mLineChart.invalidate()


                        })
    }
}
