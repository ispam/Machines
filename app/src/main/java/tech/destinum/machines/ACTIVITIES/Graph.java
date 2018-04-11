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
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.DB.DBHelpter;
import tech.destinum.machines.R;
import tech.destinum.machines.data.POJO.Income;
import tech.destinum.machines.data.POJO.Machine;
import tech.destinum.machines.data.ViewModel.IncomeViewModel;

public class Graph extends AppCompatActivity {

    private BarChart mChart;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    IncomeViewModel incomeViewModel;

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

                            while (cursor.moveToNext()) {
                                double total = cursor.getDouble(cursor.getColumnIndex("money"));
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
                        }));
    }
}
