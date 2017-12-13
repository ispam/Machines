package tech.destinum.machines.ACTIVITIES;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.R;

public class LineChart extends AppCompatActivity {

    private com.github.mikephil.charting.charts.LineChart mLineChart;
    private DBHelpter mDBHelpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        mDBHelpter = new DBHelpter(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLineChart = findViewById(R.id.line_chart);

        List<Entry> entries = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            Cursor cursor = mDBHelpter.incomeCursor(bundle.getLong("id"));
            setTitle(bundle.getString("name", ""));
            while (cursor.moveToNext()) {
                double total = cursor.getDouble(cursor.getColumnIndex("money"));
                float id = cursor.getLong(cursor.getColumnIndex("_id"));
                float newTotal = (float) total;
                entries.add(new Entry(id, newTotal));
            }
            cursor.close();
        } else {
            finish();
        }

        LineDataSet set = new LineDataSet(entries, "Maquinas");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);
        data.setValueTextSize(16f);
        set.setCircleRadius(8f);
        set.setCircleHoleRadius(4f);
        set.setCircleColors(ColorTemplate.MATERIAL_COLORS);
        mLineChart.setData(data);
        mLineChart.invalidate();

    }
}
