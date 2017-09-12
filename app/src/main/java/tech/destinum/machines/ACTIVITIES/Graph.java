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


//        final List<Document> list = mDBHelper.getAllDocuments();
//        String [] symbols = new String[list.size()];
//
//        for (int i = 0; i< list.size(); i++){
//            symbols[i] = list.get(i).getSymbol();
//        }

//        entries.add(new BarEntry(0f, 450.000f));
//        entries.add(new BarEntry(1f, 225.000f));
//        entries.add(new BarEntry(2f, 375.456f));
//        entries.add(new BarEntry(3f, 125.050f));
//        // gap of 2f
//        entries.add(new BarEntry(5f, 654.070f));
//        entries.add(new BarEntry(6f, 485.060f));

        BarDataSet set = new BarDataSet(entries, "Maquinas");
        BarData data = new BarData(set);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setFitBars(true);
        data.setValueTextSize(16f);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        mChart.invalidate();
        mChart.setData(data);
    }


}
