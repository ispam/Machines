package tech.destinum.machines.UTILS

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class DayAxisValueFormatter : IAxisValueFormatter {

    private val mFormat: DateFormat

    init {
        mFormat = SimpleDateFormat("dd/MM")
    }

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        // "value" represents the position of the label on the axis (x or y)
        return mFormat.format(value.toLong())
    }


}