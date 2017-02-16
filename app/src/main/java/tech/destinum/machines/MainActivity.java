package tech.destinum.machines;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import static tech.destinum.machines.MachinesAdapter.PREFS_NAME;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFAB;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFAB = (FloatingActionButton) findViewById(R.id.fabAddMachine);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MachineCreation.class);
                startActivity(i);
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mFAB.show();
                        break;
                    case 1:
                        mFAB.hide();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView mRecyclerView;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            DBHelpter mDBHelper = new DBHelpter(getContext());

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvNew);
            MachinesAdapter mAdapter = new MachinesAdapter(getContext(), mDBHelper.getAllMachines());

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setHasFixedSize(true);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            mRecyclerView.addItemDecoration(itemDecoration);

            return rootView;
        }
    }

    public static class Fragment2 extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private TextView mTextView;
        private PieChart mPieChart;
        private DBHelpter mDBHelpter;

        public Fragment2() {
        }

        public static Fragment2 newInstance(int sectionNumber) {
            Fragment2 fragment = new Fragment2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mDBHelpter = new DBHelpter(getContext());

            View rootView = inflater.inflate(R.layout.fragment_2, container, false);
            mPieChart = (PieChart) rootView.findViewById(R.id.pieChart);

            ArrayList<PieEntry> yData = new ArrayList<>();
            for (int i = 0; i < mDBHelpter.yData().size(); i++){
                yData.add(new PieEntry(mDBHelpter.yData().get(i), i));
            }
            ArrayList<String> xData = new ArrayList<>();
            for (int i = 0; i < mDBHelpter.xData().size(); i++){
                xData.add(mDBHelpter.xData().get(i));
            }

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.rgb(112, 46, 90));
            colors.add(Color.rgb(46, 49, 112));
            colors.add(Color.rgb(46, 98, 112));
            colors.add(Color.rgb(46, 112, 76));
            colors.add(Color.rgb(111, 112, 46));
            colors.add(Color.rgb(112, 76, 46));
            colors.add(Color.rgb(112, 46, 46));

            PieDataSet pieDataSet = new PieDataSet(yData, "Maquinas");
            pieDataSet.setValueTextSize(25f);
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setSliceSpace(3f);
            pieDataSet.setHighlightEnabled(true);
            pieDataSet.setDrawValues(true);


            pieDataSet.setColors(colors);


            PieData pieData =  new PieData(pieDataSet);
            mPieChart.setHoleRadius(20);
            mPieChart.setTransparentCircleAlpha(0);
            mPieChart.setRotationEnabled(false);
            mPieChart.setData(pieData);
            mPieChart.getLegend().setEnabled(false);
            mPieChart.invalidate();


            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return Fragment2.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MAQUINAS";
                case 1:
                    return "GRÁFICA";
            }
            return null;
        }
    }
}
