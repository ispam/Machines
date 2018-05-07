package tech.destinum.machines.ACTIVITIES;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.R;
import tech.destinum.machines.data.local.POJO.Machine;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;
import tech.destinum.machines.data.local.ViewModel.MachineViewModel;

public class Graph extends AppCompatActivity {

    private BarChart mChart;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    IncomeViewModel incomeViewModel;

    @Inject
    MachineViewModel mMachineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChart = findViewById(R.id.graph);

    }

    @Override
    protected void onStop() {
        if (disposable != null && !disposable.isDisposed()){
            disposable.clear();
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        disposable.add(
                incomeViewModel.getCursor()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cursor -> {
                            List<BarEntry> entries = new ArrayList<>();

                            XAxis xAxis = mChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setDrawGridLines(false);
                            xAxis.setGranularityEnabled(true);
                            xAxis.setGranularity(1f);

                            List<Machine> xList = new ArrayList<>();

                            while (cursor.moveToNext()) {
                                double total = cursor.getDouble(0);
                                float id = cursor.getLong(1);
                                float newTotal = (float) total;

                                disposable.add(mMachineViewModel.getAllMachines()
                                        .takeLast(1)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(machines -> {
                                            xList.addAll(machines);
                                            xAxis.setValueFormatter((value, axis) -> {
//                                                switch (xList.get((int) value)){
//
//                                                }
                                                return xList.get((int)value).getName();
                                            });
                                        }));

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

                            YAxis leftAxis = mChart.getAxisLeft();
                            leftAxis.setLabelCount(8, false);
                            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                            leftAxis.setSpaceTop(10f);
                            leftAxis.setAxisMinimum(0f);

                            YAxis rightAxis = mChart.getAxisRight();
                            rightAxis.setDrawGridLines(false);
                            rightAxis.setLabelCount(8, false);
                            rightAxis.setSpaceTop(10f);
                            rightAxis.setAxisMinimum(0f);


                            mChart.getDescription().setEnabled(false);
                            mChart.setData(data);
                            mChart.invalidate();
                        }));
    }
}
