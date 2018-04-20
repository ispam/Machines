package tech.destinum.machines.ACTIVITIES;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.R;
import tech.destinum.machines.UTILS.DayAxisValueFormatter;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;

public class LineChart extends AppCompatActivity {

    private com.github.mikephil.charting.charts.LineChart mLineChart;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String name;
    private long id;
    private long reference_timestamp;


    @Inject
    IncomeViewModel incomeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLineChart = findViewById(R.id.line_chart);
        mLineChart.getDescription().setEnabled(false);


    }

    @Override
    protected void onStop() {
        if (disposable!= null && !disposable.isDisposed()){
            disposable.clear();
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            String name = bundle.getString("name");
            long id = bundle.getLong("id");
            this.id = id;
            this.name = name;
            setTitle(name);
        } else {
            finish();
        }

        disposable.add(
                incomeViewModel.getCursorByID(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cursor -> {

                            List<Entry> entries = new ArrayList<Entry>();

                            IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter();
                            while (cursor.moveToNext()) {
                                double total = cursor.getDouble(0);
                                float id = cursor.getLong(1);
                                String dateString = cursor.getString(2);
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                                Date date = dateFormat.parse(dateString);

                                long mili = date.getTime();
                                float newTotal = (float) total;
                                entries.add(new Entry(mili, newTotal));
                            }


                            XAxis xAxis = mLineChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setTypeface(Typeface.DEFAULT);
                            xAxis.setDrawGridLines(false);
                            xAxis.setGranularity(1f); // only intervals of 1 day
                            xAxis.setLabelCount(5);
                            xAxis.setValueFormatter(xAxisFormatter);
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


                            LineDataSet set = new LineDataSet(entries, name);
                            set.setAxisDependency(YAxis.AxisDependency.LEFT);

                            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(set);

                            LineData data = new LineData(dataSets);
                            data.setValueTextSize(16f);
                            set.setDrawFilled(true);
                            set.setFillColor(ColorTemplate.LIBERTY_COLORS[3]);
                            set.setCircleRadius(8f);
//                            set.setCircleHoleRadius(4f);
                            set.setCircleColors(ColorTemplate.MATERIAL_COLORS);

                            YAxis leftAxis = mLineChart.getAxisLeft();
                            leftAxis.setLabelCount(8, false);
                            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                            leftAxis.setSpaceTop(15f);
                            leftAxis.setAxisMinimum(0f);

                            YAxis rightAxis = mLineChart.getAxisRight();
                            rightAxis.setDrawGridLines(false);
                            rightAxis.setLabelCount(8, false);
                            rightAxis.setSpaceTop(15f);
                            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//                            Legend l = mLineChart.getLegend();
//                            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//                            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                            l.setDrawInside(false);
//                            l.setForm(Legend.LegendForm.SQUARE);
//                            l.setFormSize(9f);
//                            l.setTextSize(11f);
//                            l.setXEntrySpace(4f);


                            mLineChart.setData(data);
                            mLineChart.invalidate();

                            cursor.close();
                        }));
    }
}
