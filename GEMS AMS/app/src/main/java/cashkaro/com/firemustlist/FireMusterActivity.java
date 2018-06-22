package cashkaro.com.firemustlist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cashkaro.com.firemustlist.App.App;
import cashkaro.com.firemustlist.adapter.GridViewRecyclerAdapter;
import cashkaro.com.firemustlist.api.LoginApi;
import cashkaro.com.firemustlist.dashboard.DashboardActivity;
import cashkaro.com.firemustlist.database.DatabaseHandler;
import cashkaro.com.firemustlist.model.PersonData;
import cashkaro.com.firemustlist.model.ResultResponse;
import cashkaro.com.firemustlist.model.TabMenu;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FireMusterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PassListInterface, GridViewRecyclerAdapter.OnItemClickListener {


    private LinearLayout alllayout, visitorlayout, stafflayout, studentlayout;
    private TextView all, student, visitor, staff;
    private TextView allcount, studentcount, visitorcount, staffcount;
    private TextView reset, save, incedentname, timing, schoolname;
    private ImageView refresh;
    private EditText search;

    private View vilast;
    private static final String TAG = "FireMusterActivity";
    private List<PersonData> personDataList;
    private List<PersonData> defaultList;

    private Retrofit retrofit = App.getApp().getRetrofit();

    private BaseFragment fragment = null;

    private LinearLayout ascending, descending, selectall;
    private ImageView ascendingimg, descendingimg, selectallimg;
    private boolean isDesending = true;
    private boolean isAscending = true;
    private boolean isSelectAll = true;
    private SharedPreferences sharedPreferences;

    private ResultResponse resultResponse;

    private RecyclerView tabrecyclerView;
    private GridViewRecyclerAdapter gridViewRecyclerAdapter;

    private List<TabMenu> listgrid;

    private TextView closeinstance;

    private List<PersonData> serverUpdateList;

    private DatabaseHandler databaseHandler;
    private int id;
    private String school_name;

    private boolean isActivityFirstTimeRunning = true;

    private Disposable networkDisposable;

    private RelativeLayout offlinedatalayout;

    private CompositeDisposable mCompositeDisposable;

    private boolean is_FireMusterDrillStarted = true;

    private List<PersonData> listSaved;

    private TextView instancenamedate;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fire_muster_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        offlinedatalayout = (RelativeLayout) findViewById(R.id.offlinedatalayout);
        listSaved = new ArrayList<>();
        serverUpdateList = new ArrayList<>();
        mCompositeDisposable = new CompositeDisposable();

        timing = (TextView) findViewById(R.id.timing);
        schoolname = (TextView) findViewById(R.id.schoolname);

        instancenamedate = (TextView) findViewById(R.id.instancenamedate);

        databaseHandler = new DatabaseHandler(this);

        closeinstance = (TextView) findViewById(R.id.closeinstance);

        sharedPreferences = getSharedPreferences("firemustlist", MODE_PRIVATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("who_is", false);
        editor.commit();

        if (sharedPreferences.getString("user_type", "").equalsIgnoreCase("School Administrator")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_fire_muster_list_draweronly);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_fire_muster_list_drawer);

        }


//        getSupportActionBar().hide();

        personDataList = new ArrayList<>();
        defaultList = new ArrayList<>();


        listgrid = new ArrayList<>();

        listgrid.add(new TabMenu("All", "0"));
        listgrid.add(new TabMenu("Safe", "0"));
        listgrid.add(new TabMenu("Un Safe", "0"));
        listgrid.add(new TabMenu("Visitor", "0"));
        listgrid.add(new TabMenu("Staff", "0"));
        listgrid.add(new TabMenu("Student", "0"));


        gridViewRecyclerAdapter = new GridViewRecyclerAdapter(this, listgrid);

        tabrecyclerView = (RecyclerView) findViewById(R.id.tabrecyclerview);
// set a GridLayoutManager with default vertical orientation and 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        gridRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        tabrecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // set LayoutManager to RecyclerView
        tabrecyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
//        tabrecyclerView.addItemDecoration(new EqualSpaceItemDecoration(5));
//        tabrecyclerView.addItemDecoration(new SimpleItemDecorator(5));
        tabrecyclerView.setHasFixedSize(true);
        tabrecyclerView.setAdapter(gridViewRecyclerAdapter);

        id = getIntent().getIntExtra("school_id", 0);
        school_name = getIntent().getStringExtra("school_name");
//
        schoolname.setText(school_name);

        if (Utils.isOnline(getApplicationContext())) {

//            loadFireMustList(id);
            if (sharedPreferences.getBoolean(school_name, false)) {
                updateCallReturnValue();
//                Toast.makeText(this, "Updated called", Toast.LENGTH_SHORT).show();
            } else {
                loadFireMustList(id);
            }
            offlinedatalayout.setVisibility(View.GONE);

        } else {

            List<PersonData> listdataBase = databaseHandler.getAllPersonData(id);

            personDataList.clear();
            personDataList.addAll(listdataBase);
            defaultList.clear();
            defaultList.addAll(listdataBase);

            showTotalCount(listdataBase);

            if (fragment != null) {
                fragment.loadData(listdataBase);
            }

            timing.setText(Utils.getTimeAMPM());

//            Toast.makeText(this, "Offline data is synching", Toast.LENGTH_SHORT).show();

            offlinedatalayout.setVisibility(View.VISIBLE);

        }

//        changeLayoutBackgroundColor(R.id.alllayout);
        addFragment("all");
        navigationView.setItemIconTintList(null);
//        navigationView.getMenu().findItem(R.id.firemusterList).setChecked(true);

        refresh = (ImageView) findViewById(R.id.refresh);
        closeinstance = (TextView) findViewById(R.id.closeinstance);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isOnline(getApplicationContext())) {

                    updateCallReturnValue();
                } else {
//                    Toast.makeText(FireMusterActivity.this, "Offline data is synching", Toast.LENGTH_SHORT).show();

                    offlinedatalayout.setVisibility(View.VISIBLE);
                }


//                loadFireMustList(id);
            }
        });

        closeinstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        networkDisposable = ReactiveNetwork.observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Connectivity>() {
                    @Override
                    public void accept(final Connectivity connectivity) {
                        // do something with connectivity
                        // you can call connectivity.getState();
                        // connectivity.getType(); or connectivity.toString();

                        if (Utils.isOnline(getApplicationContext())) {
//                            offlinedatalayout.setVisibility(View.GONE);
//                            Toast.makeText(FireMusterActivity.this, "Offline data is synching", Toast.LENGTH_SHORT).show();
                        } else {
                            offlinedatalayout.setVisibility(View.VISIBLE);
                        }

                        if (!isActivityFirstTimeRunning && Utils.isOnline(getApplicationContext())) {
//                            Toast.makeText(FireMusterActivity.this, "Net work avaiable", Toast.LENGTH_SHORT).show();

//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
                            updateList();
//                                }
//                            }, 1000);


                        }
                        isActivityFirstTimeRunning = false;
//                        Toast.makeText(FireMusterActivity.this, "" + connectivity.getType(), Toast.LENGTH_SHORT).show();

                    }
                });


    }


    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCallReturnValue();
                Log.e(TAG, "run: called Timer ");
            }
        }, 60 * 1000, 60 * 1000);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void position(TabMenu itemName) {

        addFragment(itemName.getTabName().toString());

    }


    private void addFragment(String name) {
        switch (name.toLowerCase()) {
            case "all":
                fragment = AllFragment.getInstance(personDataList);
                break;
            case "safe":
                fragment = SafeFragment.getInstance(personDataList);
                break;
            case "un safe":
                fragment = UnSafeFragment.getInstance(personDataList);
                break;

            case "visitor":
                fragment = VisitorFragment.getInstance(personDataList);
                break;

            case "staff":
                fragment = StaffFragment.getInstance(personDataList);

                break;

            case "student":
                fragment = StudentFragment.getInstance(personDataList);
                break;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

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

        if (id == R.id.logout) {
            // Handle the camera action
            clear();

        } else if (id == R.id.dashboard) {

            startActivity(new Intent(this, DashboardActivity.class));
            finish();

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

    private void loadFireMustList(final int id) {

        Utils.showProgress(FireMusterActivity.this, "Loading");
        try {
            LoginApi loginApi = retrofit.create(LoginApi.class);

            JSONObject jsonObject = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("school_id", id);
            jsonObject.put("params", params);

            Log.e(TAG, "School list : post data  " + jsonObject.toString());

            loginApi.getCreateFireMusterList(jsonObject.toString()).enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                    Utils.hideProgress();
                    if (response.body().getResult().getStatus() == true) {

                        Log.e(TAG, "onResponse: Instance Id " + response.body().getResult().getIncidentId());
                        instancenamedate.setText("Incident Time " + response.body().getResult().getIncident_name());
                        offlinedatalayout.setVisibility(View.GONE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("school_id", id);
                        editor.putInt(school_name + "instance_id", response.body().getResult().getIncidentId());
                        editor.putBoolean(school_name, true);
                        editor.commit();
                        resultResponse = response.body();
                        List<PersonData> list = response.body().getResult().getPeopleData();

                        Collections.sort(list, new Comparator<PersonData>() {
                            @Override
                            public int compare(PersonData lhs, PersonData rhs) {
                                return lhs.getName().compareTo(rhs.getName());
                            }
                        });


                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSchoolId(id);
                        }

                        listSaved.clear();
                        listSaved.addAll(list);
//                        databaseHandler.deleteBasedOnSchoolId(id);
//
//                        databaseHandler.addAllContact(list);
//
//                        final List<PersonData> listdataBase = databaseHandler.getAllPersonData(id);
//
//
                        personDataList.clear();
                        personDataList.addAll(list);
                        defaultList.clear();
                        defaultList.addAll(list);

                        if (fragment != null) {
                            fragment.loadData(list);
                        }

                        showTotalCount(list);
                        new LoadData().execute(list);

                        timing.setText(Utils.getTimeAMPM());


                    } else {
//                        Toast.makeText(FireMusterActivity.this, "There is no records", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
                    Utils.hideProgress();
                    Log.e(TAG, "onFailure: " + t.getMessage());


                    List<PersonData> listdataBase = databaseHandler.getAllPersonData(id);
                    Collections.sort(listdataBase, new Comparator<PersonData>() {
                        @Override
                        public int compare(PersonData lhs, PersonData rhs) {
                            return lhs.getName().compareTo(rhs.getName());
                        }
                    });


                    Log.e(TAG, "onFailure: " + listdataBase.size());

                    personDataList.clear();
                    personDataList.addAll(listdataBase);
                    defaultList.clear();
                    defaultList.addAll(listdataBase);

                    showTotalCount(listdataBase);

                    if (fragment != null) {
                        fragment.loadData(listdataBase);
                    }

                    timing.setText(Utils.getTimeAMPM());


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class LoadData extends AsyncTask<List<PersonData>, List<PersonData>, List<PersonData>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Utils.showProgress(FireMusterActivity.this, "Loading");
        }

        @Override
        protected List<PersonData> doInBackground(List<PersonData>... lists) {

            List<PersonData> list = lists[0];
            Collections.sort(list, new Comparator<PersonData>() {
                @Override
                public int compare(PersonData lhs, PersonData rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });


//            for (int i = 0; i < list.size(); i++) {
//                list.get(i).setSchoolId(id);
//            }
//
            databaseHandler.deleteBasedOnSchoolId(id);

            databaseHandler.addAllContact(list);

            final List<PersonData> listdataBase = databaseHandler.getAllPersonData(id);

            return listdataBase;
        }

        @Override
        protected void onPostExecute(List<PersonData> listdataBase) {
//            super.onPostExecute(list);

//            personDataList.clear();
//            personDataList.addAll(listdataBase);
//            defaultList.clear();
//            defaultList.addAll(listdataBase);
//
//            if (fragment != null) {
//                fragment.loadData(listdataBase);
//            }
//
//            showTotalCount(listdataBase);
//            Utils.hideProgress();
        }
    }


    private void showTotalCount(final List<PersonData> list) {

        new AsyncTask<List<TabMenu>, List<TabMenu>, List<TabMenu>>() {
            @Override
            protected List<TabMenu> doInBackground(List<TabMenu>... lists) {
                List<PersonData> visitorCountList = new ArrayList<>();
                List<PersonData> staffCountList = new ArrayList<>();
                List<PersonData> studentCountList = new ArrayList<>();
                List<PersonData> safeCountList = new ArrayList<>();
                List<PersonData> unsafeCountList = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    PersonData personData = list.get(i);

                    if (personData.getType().toLowerCase().toString().equalsIgnoreCase("Staff".toLowerCase())) {
                        staffCountList.add(personData);
                    }

                    if (personData.getType().toLowerCase().toString().equalsIgnoreCase("Student".toLowerCase().toString())) {
                        studentCountList.add(personData);
                    }

                    if (personData.getType().toLowerCase().toString().equalsIgnoreCase("Visitor".toLowerCase())) {
                        visitorCountList.add(personData);
                    }

                    if (personData.getIsSafe() == true) {
                        safeCountList.add(personData);
                    }

                    if (personData.getIsSafe() != true) {
                        unsafeCountList.add(personData);
                    }
                }

                int acount = list.size();
                int vcount = visitorCountList.size();
                int stffcount = staffCountList.size();
                int scount = studentCountList.size();
                int safecount = safeCountList.size();
                int unsafecount = unsafeCountList.size();

                List<TabMenu> List = new ArrayList<>();

                List.add(new TabMenu("All", "" + acount));
                List.add(new TabMenu("Safe", "" + safecount));
                List.add(new TabMenu("Un Safe", "" + unsafecount));
                List.add(new TabMenu("Visitor", "" + vcount));
                List.add(new TabMenu("Staff", "" + stffcount));
                List.add(new TabMenu("Student", "" + scount));


                return List;
            }

            @Override
            protected void onPostExecute(List<TabMenu> tabMenus) {
//                super.onPostExecute(tabMenus);
                gridViewRecyclerAdapter.update(tabMenus);
            }
        }.execute();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        safelyDispose(networkDisposable);
        mCompositeDisposable.clear();
    }


    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    @Override
    public void updateList() {

        List<PersonData> updatedList = databaseHandler.getAllPersonDataBasedOnServerUpdated();
        if (updatedList.size() > 0) {
            updateCall();

        } else {
            loadFireMustList(id);
        }
    }


    private void updateCall() {
        try {


//            databaseHandler.updateAllPersonData(personDataList);


            List<PersonData> isSafeList = new ArrayList<>();

            List<PersonData> updatedList = databaseHandler.getAllPersonDataBasedOnServerUpdated();

            isSafeList.addAll(updatedList);


            List<PersonData> totalList = databaseHandler.getAllPersonData(id);

            showTotalCount(totalList);


            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < isSafeList.size(); i++) {
                PersonData personData = isSafeList.get(i);
                Log.e(TAG, "updateList: " + personData.toString());

                JSONObject jsonObject1 = new JSONObject();

                jsonObject1.put("stud_class", personData.getStudClass());
                jsonObject1.put("is_safe", personData.getIsSafe());
                jsonObject1.put("type", personData.getType());
                jsonObject1.put("id", personData.getId());
                jsonObject1.put("name", personData.getName());

                jsonArray.put(jsonObject1);

            }

            Log.e(TAG, "updateList: " + jsonArray.toString());


            String json = new Gson().toJson(isSafeList);

            ResultResponse rresponse = resultResponse;


            if (sharedPreferences.getInt("school_id", 0) != 0) {

                updateDataList(id, sharedPreferences.getInt(school_name + "instance_id", 0), jsonArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateCallReturnValue() {
        try {


//            databaseHandler.updateAllPersonData(personDataList);


            List<PersonData> isSafeList = new ArrayList<>();

            List<PersonData> updatedList = databaseHandler.getAllPersonDataBasedOnServerUpdated();

            isSafeList.addAll(updatedList);


            List<PersonData> totalList = databaseHandler.getAllPersonData(id);

            showTotalCount(totalList);


            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < isSafeList.size(); i++) {
                PersonData personData = isSafeList.get(i);
                Log.e(TAG, "updateList: " + personData.toString());

                JSONObject jsonObject1 = new JSONObject();

                jsonObject1.put("stud_class", personData.getStudClass());
                jsonObject1.put("is_safe", personData.getIsSafe());
                jsonObject1.put("type", personData.getType());
                jsonObject1.put("id", personData.getId());
                jsonObject1.put("name", personData.getName());

                jsonArray.put(jsonObject1);

            }

            Log.e(TAG, "updateList: " + jsonArray.toString());


            String json = new Gson().toJson(isSafeList);

            ResultResponse rresponse = resultResponse;


            if (sharedPreferences.getInt("school_id", 0) != 0) {

                updateDataListReturnValues(id, sharedPreferences.getInt(school_name + "instance_id", 0), jsonArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addUpdateList(final PersonData personData) {


        Log.e(TAG, "addUpdateList: called  " + sharedPreferences.getInt(school_name + "instance_id", 0));

        if (sharedPreferences.getInt(school_name + "instance_id", 0) == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "Do you want to create a new fire muster incident?")
                    .setCancelable(false)
                    .setPositiveButton("Create",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    serverUpdate(personData);
                                    is_FireMusterDrillStarted = false;
                                }
                            })
                    .setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {


                                    dialog.cancel();
                                }
                            }).show();


        } else {


            if (is_FireMusterDrillStarted) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(
                        "Do you want to start the fire muster drill?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        serverUpdate(personData);
                                        is_FireMusterDrillStarted = false;
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
//                                        is_FireMusterDrillStarted = true;
//                                        personDataList.clear();
//                                        personDataList.addAll(listSaved);
//                                        defaultList.clear();
//                                        defaultList.addAll(listSaved);


                                        for (int i = 0; i < listSaved.size(); i++) {
                                            PersonData personData1 = listSaved.get(i);
                                            if (personData.getId() == personData1.getId()) {

                                                if (personData.getIsSafe()) {
                                                    personData1.setIsSafe(false);
                                                } else {
                                                    personData1.setIsSafe(true);
                                                }
                                            }
                                        }

                                        if (fragment != null) {
                                            fragment.loadData(listSaved);
                                        }


                                        dialog.cancel();
                                    }
                                }).show();
            } else {

                is_FireMusterDrillStarted = false;
                serverUpdate(personData);
            }


//            serverUpdate(personData);
        }


    }

    private void serverUpdate(PersonData personData) {
        serverUpdateList.clear();
        serverUpdateList.add(personData);
        databaseHandler.updateAllPersonData(serverUpdateList);

        List<PersonData> totalList = databaseHandler.getAllPersonData(id);

        showTotalCount(totalList);

        if (fragment != null) {
            fragment.loadData(totalList);
        }

        if (Utils.isOnline(getApplicationContext())) {
            updateList();
            offlinedatalayout.setVisibility(View.GONE);
        } else {
//            Toast.makeText(this, "Offline data is synching", Toast.LENGTH_SHORT).show();
            offlinedatalayout.setVisibility(View.VISIBLE);
        }

    }


    private void updateDataListReturnValues(final int id, final int incedentId, final JSONArray json) {

//        Utils.showProgress(FireMusterActivity.this, "Loading");

        try {
            LoginApi loginApi = retrofit.create(LoginApi.class);

            JSONObject jsonObject = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("school_id", id);
            params.put("incident_id", incedentId);
            params.put("people_data", json);

            jsonObject.put("params", params);

            Log.e(TAG, "Update List : post data  " + jsonObject.toString());

            loginApi.updateFireMusterList(jsonObject.toString()).enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                    Utils.hideProgress();

                    loadFireMustList(id);


                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
//                    Utils.hideProgress();
                    Log.e(TAG, "onFailure: " + t.getMessage());

                    if (Utils.isOnline(getApplicationContext())) {
                        updateList();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void updateDataList(final int id, final int incedentId, final JSONArray json) {

//        Utils.showProgress(FireMusterActivity.this, "Loading");

        try {
            LoginApi loginApi = retrofit.create(LoginApi.class);

            JSONObject jsonObject = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("school_id", id);
            params.put("incident_id", incedentId);
            params.put("people_data", json);

            jsonObject.put("params", params);

            Log.e(TAG, "Update List : post data  " + jsonObject.toString());

            loginApi.updateFireMusterList(jsonObject.toString()).enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                    Utils.hideProgress();

//                    loadFireMustList(id);


                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
//                    Utils.hideProgress();
                    Log.e(TAG, "onFailure: " + t.getMessage());

                    if (Utils.isOnline(getApplicationContext())) {
                        updateList();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void resetList() {
        for (int i = 0; i < personDataList.size(); i++) {
            personDataList.get(i).setIsSafe(false);
        }

        if (fragment != null) {
            fragment.loadData(personDataList);
        }

        checkSelectState();
    }

    @Override
    public void ascending() {

        Collections.sort(personDataList, new Comparator<PersonData>() {
            @Override
            public int compare(PersonData lhs, PersonData rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        if (fragment != null) {
            fragment.loadData(personDataList);
        }
    }

    @Override
    public void descending() {
        Collections.sort(personDataList, new Comparator<PersonData>() {
            @Override
            public int compare(PersonData lhs, PersonData rhs) {
                return rhs.getName().compareTo(lhs.getName());
            }
        });

        if (fragment != null) {
            fragment.loadData(personDataList);
        }
    }

    @Override
    public void defaultList() {
        if (fragment != null && defaultList.size() > 0) {
            fragment.loadData(defaultList);
        } else {
            Log.e(TAG, "defaultList: no data ");
        }
    }

    @Override
    public void searchlist(String search) {


        List<PersonData> searchList = new ArrayList<>();

        for (int i = 0; i < personDataList.size(); i++) {
            PersonData personData = personDataList.get(i);

            if (personData.getName().toLowerCase().contains(search.toLowerCase())) {
                searchList.add(personData);
                Log.e(TAG, "searchlist: " + personData.getName());
            }

        }
        if (fragment != null) {
            fragment.loadData(searchList);
        }

    }

    private void selectUnselectAll(boolean isSelect) {

        List<PersonData> list = new ArrayList<>(personDataList.size());
        list.clear();
        list.addAll(personDataList);

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsSafe(isSelect);
        }
        if (fragment != null) {
            fragment.loadData(list);
        }

    }

    @Override
    public void checkSelectState() {
//
//        Log.e(TAG, "checkSelectState: ");
//        int count = 0;
//        for (int i = 0; i < personDataList.size(); i++) {
//            if (!personDataList.get(i).getIsSafe()) {
//                count++;
//            }
//        }
//
//        if (count > 0) {
//            selectallimg.setImageResource(R.drawable.ic_uncheck);
//            isSelectAll = true;
//        }

    }
}
