package tech.destinum.machines.ACTIVITIES;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.R;

public class Graph extends AppCompatActivity {

    private BarChart mChart;
    private DBHelpter mDBHelpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mDBHelpter = new DBHelpter(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChart = (BarChart) findViewById(R.id.graph);

        List<BarEntry> entries = new ArrayList<>();

        Cursor cursor = mDBHelpter.getAllMachinesIncome();
        while (cursor.moveToNext()) {
            double total = cursor.getDouble(cursor.getColumnIndex("total"));
            float id = cursor.getLong(cursor.getColumnIndex("machines_id"));
            float newTotal = (float) total;
            entries.add(new BarEntry(id, newTotal));
        }
        cursor.close();

        BarDataSet set = new BarDataSet(entries, "Maquinas");
        BarData data = new BarData(set);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setFitBars(true);
        data.setValueTextSize(16f);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        mChart.setData(data);
        mChart.invalidate();
    }


}
