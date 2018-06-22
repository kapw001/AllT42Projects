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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cashkaro.com.firemustlist.LoginActivity;
import cashkaro.com.firemustlist.R;
import cashkaro.com.firemustlist.SchoolListActivity;
import cashkaro.com.firemustlist.dashboard.adapter.GridViewRecyclerAdapter;
import cashkaro.com.firemustlist.dashboard.model.DataSet;
import cashkaro.com.firemustlist.dashboard.model.DataSetUpdate;
import cashkaro.com.firemustlist.dashboard.model.PassValuesTwoActivity;
import cashkaro.com.firemustlist.dashboard.model.SchoolList;
import cashkaro.com.firemustlist.dashboard.model.VisitorList;
import cashkaro.com.firemustlist.dashboard.request.WebRequest;
import cashkaro.com.firemustlist.database.DatabaseHandler;


public class DashboardActivityBackupNew extends AppCompatActivity
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
    private Map<Integer, Map<Integer, ArrayList<IBarDataSet>>> schoolCountByMonthMap;
    private Map<Integer, Map<Integer, ArrayList<IBarDataSet>>> schoolCountByMonthMapCopy;

    private List<SchoolList> schoolLists;
    private SharedPreferences sharedPreferences;

    private List<Integer> colors = cashkaro.com.firemustlist.dashboard.Utils.getColors();

    private List<VisitorList> visitorListsCopy;

    private List<SchoolList> refreshDataList;

    private Map<Integer, SchoolList> mapSchoolList;

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
        schoolCountByMonthMap = new HashMap<Integer, Map<Integer, ArrayList<IBarDataSet>>>();
        schoolCountByMonthMapCopy = new HashMap<Integer, Map<Integer, ArrayList<IBarDataSet>>>();
        visitorListsCopy = new ArrayList<>();
        sharedPreferences = getSharedPreferences("firemustlist", MODE_PRIVATE);
        schoolLists = new ArrayList<>();
        webRequest = WebRequest.getWebRequest(this);

        databaseHandler = new DatabaseHandler(this);

        refreshDataList = new ArrayList<>();
        mapSchoolList = new LinkedHashMap<>();

        schoolsselected = (TextView) findViewById(R.id.schoolsselected);
        startrange = (TextView) findViewById(R.id.startrange);
        endrange = (TextView) findViewById(R.id.endrange);
        totalvisitorcount = (TextView) findViewById(R.id.totalvisitorcount);
        maxvisitorindays = (TextView) findViewById(R.id.maxvisitorindays);
        maximunvisitorhours = (TextView) findViewById(R.id.maximunvisitorhours);

        startrange.setText(cashkaro.com.firemustlist.dashboard.Utils.getDaymonthYear(cashkaro.com.firemustlist.dashboard.Utils.minusDays(cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date()))));
        endrange.setText(cashkaro.com.firemustlist.dashboard.Utils.getDaymonthYear(cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date())));


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (sharedPreferences.getString("user_type", "").equalsIgnoreCase("School Administrator")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_fire_muster_list_draweronly);
        } else {
            navigationView.getMenu().clear();
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


        webRequest.getSchoolList(webRequest.getParms(), new WebRequest.CallBackWebResponse() {
            @Override
            public void showSchoolData(List<SchoolList> schoolLists2) {


                schoolLists.clear();
                schoolLists.addAll(schoolLists2);

                refreshDataList.clear();
                refreshDataList.addAll(schoolLists2);

                mapSchoolList.clear();

                for (int i = 0; i < schoolLists2.size(); i++) {
                    SchoolList schoolList = schoolLists2.get(i);
                    mapSchoolList.put(schoolList.getId(), schoolList);
                }

//
//                for (Map.Entry<Integer, SchoolList> entry : mapSchoolList.entrySet()) {
////                    System.out.println(entry.getKey() + "/" + entry.getValue());
//                    Log.e(TAG, "showSchoolData: " + entry.getKey() + "/" + entry.getValue().getName());
//                }


                List<Integer> universityIdlist = new ArrayList<Integer>();

                for (int i = 0; i < schoolLists.size(); i++) {
                    schoolLists.get(i).setColor(colors.get(i));
                    universityIdlist.add(schoolLists.get(i).getId());
                }

                schoolsselected.setText(universityIdlist.size() + " Schools selected");

                getVisitorData(cashkaro.com.firemustlist.dashboard.Utils.minusDays(cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date())), cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date()), universityIdlist);
//                gridViewRecyclerAdapter.update(schoolLists);

            }

            @Override
            public void showVisitorData(List<VisitorList> visitorLists) {

            }

            @Override
            public void fail(String failMsg) {

                Toast.makeText(DashboardActivityBackupNew.this, "" + failMsg, Toast.LENGTH_SHORT).show();

            }
        });
//        navigationView.getMenu().findItem(R.id.dashboard).setChecked(true);

    }

    private String startdate, enddate;

    public void getVisitorData(String startdate, String enddate, List<Integer> uList) {

        this.startdate = startdate;
        this.enddate = enddate;

        Log.e(TAG, "getVisitorData: " + webRequest.getParms(startdate, enddate, uList).toString());

        webRequest.getVisitorData(webRequest.getParms(startdate, enddate, uList), new WebRequest.CallBackWebResponse() {
            @Override
            public void showSchoolData(List<SchoolList> schoolLists) {


            }

            @Override
            public void showVisitorData(List<VisitorList> visitorLists) {

                visitorListsCopy.clear();
                visitorListsCopy.addAll(visitorLists);

                seperateDate(visitorLists);

                loadDataChart(visitorLists);
            }

            @Override
            public void fail(String failMsg) {

            }
        });
    }

    int colorCount = 0;

    private void loadDataChart(List<VisitorList> visitorLists) {

        schoolCountByMonthMap.clear();
        schoolCountByMonthMapCopy.clear();
        iBarDataSets.clear();
        iLineDataSets.clear();

        for (int i = 0; i < visitorLists.size(); i++) {
            VisitorList visitorList = visitorLists.get(i);
            if (visitorList.getDataSet().size() > 0) {
                List<DataSet> list = visitorList.getDataSet();
                Collections.sort(list, new CustomComparator());
                schoolCountByMonthMap.put(visitorList.getId(), loadData(visitorList.getId(), list));
                colorCount++;
            }
//            schoolCountByMonthMap.put(visitorList.getId(), loadData(visitorList.getId(), visitorList.getDataSet()));
        }
        colorCount = 0;

        schoolCountByMonthMapCopy.putAll(schoolCountByMonthMap);

        List<SchoolList> lists = new ArrayList<SchoolList>(mapSchoolList.values());

        gridViewRecyclerAdapter.update(lists);
        loadChartLine();
        loadChartBar();

    }


    private void loadChartLine() {

        if (schoolCountByMonthMap.size() > 0) {

            List<String> xAxisLabels = cashkaro.com.firemustlist.dashboard.Utils.getMonthsFromTwoDatesS(startdate, cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDate(enddate)));
//        List<String> xAxisLabels = cashkaro.com.firemustlist.dashboard.Utils.getMonthsFromTwoDatesS(cashkaro.com.firemustlist.dashboard.Utils.minusDays(cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date())), cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(new Date()));
//
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


            mLineChart.setPinchZoom(false);
            mLineChart.setDoubleTapToZoomEnabled(false);
            LineData data = new LineData(iLineDataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(0f);
            data.setValueFormatter(new LargeValueFormatter());

            mLineChart.setData(data);
            mLineChart.animateXY(1000, 1000);
            //mChart.invalidate();

            mLineChart.notifyDataSetChanged();

            // dont forget to refresh the drawing
            mLineChart.invalidate();

        } else {

            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();

        }


    }


    private void loadChartBar() {

        if (schoolCountByMonthMap.size() > 0) {
            float groupSpace = 0.4f;
            float barSpace = 0.1f; // x4 DataSet
            float barWidth = 0.2f; // x4 DataSet
            // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

            int groupCount = 0;
            int startYear = 0;
            int endYear = startYear + groupCount;

            groupCount = schoolCountByMonthMap.size();
            List<String> xAxisLabels = cashkaro.com.firemustlist.dashboard.Utils.getMonthsFromTwoDatesS(startdate, cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDate(enddate)));

            XAxis xAxis = mBarChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
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


            mBarChart.setPinchZoom(false);
            mBarChart.setDoubleTapToZoomEnabled(false);

            BarData data = new BarData(iBarDataSets);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(0f);

            mBarChart.setPinchZoom(false);
            mBarChart.setDoubleTapToZoomEnabled(false);
            mBarChart.setData(data);
            mBarChart.setFitBars(true);
            mBarChart.animateXY(1000, 1000);


            if (schoolCountByMonthMap.size() > 1) {
                mBarChart.getBarData().setBarWidth(barWidth);
                mBarChart.getXAxis().setAxisMinimum(0);
                mBarChart.getXAxis().setAxisMaximum(0 + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                mBarChart.groupBars(0, groupSpace, barSpace);
            }
            mLineChart.notifyDataSetChanged();
            mBarChart.invalidate();

        } else {

            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();

        }


    }

    ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();

    private Map<Integer, ArrayList<IBarDataSet>> loadData(int id, List<DataSet> dataSetList1) {
        List<DataSet> dataSetList = dataSetList1;


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
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;
        Iterator<Map.Entry<String, Integer>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<String, Integer> pair = it1.next();
            entries1.add(new BarEntry(i, pair.getValue()));
            entries.add(new Entry(i, pair.getValue()));
            i++;
        }

        BarDataSet set1 = new BarDataSet(entries1, "Visitors ");
        set1.setColor(colors.get(colorCount));
        mapSchoolList.get(id).setIs_selected(true);
        mapSchoolList.get(id).setColor(colors.get(colorCount));
//        schoolLists.get(colorCount).setIs_selected(true);
//        schoolLists.get(colorCount).setColor(colors.get(colorCount));
        iBarDataSets.add(set1);

        Collections.sort(entries, new EntryXComparator());
        LineDataSet set2 = new LineDataSet(entries, "Visitors 1");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setLineWidth(3);
        set2.setColor(colors.get(colorCount));
        set2.setCircleColor(colors.get(colorCount));
        iLineDataSets.add(set2);


        Map<Integer, ArrayList<IBarDataSet>> listMap = new HashMap<>();

        return listMap;

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
                setdateList.add(new DataSetUpdate(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDateWithTime(dataSet.getCheckIn())));
//                Log.e(TAG, "seperateDate: " + dataSet.getCheckIn() + "     " + cashkaro.com.firemustlist.dashboard.Utils.convertStringToDateWithTime(dataSet.getCheckIn()));
            }
        }


        totalvisitorcount.setText("" + listNewUpdate.size());


        List<String> weekDaysList = new ArrayList<>();
        List<String> hoursList = new ArrayList<>();

        for (int i = 0; i < setdateList.size(); i++) {
            DataSetUpdate dataSetUpdate = setdateList.get(i);
            weekDaysList.add(cashkaro.com.firemustlist.dashboard.Utils.getDayByFullName(cashkaro.com.firemustlist.dashboard.Utils.convertDateToString(dataSetUpdate.getDate())));
            hoursList.add(cashkaro.com.firemustlist.dashboard.Utils.getHoursU(dataSetUpdate.getDate()));

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

//            Log.e(TAG, "seperateDate: Hours  list size " + liInSide.get(i).size());
            countList.add(liInSide.get(i).size());
        }

        List<Integer> countListHours = new ArrayList<>();

        for (int i = 0; i < liInSideHours.size(); i++) {

//            Log.e(TAG, "seperateDate: particular list size liInSideHours  " + liInSideHours.get(i).size() + "   " + liInSideHours.get(i).get(0));
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
                passValuesTwoActivity.setListofSchools(schoolLists);
                passValuesTwoActivity.setSchoolSelectedList(schoolSelectedList);
//                }

                startActivityForResult(new Intent(DashboardActivityBackupNew.this, FilterActivity.class).putExtra("passvalues", passValuesTwoActivity), 2);
                overridePendingTransition(R.animator.enter, R.animator.exit);
//                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;

            case R.id.refresh:


                List<Integer> universityIdlist = new ArrayList<Integer>();//
                for (int i = 0; i < refreshDataList.size(); i++) {
//                Nl.get(i).setColor(colors.get(colorCount));
                    universityIdlist.add(refreshDataList.get(i).getId());
                }
                gridViewRecyclerAdapter.update(refreshDataList);

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
//            startrange.setText(passValuesTwoActivity.getStartDate());
//            endrange.setText(passValuesTwoActivity.getEnddate());
            String startDate = passValuesTwoActivity.getStartDate();
            String endDate = passValuesTwoActivity.getEnddate();

//            startrange.setText(cashkaro.com.firemustlist.dashboard.Utils.getMonthByName(cashkaro.com.firemustlist.dashboard.Utils.getStartDateOfMonth(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDate(startDate))) + "," + cashkaro.com.firemustlist.dashboard.Utils.getDaysByNumber(cashkaro.com.firemustlist.dashboard.Utils.getStartDateOfMonth(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDate(startDate))));
//            endrange.setText(cashkaro.com.firemustlist.dashboard.Utils.getMonthByName(cashkaro.com.firemustlist.dashboard.Utils.getEndDateOfMonth(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDate(endDate))) + "," + cashkaro.com.firemustlist.dashboard.Utils.getDaysByNumber(cashkaro.com.firemustlist.dashboard.Utils.getEndDateOfMonth(cashkaro.com.firemustlist.dashboard.Utils.convertStringToDate(endDate))));
            startrange.setText(cashkaro.com.firemustlist.dashboard.Utils.getDaymonthYear(startDate));
            endrange.setText(cashkaro.com.firemustlist.dashboard.Utils.getDaymonthYear(endDate));


            List<String> schoolList = passValuesTwoActivity.getSchoolListList();
            List<SchoolList> Nl = new ArrayList<>();

            for (String s : schoolList) {
                for (int i = 0; i < schoolLists.size(); i++) {
                    SchoolList schoolList1 = schoolLists.get(i);
                    if (schoolList1.getName().equalsIgnoreCase(s)) {
                        Nl.add(schoolList1);
                    }
                }
            }

            schoolSelectedList.clear();
            schoolSelectedList.addAll(Nl);

            refreshDataList.clear();
            refreshDataList.addAll(Nl);

            List<Integer> universityIdlist = new ArrayList<Integer>();//
            for (int i = 0; i < Nl.size(); i++) {
//                Nl.get(i).setColor(colors.get(colorCount));
                universityIdlist.add(schoolSelectedList.get(i).getId());
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
                if (schoolCountByMonthMap.size() > 0)
                    loadChartBar();

                break;

            case R.id.barlay:

                barlay.setBackgroundResource(R.drawable.rightselectedstate);
                barchartimg.setImageResource(R.drawable.ic_linechart_enabled);
                mLineChart.setVisibility(View.VISIBLE);
                if (schoolCountByMonthMap.size() > 0)
                    loadChartLine();
                break;

        }

    }

    @Override
    public void add(SchoolList itemName,int pos) {

//        try {
//
//            int id = itemName.getId();
//            int pos = 0;
//            for (int i = 0; i < removedList.size(); i++) {
//                if (id == removedList.get(i).getId()) {
//                    pos = i;
//                }
//            }
//            visitorListsCopy.add(removedList.get(pos));
//            removedList.remove(pos);
//
//            addRemove(id, true, visitorListsCopy);
//            Log.e(TAG, "add : " + id);
//
//        } catch (IndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }


    }


    private void addRemove(int id, boolean is_removed, List<VisitorList> visitorLists) {

//        if (visitorLists.size() > 1) {

        iBarDataSets.clear();
        iLineDataSets.clear();

        for (int i = 0; i < visitorLists.size(); i++) {
            VisitorList visitorList = visitorLists.get(i);
            if (visitorList.getDataSet().size() > 0) {
                List<DataSet> list = visitorList.getDataSet();
                Collections.sort(list, new CustomComparator());
                loadData(id, is_removed, visitorList.getId(), list);
                Log.e(TAG, "addRemove: avail Id " + visitorList.getId());
                colorCount++;
            }
        }
        colorCount = 0;
        loadChartBar();
        loadChartLine();

        gridViewRecyclerAdapter.update(schoolLists);
//        } else {
//            Toast.makeText(this, "Atleast one data ", Toast.LENGTH_SHORT).show();
//        }

    }


    private List<VisitorList> removedList = new ArrayList<>();

    @Override
    public void remove(SchoolList itemName,int pos) {

//        try {
//            int id = itemName.getId();
//
//
//            int pos = 0;
//            for (int i = 0; i < visitorListsCopy.size(); i++) {
//                if (id == visitorListsCopy.get(i).getId()) {
//                    pos = i;
//                }
//            }
//            removedList.add(visitorListsCopy.get(pos));
//            visitorListsCopy.remove(pos);
//
//            Log.e(TAG, "remove: " + id);
//            addRemove(id, false, visitorListsCopy);
//        } catch (IndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }

//        if (visitorListsCopy.size() > 1) {

//        } else {
//            Toast.makeText(this, "Atleast one data there ", Toast.LENGTH_SHORT).show();
//        }


    }


    private Map<Integer, ArrayList<IBarDataSet>> loadData(int addRemoveID, boolean is_removed, int id, List<DataSet> dataSetList1) {

//        List<SchoolList> schoolListsListNew = schoolLists;

        List<DataSet> dataSetList = dataSetList1;


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
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;
        Iterator<Map.Entry<String, Integer>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<String, Integer> pair = it1.next();
            entries1.add(new BarEntry(i, pair.getValue()));
            entries.add(new Entry(i, pair.getValue()));
            i++;
        }

        BarDataSet set1 = new BarDataSet(entries1, "Visitors ");
        set1.setColor(colors.get(colorCount));
        mapSchoolList.get(id).setIs_selected(is_removed);
        mapSchoolList.get(id).setColor(colors.get(colorCount));


//        if (id == schoolLists.get(colorCount).getId()) {
//            set1.setColor(colors.get(colorCount));
//            schoolLists.get(colorCount).setColor(colors.get(colorCount));
//        }

//        schoolLists.get(colorCount).setColor(colors.get(colorCount));
//        if (schoolLists.get(colorCount).getId() == addRemoveID) {
//            schoolLists.get(colorCount).setIs_selected(is_removed);
//        }
//        else {
//            schoolLists.get(colorCount).setColor(colors.get(colorCount));
//        }


        iBarDataSets.add(set1);

        Collections.sort(entries, new EntryXComparator());
        LineDataSet set2 = new LineDataSet(entries, "Visitors 1");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setLineWidth(3);
        set2.setColor(colors.get(colorCount));
        set2.setCircleColor(colors.get(colorCount));
        iLineDataSets.add(set2);


        Map<Integer, ArrayList<IBarDataSet>> listMap = new HashMap<>();

        return listMap;

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