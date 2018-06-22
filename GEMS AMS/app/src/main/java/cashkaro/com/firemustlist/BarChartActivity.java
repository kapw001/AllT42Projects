package cashkaro.com.firemustlist;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BarChartActivity extends AppCompatActivity {

    protected BarChart mBarChart;
    private static final String TAG = "BarChartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        mBarChart = (BarChart) findViewById(R.id.barchart);
        loadBarchartData();

    }


    private void loadBarchartData() {

        float groupSpace = 0.4f;
        float barSpace = 0.1f; // x4 DataSet
        float barWidth = 0.3f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 6;
        int startYear = 0;
        int endYear = startYear + groupCount;

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 60f));

        BarDataSet set1 = new BarDataSet(entries, "BarDataSet");

        Collections.sort(entries, new EntryXComparator());


        List<BarEntry> entries1 = new ArrayList<>();
        entries1.add(new BarEntry(0f, 10f));
        entries1.add(new BarEntry(1f, 40f));
        entries1.add(new BarEntry(2f, 20f));
        entries1.add(new BarEntry(3f, 90f));
        entries1.add(new BarEntry(4f, 30f));
        entries1.add(new BarEntry(5f, 60f));

        Collections.sort(entries1, new EntryXComparator());
        BarDataSet set2 = new BarDataSet(entries1, "Visitors 1");

        ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();
//        iBarDataSets.add(set1);
//        iBarDataSets.add(set2);


        for (int i = 0; i < 6; i++) {
            List<BarEntry> entries2 = new ArrayList<>();
            int j = 0;
            while (j < 5) {
                entries2.add(new BarEntry(j, j * i + 10));
                j++;
            }

            Log.e(TAG, "loadBarchartData: " + entries2.toString());
            BarDataSet set3 = new BarDataSet(entries2, "Visitors 1");
            iBarDataSets.add(set3);
        }


//
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));

        set2.setColor(Color.GREEN);


        final ArrayList xAxisLabels = new ArrayList<>();
        xAxisLabels.add("JAN");
        xAxisLabels.add("FEB");
        xAxisLabels.add("MAR");
        xAxisLabels.add("APR");
        xAxisLabels.add("MAY");
        xAxisLabels.add("JUN");

//        mChart.setDescription("");    // Hide the description
//        mChart.getAxisLeft().setDrawLabels(false);
//        mChart.getAxisRight().setDrawLabels(false);
//        mChart.getXAxis().setDrawLabels(false);

        XAxis xAxis = mBarChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setCenterAxisLabels(true);

        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

        mBarChart.setDescription(null);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);


        BarData data = new BarData(iBarDataSets);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(0f);

        mBarChart.setPinchZoom(false);
        mBarChart.setDoubleTapToZoomEnabled(false);
        mBarChart.setData(data);
        mBarChart.setFitBars(true);
        mBarChart.animateXY(1000, 1000);


        mBarChart.getBarData().setBarWidth(barWidth);
        mBarChart.getXAxis().setAxisMinimum(0);
        mBarChart.getXAxis().setAxisMaximum(0 + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mBarChart.groupBars(0, groupSpace, barSpace);
        mBarChart.invalidate();

    }
}
