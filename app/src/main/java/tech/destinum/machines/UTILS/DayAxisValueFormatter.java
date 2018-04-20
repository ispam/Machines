package tech.destinum.machines.UTILS;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DayAxisValueFormatter implements IAxisValueFormatter {

    private DateFormat mFormat;

    public DayAxisValueFormatter() {
        mFormat = new SimpleDateFormat("dd/MM");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mFormat.format((long)value);
    }


}