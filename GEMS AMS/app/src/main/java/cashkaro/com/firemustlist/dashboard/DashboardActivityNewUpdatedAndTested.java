package cashkaro.com.firemustlist.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cashkaro.com.firemustlist.LoginActivity;
import cashkaro.com.firemustlist.R;
import cashkaro.com.firemustlist.SchoolListActivity;
import cashkaro.com.firemustlist.dashboard.adapter.GridViewRecyclerAdapter;
import cashkaro.com.firemustlist.dashboard.model.CModel;
import cashkaro.com.firemustlist.dashboard.model.DataSet;
import cashkaro.com.firemustlist.dashboard.model.DataSetUpdate;
import cashkaro.com.firemustlist.dashboard.model.PassValuesTwoActivity;
import cashkaro.com.firemustlist.dashboard.model.SchoolList;
import cashkaro.com.firemustlist.dashboard.model.VisitorList;
import cashkaro.com.firemustlist.dashboard.request.WebRequest;
import cashkaro.com.firemustlist.database.DatabaseHandler;


public class DashboardActivityNewUpdatedAndTested extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnChartValueSelectedListener, View.OnClickListener, GridViewRecyclerAdapter.OnItemClickListener, OnChartGestureListener {
    protected BarChart mBarChart;
    private LineChart mLineChart;

    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private static final String TAG = "MainActivity";
    private DatabaseHandler databaseHandler;

    private RelativeLayout linelay, barlay;
    private ImageView linechartimg, barchartimg;

    private RecyclerView schoolSelectedListView;

    private String[] color = {"#ff4000", "#ffff00", "#40ff00", "#00ff80", "#0080ff", "#ff00bf", "#bf00ff"};


    private GridViewRecyclerAdapter gridViewRecyclerAdapter;
    private List<SchoolList> schoolListList;

    private ImageView leftarrow, rightarrow;
    public int selectedPosition = 0;
    private LinearLayoutManager linearLayoutManager;

    private ImageView filter;

    private PassValuesTwoActivity passValuesTwoActivity = null;


    private TextView schoolsselected, startrange, endrange, totalvisitorcount;

    private WebRequest webRequest;

    private TextView maxvisitorindays, maximunvisitorhours;

    private SharedPreferences sharedPreferences;


    private Map<Integer, List<DataSet>> mapDataSet = new HashMap<>();
    private Map<Integer, BarDataSet> mapBarData = new HashMap<>();
    private Map<Integer, BarDataSet> mapBarDataCopy = new HashMap<>();
    private Map<Integer, LineDataSet> mapLineData = new HashMap<>();
    private Map<Integer, LineDataSet> mapLineDataCopy = new HashMap<>();
    private Map<Integer, SchoolList> schoolListMap = new HashMap<>();

    private Map<Integer, Map<String, Integer>> monthDataSet = new HashMap<>();

    private String startdate, enddate;

    private List<SchoolList> refreshDataList;

    private TextView error;
    private RelativeLayout wholelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the version of Android is Lollipop or higher
        if (Build.VERSION.SDK_INT >= 21) {

            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        }
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("firemustlist", MODE_PRIVATE);

//        webRequest = WebRequest.getWebRequest(this);
        webRequest = new WebRequest(this);
        refreshDataList = new ArrayList<>();
        databaseHandler = new DatabaseHandler(this);


        error = (TextView) findViewById(R.id.error);
        wholelayout = (RelativeLayout) findViewById(R.id.wholelayout);
        schoolsselected = (TextView) findViewById(R.id.schoolsselected);
        startrange = (TextView) findViewById(R.id.startrange);
        endrange = (TextView) findViewById(R.id.endrange);
        totalvisitorcount = (TextView) findViewById(R.id.totalvisitorcount);
        maxvisitorindays = (TextView) findViewById(R.id.maxvisitorindays);
        maximunvisitorhours = (TextView) findViewById(R.id.maximunvisitorhours);

        startrange.setText(Utils.getDaymonthYear(Utils.minusDays(Utils.convertDateToString(new Date()))));
        endrange.setText(Utils.getDaymonthYear(Utils.convertDateToString(new Date())));


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().clear();
        if (sharedPreferences.getString("user_type", "").equalsIgnoreCase("School Administrator")) {
            navigationView.inflateMenu(R.menu.activity_fire_muster_list_draweronly);
        } else {
            navigationView.inflateMenu(R.menu.activity_fire_muster_list_drawerwho);
//
        }

        schoolListList = new ArrayList<>();

        schoolSelectedListView = (RecyclerView) findViewById(R.id.schoollistselectedview);

        leftarrow = (ImageView) findViewById(R.id.leftarrow);
        rightarrow = (ImageView) findViewById(R.id.rightarrow);

        leftarrow.setOnClickListener(this);
        rightarrow.setOnClickListener(this);

        linelay = (RelativeLayout) findViewById(R.id.linelay);
        barlay = (RelativeLayout) findViewById(R.id.barlay);

        linechartimg = (ImageView) findViewById(R.id.linechartimg);
        barchartimg = (ImageView) findViewById(R.id.barchartimg);

        linelay.setOnClickListener(this);
        barlay.setOnClickListener(this);


        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");


        gridViewRecyclerAdapter = new GridViewRecyclerAdapter(this, schoolListList);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        schoolSelectedListView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        schoolSelectedListView.setHasFixedSize(true);
        schoolSelectedListView.setAdapter(gridViewRecyclerAdapter);


        mBarChart = (BarChart) findViewById(R.id.barchart);
        mLineChart = (LineChart) findViewById(R.id.linechart);

        changeImage(R.id.linelay);
        cashkaro.com.firemustlist.Utils.showProgress(DashboardActivityNewUpdatedAndTested.this, "Loading");

        webRequest.getSchoolList(webRequest.getParms(), new WebRequest.CallBackWebResponse() {
            @Override
            public void showSchoolData(List<SchoolList> schoolLists2) {

                if (schoolLists2.size() > 0) {

                    error.setVisibility(View.GONE);
                    wholelayout.setVisibility(View.VISIBLE);

                    refreshDataList.clear();
                    refreshDataList.addAll(schoolLists2);

                    for (int i = 0; i < schoolLists2.size(); i++) {
                        SchoolList schoolList = schoolLists2.get(i);
                        schoolListMap.put(schoolList.getId(), schoolList);
                    }
                    List<Integer> universityIdlist = new ArrayList<Integer>();
                    for (int i = 0; i < schoolLists2.size(); i++) {
                        universityIdlist.add(schoolLists2.get(i).getId());
                    }
                    schoolsselected.setText(universityIdlist.size() + " Schools selected");
                    cashkaro.com.firemustlist.Utils.hideProgress();
                    getVisitorData(Utils.minusDays(Utils.convertDateToString(new Date())), Utils.convertDateToString(new Date()), universityIdlist);
                } else {

                    Toast.makeText(DashboardActivityNewUpdatedAndTested.this, "There is no school available", Toast.LENGTH_SHORT).show();

                    error.setVisibility(View.VISIBLE);
                    wholelayout.setVisibility(View.GONE);
                    cashkaro.com.firemustlist.Utils.hideProgress();

                }
            }

            @Override
            public void showVisitorData(List<VisitorList> visitorLists) {

            }

            @Override
            public void fail(String failMsg) {
                error.setVisibility(View.VISIBLE);
                wholelayout.setVisibility(View.GONE);
                Toast.makeText(DashboardActivityNewUpdatedAndTested.this, "Please check network connection" + failMsg, Toast.LENGTH_SHORT).show();
                cashkaro.com.firemustlist.Utils.hideProgress();
            }
        });
        navigationView.setItemIconTintList(null);
//        navigationView.getMenu().findItem(R.id.dashboard).setChecked(true);

    }

    public void getVisitorData(String startdate, String enddate, List<Integer> uList) {

        this.startdate = startdate;
        this.enddate = enddate;
        cashkaro.com.firemustlist.Utils.showProgress(DashboardActivityNewUpdatedAndTested.this, "Loading");
        Log.e(TAG, "getVisitorData: " + webRequest.getParms(startdate, enddate, uList).toString());

        webRequest.getVisitorData(webRequest.getParms(startdate, enddate, uList), new WebRequest.CallBackWebResponse() {
            @Override
            public void showSchoolData(List<SchoolList> schoolLists) {
                cashkaro.com.firemustlist.Utils.hideProgress();

            }

            @Override
            public void showVisitorData(List<VisitorList> visitorLists) {
                cashkaro.com.firemustlist.Utils.hideProgress();

//                visitorLists.get(0).getDataSet().get(0).setCheckIn("2017-07-07 10:09");

                if (visitorLists.size() > 0) {

                    seperateDate(visitorLists);

                    reloadData(visitorLists);
                    error.setVisibility(View.GONE);
                    wholelayout.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.VISIBLE);
                    wholelayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void fail(String failMsg) {
                error.setVisibility(View.VISIBLE);
                wholelayout.setVisibility(View.GONE);
                cashkaro.com.firemustlist.Utils.hideProgress();
            }
        });
    }


    private void reloadData(List<VisitorList> visitorLists) {
        mapDataSet.clear();
        mapBarDataCopy.clear();
        mapBarData.clear();
        mapLineData.clear();
        mapLineDataCopy.clear();
        month.clear();

        for (Map.Entry<Integer, SchoolList> entry : schoolListMap.entrySet()) {
            entry.getValue().setIs_selected(false);
//            Log.e(TAG, "reloadData: " + entry.getValue().is_selected() + "   " + entry.getValue().getId());
        }

        List<VisitorList> dataSets = visitorLists;

        for (int i = 0; i < dataSets.size(); i++) {
            List<DataSet> dataSets1 = dataSets.get(i).getDataSet();
            mapDataSet.put(dataSets.get(i).getId(), dataSets1);

            CModel cModel = new CModel();
            cModel.setId(dataSets.get(i).getId());
            cModel.setList(dataSets1);
            cModel.setCount(dataSets1.size());

        }

        getMonth();
        loadDataSchoolMap();
    }

    TreeMap<Integer, Integer> trmap = new TreeMap<Integer, Integer>();

    private void loadDataSchoolMap() {

        int selectionCount = 0;

//        Collections.sort(cModelsList, new Comparator<CModel>() {
//            @Override
//            public int compare(CModel cModel, CModel t1) {
//                return cModel.getCount() > t1.getCount() ? 1 : 0;
//            }
//        });
//
//        for (int i = 0; i < cModelsList.size(); i++) {
//            CModel cModel = cModelsList.get(i);
//
//            loadData(selectionCount, cModel.getId(), cModel.getList());
//            selectionCount++;
//            Log.e(TAG, "reloadData: id " + cModel.getId() + "  count  " + cModel.getCount());
//        }


        for (Map.Entry<Integer, List<DataSet>> entry : mapDataSet.entrySet()) {

            loadData(selectionCount, entry.getKey(), mapDataSet.get(entry.getKey()));
            selectionCount++;
        }
        selectionCount = 0;

        mapBarDataCopy.putAll(mapBarData);
        mapLineDataCopy.putAll(mapLineData);


        int c = 0;
        for (Map.Entry<Integer, BarDataSet> entry : mapBarDataCopy.entrySet()) {
            if (c > 4) {
                mapBarData.remove(entry.getKey());
                mapLineData.remove(entry.getKey());
                schoolListMap.get(entry.getKey()).setIs_selected(false);
            }
            c++;
        }

        loadChartBar();
        loadChartLine();
        updateList();

    }

    private void updateList() {
        List<SchoolList> list = new ArrayList<>(schoolListMap.values());
        gridViewRecyclerAdapter.update(list);
        gridViewRecyclerAdapter.notifyDataSetChanged();
    }

    private void getMonth() {

        List<DataSet> l = new ArrayList<>();

        for (Map.Entry<Integer, List<DataSet>> entry : mapDataSet.entrySet()) {

            l.addAll(entry.getValue());
        }

        Collections.sort(l, new Comparator<DataSet>() {
            @Override
            public int compare(DataSet dataSet, DataSet t1) {
                return Utils.convertStringToDate(dataSet.getCheckIn()).compareTo(Utils.convertStringToDate(t1.getCheckIn()));
            }
        });

        List<String> mL = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            DataSet dataSet = l.get(i);
            mL.add(Utils.getMonthByName(Utils.convertDateToString(Utils.convertStringToDateN(dataSet.getCheckIn()))));
        }

        Set<String> m = new HashSet<>();
        m.addAll(mL);

        month.addAll(m);
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


    private void loadChartLine() {

        if (mapDataSet.size() > 0) {

            try {


                List<String> xAxisLabels = Utils.getMonthsFromTwoDatesS(startdate, Utils.convertDateToString(Utils.convertStringToDate(enddate)));
                XAxis xAxis = mLineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);
                xAxis.setCenterAxisLabels(false);
                xAxis.setAxisLineWidth(.5f);

                xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

                mLineChart.setDescription(null);
                mLineChart.getLegend().setEnabled(false);
                mLineChart.getAxisRight().setEnabled(false);
                mLineChart.setVisibleXRangeMaximum(4);
                mLineChart.moveViewToX(0);


                mLineChart.setPinchZoom(false);
                mLineChart.setDoubleTapToZoomEnabled(false);
                ArrayList<ILineDataSet> l = new ArrayList<ILineDataSet>(mapLineData.values());

                LineData data = new LineData(l);
                data.setValueTextColor(Color.BLACK);
                data.setValueTextSize(5f);
                data.setValueFormatter(new LargeValueFormatter());

                mLineChart.setData(data);
                mLineChart.animateXY(1000, 1000);
                //mChart.invalidate();

                mLineChart.notifyDataSetChanged();

                // dont forget to refresh the drawing
                mLineChart.invalidate();
            } catch (NullPointerException e) {
                e.printStackTrace();

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        } else {

//            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();

        }


    }


    private void loadChartBar() {

        if (mapDataSet.size() > 0) {
            //data
            float groupSpace = 0.04f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.225f; // x2 dataset
            // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

            int groupCount = 0;
            int startYear = 0;
            int endYear = startYear + groupCount;

            groupCount = mapDataSet.size();
            List<String> xAxisLabels = Utils.getMonthsFromTwoDatesS(startdate, Utils.convertDateToString(Utils.convertStringToDate(enddate)));

            for (int i = 0; i < xAxisLabels.size(); i++) {
                Log.e(TAG, "loadChartBar: month " + xAxisLabels.get(i));
            }

            XAxis xAxis = mBarChart.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(true);
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
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
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(5f);

//            mBarChart.setPinchZoom(false);
//            mBarChart.setDoubleTapToZoomEnabled(false);
            mBarChart.setData(data);
            mBarChart.setFitBars(false);
            mBarChart.setVisibleXRangeMaximum(4);
            mBarChart.moveViewToX(0);
            mBarChart.animateXY(1000, 1000);


            if (mapBarData.size() > 1) {
                mBarChart.getBarData().setBarWidth(barWidth);
                mBarChart.getXAxis().setAxisMinValue(startYear);
                mBarChart.groupBars(startYear, groupSpace, barSpace);
                mBarChart.getXAxis().setCenterAxisLabels(true);
                mBarChart.getXAxis().setLabelCount(12);
                mBarChart.setDragEnabled(true);

//                mBarChart.getBarData().setBarWidth(barWidth);
//                mBarChart.getXAxis().setAxisMinimum(0);
//                mBarChart.setDragEnabled(true);
////                mBarChart.getXAxis().setAxisMaxValue(12f);
//                mBarChart.getXAxis().setAxisMaximum(0 + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
//                mBarChart.getXAxis().setLabelCount(12);
//                mBarChart.groupBars(0, groupSpace, barSpace);

            }
            mBarChart.notifyDataSetChanged();
            mBarChart.invalidate();

        } else {

//            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();

        }


    }

    private List<String> month = new ArrayList<>();

    private void loadData(int selectionCount, int id, List<DataSet> dataSetList1) {

        List<DataSet> dataSetList = dataSetList1;

        Collections.sort(dataSetList, new Comparator<DataSet>() {
            @Override
            public int compare(DataSet dataSet, DataSet t1) {
                return Utils.convertStringToDate(dataSet.getCheckIn()).compareTo(Utils.convertStringToDate(t1.getCheckIn()));
            }
        });

        int colors = Utils.getColors().get(selectionCount);

        List<String> mL = new ArrayList<>();
        for (int i = 0; i < dataSetList.size(); i++) {
            DataSet dataSet = dataSetList.get(i);

            mL.add(Utils.getMonthByName(Utils.convertDateToString(Utils.convertStringToDateN(dataSet.getCheckIn()))));
        }

        Set<String> mLSet = new LinkedHashSet<>();
        mLSet.addAll(mL);
        List<String> xAxis = Utils.getMonthsFromTwoDatesS(startdate, Utils.convertDateToString(Utils.convertStringToDate(enddate)));

        Map<String, Integer> map = new LinkedHashMap<>();

        for (int i = 0; i < xAxis.size(); i++) {
            map.put(xAxis.get(i), 0);
        }
        for (String s : mL) {
            map.put(s, Collections.frequency(mL, s));

        }


        List<BarEntry> entries1 = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();

        int i = 0;
        Iterator<Map.Entry<String, Integer>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<String, Integer> pair = it1.next();
            entries1.add(new BarEntry(i, pair.getValue()));
            entries.add(new Entry(i, pair.getValue()));
            i++;
        }

//

        BarDataSet set1 = new BarDataSet(entries1, "Visitors ");
        set1.setColor(colors);

        Collections.sort(entries, new EntryXComparator());
        LineDataSet set2 = new LineDataSet(entries, "Visitors 1");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setLineWidth(3);
        set2.setColor(colors);
        set2.setCircleColor(colors);

        schoolListMap.get(id).setIs_selected(true);

        schoolListMap.get(id).setColor(colors);
        mapBarData.put(id, set1);
        mapLineData.put(id, set2);
//        }
    }

    private void seperateDate(List<VisitorList> visitorLists) {
        List<VisitorList> vList = visitorLists;

        List<DataSet> listNewUpdate = new ArrayList<>();
        List<DataSetUpdate> setdateList = new ArrayList<>();

        for (int i = 0; i < vList.size(); i++) {
            List<DataSet> dataSetList = vList.get(i).getDataSet();
            for (int j = 0; j < dataSetList.size(); j++) {
                DataSet dataSet = dataSetList.get(j);
                listNewUpdate.add(dataSet);
                setdateList.add(new DataSetUpdate(Utils.convertStringToDateWithTime(dataSet.getCheckIn())));
//                Log.e(TAG, "seperateDate: " + dataSet.getCheckIn() + "     " + cashkaro.com.firemustlist.dashboard.Utils.convertStringToDateWithTime(dataSet.getCheckIn()));
            }
        }


        totalvisitorcount.setText("" + listNewUpdate.size());


        List<String> weekDaysList = new ArrayList<>();
        List<String> hoursList = new ArrayList<>();

        for (int i = 0; i < setdateList.size(); i++) {
            DataSetUpdate dataSetUpdate = setdateList.get(i);
            weekDaysList.add(Utils.getDayByFullName(Utils.convertDateToString(dataSetUpdate.getDate())));
            hoursList.add(Utils.getHoursU(dataSetUpdate.getDate()));

//            Log.e(TAG, "seperateDate:  hours  " + dataSetUpdate.getDate() + "      " + cashkaro.com.firemustlist.dashboard.Utils.getHoursU(dataSetUpdate.getDate()).toString());

        }

        Set<String> dataSets = new HashSet<>();
        dataSets.addAll(weekDaysList);
        List<List<String>> liInSide = new ArrayList<>();

        for (String dataSet : dataSets) {

            List<String> oneByOne = new ArrayList<>();
            for (int i = 0; i < weekDaysList.size(); i++) {
                String dataSet1 = weekDaysList.get(i);
                if (dataSet.equalsIgnoreCase(dataSet1)) {
                    oneByOne.add(dataSet1);
                }
            }

            liInSide.add(oneByOne);
        }

        Set<String> hoursSet = new HashSet<>();
        hoursSet.addAll(hoursList);

        List<List<String>> liInSideHours = new ArrayList<>();

        for (String dataSet : hoursSet) {
            List<String> oneByOne = new ArrayList<>();
            for (int i = 0; i < hoursList.size(); i++) {
                String dataSet1 = hoursList.get(i);
                if (dataSet.equalsIgnoreCase(dataSet1)) {
                    oneByOne.add(dataSet1);
                }
            }
            liInSideHours.add(oneByOne);
        }


        List<Integer> countList = new ArrayList<>();

        for (int i = 0; i < liInSide.size(); i++) {
            countList.add(liInSide.get(i).size());
        }

        List<Integer> countListHours = new ArrayList<>();

        for (int i = 0; i < liInSideHours.size(); i++) {
            countListHours.add(liInSideHours.get(i).size());
        }


        int maxPos = getMaxValuesPosition(countList);

        if (liInSide.size() > 0) {
            String maxvisitorindaystxt = liInSide.get(maxPos).get(0);

//        Log.e(TAG, "seperateDate: " + maxvisitorindaystxt);
            maxvisitorindays.setText(maxvisitorindaystxt);
        }

//}

        int maxPosHours = getMaxValuesPosition(countListHours);
        if (liInSideHours.size() > 0) {
            String maxvisitorHours = liInSideHours.get(maxPosHours).get(0);

//        Log.e(TAG, "seperateDate: " + maxvisitorHours);

            maximunvisitorhours.setText(maxvisitorHours);
        }


    }


    private static int getMaxValuesPosition(List<Integer> countList) {
        List<Integer> list = countList;
        int limit = list.size();
        int max = Integer.MIN_VALUE;
        int maxPos = -1;
        for (int i = 0; i < limit; i++) {
            int value = list.get(i);
            if (value > max) {
                max = value;
                maxPos = i;
            }
        }

        return maxPos;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }


    private List<SchoolList> schoolSelectedList = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:

//                if (schoolCountByMonthMap.size() > 0) {
                passValuesTwoActivity = new PassValuesTwoActivity();
                passValuesTwoActivity.setStartDate(startdate);
                passValuesTwoActivity.setEnddate(enddate);
                List<SchoolList> list = new ArrayList<>(schoolListMap.values());
                passValuesTwoActivity.setListofSchools(list);
                passValuesTwoActivity.setSchoolSelectedList(schoolSelectedList);
//                }

                startActivityForResult(new Intent(DashboardActivityNewUpdatedAndTested.this, FilterActivity.class).putExtra("passvalues", passValuesTwoActivity), 2);
                overridePendingTransition(R.animator.enter, R.animator.exit);
//                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;

            case R.id.refresh:


                List<Integer> universityIdlist = new ArrayList<Integer>();//
                for (int i = 0; i < refreshDataList.size(); i++) {
//                Nl.get(i).setColor(colors.get(colorCount));
                    universityIdlist.add(refreshDataList.get(i).getId());
                }
//                gridViewRecyclerAdapter.update(refreshDataList);

                getVisitorData(startdate, enddate, universityIdlist);


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2 && requestCode == 2) {

            passValuesTwoActivity = (PassValuesTwoActivity) data.getSerializableExtra("passvalues");
            schoolsselected.setText(passValuesTwoActivity.getSchoolselectedcount());
            String startDate = passValuesTwoActivity.getStartDate();
            String endDate = passValuesTwoActivity.getEnddate();
            startrange.setText(Utils.getDaymonthYear(startDate));
            endrange.setText(Utils.getDaymonthYear(endDate));

            List<String> schoolList = passValuesTwoActivity.getSchoolListList();
            for (int i = 0; i < schoolList.size(); i++) {
                Log.e(TAG, "onActivityResult: " + schoolList.get(i));
            }
            List<SchoolList> Nl = new ArrayList<>();

            for (String s : schoolList) {

                for (Map.Entry<Integer, SchoolList> entry : schoolListMap.entrySet()) {

                    if (entry.getValue().getName().equalsIgnoreCase(s)) {
                        Nl.add(entry.getValue());
                    }
                }
            }
//
            schoolSelectedList.clear();
            schoolSelectedList.addAll(Nl);
            refreshDataList.clear();
            refreshDataList.addAll(Nl);

            List<Integer> universityIdlist = new ArrayList<Integer>();//
            for (int i = 0; i < Nl.size(); i++) {
                universityIdlist.add(Nl.get(i).getId());
            }
            gridViewRecyclerAdapter.update(Nl);

            getVisitorData(startDate, endDate, universityIdlist);

        } else {
            Log.e(TAG, "onActivityResult: There is no data come ");
        }


    }


    private void changeschoolSelectedListViewPosition(int id) {

        int Lpos = linearLayoutManager.findLastCompletelyVisibleItemPosition();

        int Fpos = linearLayoutManager.findFirstVisibleItemPosition();

        if (schoolListList.size() > 0) {
//            if (pos >= 0 && pos <= schoolListList.size()) {

            switch (id) {

                case R.id.leftarrow:
                    if (Fpos >= 1) {
                        Fpos--;
                        schoolSelectedListView.smoothScrollToPosition(Fpos);
                        Log.e(TAG, "changeschoolSelectedListViewPosition: left " + Fpos);

                    }
                    return;

                case R.id.rightarrow:
                    Lpos++;
                    schoolSelectedListView.smoothScrollToPosition(Lpos);
                    Log.e(TAG, "changeschoolSelectedListViewPosition:  right  " + Lpos);
                    return;

            }

//            }
        }


    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }


    protected RectF mOnValueSelectedRectF = new RectF();


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }


    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.linelay:
                changeImage(R.id.linelay);

                break;

            case R.id.barlay:
                changeImage(R.id.barlay);
                break;

            case R.id.leftarrow:
                changeschoolSelectedListViewPosition(R.id.leftarrow);
                break;
            case R.id.rightarrow:
                changeschoolSelectedListViewPosition(R.id.rightarrow);
                break;

        }
    }


    private void changeImage(int id) {

        linelay.setBackgroundResource(R.drawable.unselectedstate);
        barlay.setBackgroundResource(R.drawable.rightunselectedstate);

        linechartimg.setImageResource(R.drawable.ic_barchart_disabled);
        barchartimg.setImageResource(R.drawable.ic_linechart_disabled);

        mBarChart.setVisibility(View.GONE);
        mLineChart.setVisibility(View.GONE);

        switch (id) {

            case R.id.linelay:
                linelay.setBackgroundResource(R.drawable.selectedstate);
                linechartimg.setImageResource(R.drawable.ic_barchart_enabled);
                mBarChart.setVisibility(View.VISIBLE);

                loadChartBar();

                break;

            case R.id.barlay:

                barlay.setBackgroundResource(R.drawable.rightselectedstate);
                barchartimg.setImageResource(R.drawable.ic_linechart_enabled);
                mLineChart.setVisibility(View.VISIBLE);
//                if (schoolCountByMonthMap.size() > 0)
                loadChartLine();
                break;

        }

    }

    @Override
    public void add(SchoolList itemName,int pos) {
        int count = getCount();

        Log.e(TAG, "add: " + count);

        if (count <= 4) {
            int id = itemName.getId();

//            Log.e(TAG, "add: mapdata set  " + mapDataSet.get(id));

            if (mapDataSet.get(id) != null) {
                mapBarData.put(id, mapBarDataCopy.get(id));
                mapLineData.put(id, mapLineDataCopy.get(id));
                schoolListMap.get(id).setIs_selected(true);
                loadChartBar();
                loadChartLine();
                updateList();
            }
        } else {
            Toast.makeText(this, "Cannot select more than 5 schools", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void remove(SchoolList itemName,int pos) {
        int count = getCount();
        Log.e(TAG, "remove: " + count);
        if (count > 2) {
            int id = itemName.getId();
//            Log.e(TAG, "remove: mapdata set  " + mapDataSet.get(id));
            if (mapDataSet.get(id) != null) {

                mapBarData.remove(id);
                mapLineData.remove(id);
                schoolListMap.get(id).setIs_selected(false);
                loadChartBar();
                loadChartLine();
                updateList();
            }
        } else {
            Toast.makeText(this, "Minimum school reached", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            // Handle the camera action
        } else if (id == R.id.firemusterList) {

            startActivity(new Intent(this, SchoolListActivity.class));
            finish();

        } else if (id == R.id.logout) {
            clear();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void clear() {

//        databaseHandler.deleteAllSchoolListRecord();
//        databaseHandler.deleteAllRecord();
        ; // here you get your prefrences by either of two methods
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        finish();

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mLineChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }
}


//  List<String> mList = cashkaro.com.firemustlist.dashboard.Utils.getMonthsFromTwoDatesS(cashkaro.com.firemustlist.dashboard.Utils.minusDays(cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date())), cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date()));