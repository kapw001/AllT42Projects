package cashkaro.com.firemustlist.testactivity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cashkaro.com.firemustlist.R;
import cashkaro.com.firemustlist.dashboard.Utils;
import cashkaro.com.firemustlist.dashboard.adapter.GridViewRecyclerAdapter;
import cashkaro.com.firemustlist.dashboard.model.DataSet;
import cashkaro.com.firemustlist.dashboard.model.ResultResponse;
import cashkaro.com.firemustlist.dashboard.model.SchoolList;
import cashkaro.com.firemustlist.dashboard.model.VisitorList;

public class TestActivity extends AppCompatActivity implements GridViewRecyclerAdapter.OnItemClickListener {
    private static final String TAG = "TestActivity";

    protected BarChart mBarChart;
    Map<Integer, List<DataSet>> mapDataSet = new HashMap<>();
    Map<Integer, BarDataSet> mapBarData = new HashMap<>();
    Map<Integer, BarDataSet> mapBarDataCopy = new HashMap<>();

    private RecyclerView schoolSelectedListView;
    private GridViewRecyclerAdapter gridViewRecyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<SchoolList> schoolList;

    Map<Integer, SchoolList> schoolListMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        mBarChart = (BarChart) findViewById(R.id.barchart);
        schoolList = new ArrayList<>();

        schoolListMap.put(1, new SchoolList("School1", 1, "School1", 1));
        schoolListMap.put(2, new SchoolList("School2", 2, "School2", 1));
        schoolListMap.put(3, new SchoolList("School3", 3, "School3", 1));
        schoolListMap.put(4, new SchoolList("School4", 4, "School4", 1));
        schoolListMap.put(5, new SchoolList("School5", 5, "School5", 1));
        schoolListMap.put(6, new SchoolList("School6", 6, "School6", 1));
//        schoolListMap.put(7, new SchoolList("School7", 7, "School7", 1));


        schoolSelectedListView = (RecyclerView) findViewById(R.id.schoollistselectedview);
        gridViewRecyclerAdapter = new GridViewRecyclerAdapter(this, schoolList);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        schoolSelectedListView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        schoolSelectedListView.setHasFixedSize(true);
        schoolSelectedListView.setAdapter(gridViewRecyclerAdapter);
//        Map<Integer, List<String>> map = new HashMap<>();
//        Map<Integer, List<String>> mapRemoved = new HashMap<>();
//
//        map.put(1, getList());
//        map.put(2, getList());
//        map.put(3, getList());
//        map.put(4, getList());
//        map.put(5, getList());


//        loadDataTest(1, map, mapRemoved);
//
//        loadDataTest(4, map, mapRemoved);

//        Log.e(TAG, "onCreate: Json Data " + loadJSONFromAsset());

//        Gson gson = new Gson();
//        ResultResponse userResponse = gson.fromJson(loadJSONFromAsset(), ResultResponse.class);
//
//        List<VisitorList> dataSets = userResponse.getResult().getVisitorList();
//        for (int i = 0; i < dataSets.size(); i++) {
//            List<DataSet> dataSets1 = dataSets.get(i).getDataSet();
//            mapDataSet.put(dataSets.get(i).getId(), dataSets1);
//        }
//
////        mapDataSet.remove(3);
////        mapDataSet.remove(4);
////        mapDataSet.remove(5);
////        mapDataSet.remove(6);
//
//        loadDataSchoolMap();
        reloadData();
    }

    private void reloadData() {
        mapDataSet.clear();
        mapBarDataCopy.clear();
        mapBarData.clear();
        Gson gson = new Gson();
        ResultResponse userResponse = gson.fromJson(loadJSONFromAsset(), ResultResponse.class);
        List<VisitorList> dataSets = userResponse.getResult().getVisitorList();
        for (int i = 0; i < dataSets.size(); i++) {
            List<DataSet> dataSets1 = dataSets.get(i).getDataSet();
            mapDataSet.put(dataSets.get(i).getId(), dataSets1);
        }
        loadDataSchoolMap();
    }

    private void loadDataSchoolMap() {
        iBarDataSets.clear();

        int selectionCount = 0;

        for (Map.Entry<Integer, List<DataSet>> entry : mapDataSet.entrySet()) {

            Log.e(TAG, "onCreate: id " + entry.getKey() + "   values  " + mapDataSet.get(entry.getKey()).size());

            loadData(selectionCount, entry.getKey(), mapDataSet.get(entry.getKey()));
            selectionCount++;
        }
        selectionCount = 0;

        mapBarDataCopy.putAll(mapBarData);

        int c = 0;
        for (Map.Entry<Integer, BarDataSet> entry : mapBarDataCopy.entrySet()) {
            if (c > 3) {
                mapBarData.remove(entry.getKey());
                schoolListMap.get(entry.getKey()).setIs_selected(false);
            }
            c++;
            Log.e(TAG, "loadDataSchoolMap:  c" + c);
        }

        loadChartBar();

        updateList();

        Log.e(TAG, "loadDataSchoolMap: Total size " + mapDataSet.size());
    }

    private void updateList() {
        List<SchoolList> list = new ArrayList<>(schoolListMap.values());
        gridViewRecyclerAdapter.update(list);
        gridViewRecyclerAdapter.notifyDataSetChanged();
    }

    ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();

    private void loadChartBar() {

        if (mapDataSet.size() > 1) {
            float groupSpace = 0.4f;
            float barSpace = 0.1f; // x4 DataSet
            float barWidth = 0.5f; // x4 DataSet
            // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

            int groupCount = 0;
            int startYear = 0;
            int endYear = startYear + groupCount;

            groupCount = mapDataSet.size();
            XAxis xAxis = mBarChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            mBarChart.setDescription(null);
            mBarChart.getLegend().setEnabled(false);
            mBarChart.getAxisRight().setEnabled(false);


            mBarChart.setPinchZoom(false);
            mBarChart.setDoubleTapToZoomEnabled(false);

            ArrayList<IBarDataSet> l = new ArrayList<IBarDataSet>(mapBarData.values());

//            for (Map.Entry<Integer, BarDataSet> data : mapBarData.entrySet()) {
//
//                l.add(data.getValue());
//            }
            BarData data = new BarData(l);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(0f);

            mBarChart.setPinchZoom(false);
            mBarChart.setDoubleTapToZoomEnabled(false);
            mBarChart.setData(data);
            mBarChart.setFitBars(true);
            mBarChart.animateXY(1000, 1000);


            if (mapBarData.size() > 1) {
                mBarChart.getBarData().setBarWidth(barWidth);
                mBarChart.getXAxis().setAxisMinimum(0);
                mBarChart.getXAxis().setAxisMaximum(0 + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                mBarChart.groupBars(0, groupSpace, barSpace);
            }
            mBarChart.notifyDataSetChanged();
            mBarChart.invalidate();

        } else {

            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();

        }


    }


    private void loadData(int selectionCount, int id, List<DataSet> dataSetList1) {

        List<DataSet> dataSetList = dataSetList1;

        Random random = new Random();
        int rcolor = random.nextInt(Utils.getColors().size() - 1);

        int colors = Utils.getColors().get(rcolor);

        List<String> mL = new ArrayList<>();
        for (int i = 0; i < dataSetList.size(); i++) {
            DataSet dataSet = dataSetList.get(i);
            mL.add(cashkaro.com.firemustlist.dashboard.Utils.getMonthByName(cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDateN(dataSet.getCheckIn()))));
        }


        Set<String> mLSet = new LinkedHashSet<>();
        mLSet.addAll(mL);


        Map<String, Integer> map = new LinkedHashMap<>();
        for (String s : mL) {
            map.put(s, Collections.frequency(mL, s));
        }

        List<BarEntry> entries1 = new ArrayList<>();

        int i = 0;
        Iterator<Map.Entry<String, Integer>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<String, Integer> pair = it1.next();
            entries1.add(new BarEntry(i, pair.getValue()));
            i++;
        }
        BarDataSet set1 = new BarDataSet(entries1, "Visitors ");
        set1.setColor(colors);

        schoolListMap.get(id).setIs_selected(true);

        schoolListMap.get(id).setColor(colors);
        mapBarData.put(id, set1);
//        iBarDataSets.add(set1);


    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("json/visitordata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private int getCount() {

        int count = 0;

        for (Map.Entry<Integer, SchoolList> entry : schoolListMap.entrySet()) {

            if (entry.getValue().is_selected() == true) {
                count++;
            }

        }

        return count;

    }

    @Override
    public void add(SchoolList itemName, int pos) {
//        if (itemName.is_selected()) {

        int count = getCount();

        Log.e(TAG, "add: " + count);

        if (count <= 3) {
            int id = itemName.getId();
            mapBarData.put(id, mapBarDataCopy.get(id));
            schoolListMap.get(id).setIs_selected(true);
            loadChartBar();
            updateList();
        } else {
            Toast.makeText(this, "Maximum 4 school list", Toast.LENGTH_SHORT).show();
        }

//        }
    }

    @Override
    public void remove(SchoolList itemName, int pos) {
        int count = getCount();
        Log.e(TAG, "remove: " + count);
        if (count > 2) {
            int id = itemName.getId();
            mapBarData.remove(id);
            schoolListMap.get(id).setIs_selected(false);
            loadChartBar();
            updateList();
        } else {
            Toast.makeText(this, "Atleast two data ", Toast.LENGTH_SHORT).show();
        }
    }

    public void onReload(View view) {
        reloadData();
    }

}
