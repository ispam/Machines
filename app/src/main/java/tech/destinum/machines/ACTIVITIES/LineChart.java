package tech.destinum.machines.ACTIVITIES;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import tech.destinum.machines.R;
import tech.destinum.machines.data.local.ViewModel.IncomeViewModel;

public class LineChart extends AppCompatActivity {

    private com.github.mikephil.charting.charts.LineChart mLineChart;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String name;
    private long id;


    @Inject
    IncomeViewModel incomeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        ((App) getApplication()).getComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLineChart = findViewById(R.id.line_chart);


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

                            while (cursor.moveToNext()) {
                                double total = cursor.getDouble(0);
                                float id = cursor.getLong(1);
                                float newTotal = (float) total;
                                entries.add(new Entry(id, newTotal));
                            }

                            LineDataSet set = new LineDataSet(entries, name);
                            set.setAxisDependency(YAxis.AxisDependency.LEFT);

                            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(set);

                            LineData data = new LineData(dataSets);
                            data.setValueTextSize(16f);
                            set.setDrawFilled(true);
                            set.setFillColor(ColorTemplate.LIBERTY_COLORS[3]);
                            set.setCircleRadius(8f);
                            set.setCircleHoleRadius(4f);
                            set.setCircleColors(ColorTemplate.MATERIAL_COLORS);
                            mLineChart.setData(data);
                            mLineChart.invalidate();

                            cursor.close();
                        }));
    }
}
