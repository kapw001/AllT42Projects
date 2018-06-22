package com.pappaya.prms.activitys;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pappaya.prms.fragments.AddProjects;
import com.pappaya.prms.fragments.MyTimesheets;
import com.pappaya.prms.fragments.TimeSheetsToApprove;
import com.pappaya.prms.fragments.Weekly;
import com.pappaya.prms.interfaceses.Call;
import com.pappaya.prms.interfaceses.CallJsonRequest;
import com.pappaya.prms.model.Account_id;
import com.pappaya.prms.model.Account_ids;
import com.pappaya.prms.model.Employee_id;
import com.pappaya.prms.model.TimeSheet;
import com.pappaya.prms.model.TimeSheetActivitys;
import com.pappaya.prms.model.User_id;
import com.pappaya.prms.sessionmanagement.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.pappaya.prms.R;

import com.pappaya.prms.interfaceses.CallFragment;
import com.pappaya.prms.model.TimeSheetDetail;
import com.pappaya.prms.utils.Cons;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CallFragment, Call, CallJsonRequest {
    private static final String TAG = "MainActivity";

    private SessionManager sessionManager;

    private Fragment fragment;

    private NavigationView navigationView;
    public static List<TimeSheet> movieList = new ArrayList<>();
    private List<TimeSheet> mytimesheettoapprove = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sessionManager = new SessionManager(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("refreshapirequest"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getMyTimeSheet();
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        if (sessionManager.isLoggedIn()) {
            View view = navigationView.getHeaderView(0);
            if (sessionManager.getUserDetails().get(SessionManager.KEY_ISEMPLOYEE).equalsIgnoreCase("false")) {
                navigationView.getMenu().clear(); //clear old inflated items.
                navigationView.inflateMenu(R.menu.activity_main_draweremployee); //inflate new items.
            }
            if (!sessionManager.getUserDetails().get(SessionManager.KEY_IMAGE).equalsIgnoreCase("false")) {
                String base = sessionManager.getUserDetails().get(SessionManager.KEY_IMAGE);//

                byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
                ImageView img = (ImageView) view.findViewById(R.id.profile);
                img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                TextView username = (TextView) view.findViewById(R.id.username);
                username.setText(sessionManager.getUserDetails().get(SessionManager.KEY_EMPLOYEENAME));
            }
            if (sessionManager.getUserDetails().get(SessionManager.KEY_IMAGE).equalsIgnoreCase("true")) {


            } else {
                ImageView img = (ImageView) view.findViewById(R.id.profile);
                img.setImageResource(R.drawable.pappaya);
                TextView username = (TextView) view.findViewById(R.id.username);
                username.setText(sessionManager.getUserDetails().get(SessionManager.KEY_EMPLOYEENAME));
            }

        }


        navigationView.setNavigationItemSelectedListener(this);

//        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setCheckedItem(R.id.weekly);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.weekly));

        movieList.clear();
        mytimesheettoapprove.clear();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

            if (fragment instanceof Weekly) {
                ((Weekly) fragment).saveData();
            }

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.weekly) {
            // Handle the camera action
        } else if (id == R.id.mytimesheets) {

        } else if (id == R.id.timesheetstoapprove) {

        } else if (id == R.id.logout) {
            sessionManager.logoutUser();
        }


        addFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(int id) {


        switch (id) {
            case R.id.weekly:
                if (!(fragment instanceof Weekly)) {
                    fragment = new AddProjects();
                    getSupportActionBar().setTitle("Weekly");
                }

                break;
            case R.id.callbackid:
                fragment = new AddProjects();
                getSupportActionBar().setTitle("Weekly");
                break;
            case R.id.mytimesheets:

                fragment = new MyTimesheets();
                getSupportActionBar().setTitle("My Timesheets");
                break;
            case R.id.timesheetstoapprove:
                fragment = new TimeSheetsToApprove();
                getSupportActionBar().setTitle("Timesheets to approve");
                break;
            case R.id.addnewproject:
                fragment = new Weekly();

                navigationView.setCheckedItem(R.id.weekly);
//                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.weekly));

                break;

        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (fragment.isAdded() && (fragment instanceof Weekly || fragment instanceof MyTimesheets || fragment instanceof TimeSheetsToApprove)) {
            fragmentTransaction.show(fragment);
        } else {
            if (fragment instanceof Weekly) {
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null);
                Log.e(TAG, "addFragment: Times" + (fragment instanceof Weekly));
            } else {
                fragmentTransaction.replace(R.id.container, fragment);
            }
        }
        if (fragment instanceof AddProjects) {
            ((AddProjects) fragment).setFetchDataList1(movieList);
        }
        if (fragment instanceof Weekly) {
            ((Weekly) fragment).setFetchDataList1(movieList);
        }

        if (fragment instanceof MyTimesheets) {
            ((MyTimesheets) fragment).setFetchDataList1(movieList);
        }
        if (fragment instanceof TimeSheetsToApprove) {
            ((TimeSheetsToApprove) fragment).setFetchDataList1(mytimesheettoapprove);
        }


        fragmentTransaction.commit();

    }


    @Override
    public void callFragment(int id) {
        if (id == R.id.callbackid) {
            addFragment(id);
        } else if (id == R.id.addnewproject) {
            addFragment(id);
        } else {
            navigationView.setCheckedItem(id);
            onNavigationItemSelected(navigationView.getMenu().findItem(id));
        }

    }

    @Override
    public void call(int id) {
        addFragment(id);
    }


    private void getMyTimeSheet() {
        JSONObject params = new JSONObject();
        try {
            params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
            params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
            params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
            params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("jsonrpc", "2.0");
            jsonObject.put("method", "call");
            jsonObject.put("id", "2");
            jsonObject.put("params", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/" + "timesheet",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: Test " + response);
                try {
                    JSONObject jsonObject = response.getJSONObject("result"); //new JSONObject("result");

                    JSONArray mytimesheet = jsonObject.getJSONArray("my_time_sheet");

//                    Toast.makeText(MainActivity.this, "" + mytimesheet.toString(), Toast.LENGTH_SHORT).show();


                    for (int k = 0; k < mytimesheet.length(); k++) {


                        JSONObject my_time_sheetobject = mytimesheet.getJSONObject(k);

                        TimeSheet timeSheet = new TimeSheet();

                        JSONArray activities = my_time_sheetobject.getJSONArray("activities");

//                        Log.e(TAG, "onResponse: Activities   " + activities.toString());

                        ArrayList<TimeSheetActivitys> timeSheetActivityseslist = new ArrayList<>();

                        for (int i = 0; i < activities.length(); i++) {
                            JSONObject activitiesjsonObject = activities.getJSONObject(i);
                            TimeSheetActivitys timeSheetActivitys = new TimeSheetActivitys();
                            timeSheetActivitys.setDisplay_name(activitiesjsonObject.getString("display_name"));
                            timeSheetActivitys.setIs_billable(activitiesjsonObject.getString("is_billable"));
                            timeSheetActivitys.setStatus(activitiesjsonObject.getString("status"));
                            timeSheetActivitys.setCdate(activitiesjsonObject.getString("date"));
                            timeSheetActivitys.setId(activitiesjsonObject.getString("id"));
                            timeSheetActivitys.setName(activitiesjsonObject.getString("name"));
                            timeSheetActivitys.setUnit_amount(activitiesjsonObject.getString("unit_amount"));

                            JSONObject account_idjsonObject = activitiesjsonObject.getJSONObject("account_id");

                            timeSheetActivitys.setAccount_id(new Account_id(account_idjsonObject.getString("id"), account_idjsonObject.getString("name")));
                            timeSheetActivityseslist.add(timeSheetActivitys);

                        }

                        timeSheet.setTimeSheetActivityseslist(timeSheetActivityseslist);

                        JSONObject detail = my_time_sheetobject.getJSONObject("detail");
                        ArrayList<TimeSheetDetail> TimeSheetDetaillist = new ArrayList<>();

                        TimeSheetDetail timeSheetDetail = new TimeSheetDetail();

                        timeSheetDetail.setId(detail.getString("id"));
                        timeSheetDetail.setState(detail.getString("state"));
                        timeSheetDetail.setTotal_timesheet(detail.getString("total_timesheet"));
//                                Log.e(TAG, "onResponse: total_timesheet   " + detail.getString("total_timesheet"));
                        timeSheetDetail.setDate_from(detail.getString("date_from"));
                        timeSheetDetail.setDate_to(detail.getString("date_to"));

                        JSONObject employeeid = detail.getJSONObject("employee_id");
                        timeSheetDetail.setEmployee_id(new Employee_id(employeeid.getString("id"), employeeid.getString("name")));

                        JSONObject userid = detail.getJSONObject("user_id");
                        timeSheetDetail.setUser_id(new User_id(userid.getString("id"), userid.getString("name")));


                        JSONArray account_ids = detail.getJSONArray("account_ids");

//                        Log.e(TAG, "onResponse: Account ids " + account_ids.toString());

                        ArrayList<Account_ids> account_idseslist = new ArrayList<>();

                        for (int i = 0; i < account_ids.length(); i++) {

                            JSONObject account_idsjsonObject = account_ids.getJSONObject(i);
                            Account_ids account_ids1 = new Account_ids();
                            account_ids1.setId(account_idsjsonObject.getString("id"));
                            account_ids1.setName(account_idsjsonObject.getString("name"));
                            account_idseslist.add(account_ids1);
                        }

                        timeSheetDetail.setAccount_ids(new Account_ids(account_idseslist));

                        TimeSheetDetaillist.add(timeSheetDetail);

                        timeSheet.setTimeSheetDetailslist(TimeSheetDetaillist);

//                            list.add(timeSheet);
                        movieList.add(timeSheet);


                    }

//                    Log.e(TAG, "onResponse: list size " + movieList.size());

                    if (fragment instanceof MyTimesheets) {
                        ((MyTimesheets) fragment).setFetchDataList(movieList);
                    }

                    if (fragment instanceof AddProjects) {
                        ((AddProjects) fragment).setFetchDataList(movieList);
                    }

                    if (fragment instanceof Weekly) {
                        ((Weekly) fragment).setFetchDataList1(movieList);
                    }


                    JSONArray mytimesheetapprove = jsonObject.getJSONArray("time_sheet_to_approve");
                    for (int j = 0; j < mytimesheetapprove.length(); j++) {


                        JSONObject my_time_sheetobjectapprove = mytimesheetapprove.getJSONObject(j);

                        TimeSheet timeSheetapprove = new TimeSheet();

                        JSONArray activitiesapprove = my_time_sheetobjectapprove.getJSONArray("activities");

                        ArrayList<TimeSheetActivitys> timeSheetActivityseslistapprove = new ArrayList<>();

                        for (int i = 0; i < activitiesapprove.length(); i++) {
                            JSONObject activitiesjsonObject = activitiesapprove.getJSONObject(i);
                            TimeSheetActivitys timeSheetActivitys = new TimeSheetActivitys();
                            timeSheetActivitys.setDisplay_name(activitiesjsonObject.getString("display_name"));
                            timeSheetActivitys.setIs_billable(activitiesjsonObject.getString("is_billable"));
                            timeSheetActivitys.setStatus(activitiesjsonObject.getString("status"));
                            timeSheetActivitys.setCdate(activitiesjsonObject.getString("date"));
                            timeSheetActivitys.setId(activitiesjsonObject.getString("id"));
                            timeSheetActivitys.setName(activitiesjsonObject.getString("name"));
                            timeSheetActivitys.setUnit_amount(activitiesjsonObject.getString("unit_amount"));
                            JSONObject account_idjsonObject = activitiesjsonObject.getJSONObject("account_id");

                            timeSheetActivitys.setAccount_id(new Account_id(account_idjsonObject.getString("id"), account_idjsonObject.getString("name")));
                            timeSheetActivityseslistapprove.add(timeSheetActivitys);

                        }

                        timeSheetapprove.setTimeSheetActivityseslist(timeSheetActivityseslistapprove);

                        JSONObject detailapprove = my_time_sheetobjectapprove.getJSONObject("detail");
                        ArrayList<TimeSheetDetail> TimeSheetDetaillistapprove = new ArrayList<>();

                        TimeSheetDetail timeSheetDetailapprove = new TimeSheetDetail();

                        timeSheetDetailapprove.setId(detailapprove.getString("id"));
                        timeSheetDetailapprove.setState(detailapprove.getString("state"));
                        timeSheetDetailapprove.setTotal_timesheet(detailapprove.getString("total_timesheet"));
                        timeSheetDetailapprove.setDate_from(detailapprove.getString("date_from"));
                        timeSheetDetailapprove.setDate_to(detailapprove.getString("date_to"));

                        JSONObject employeeidapprove = detailapprove.getJSONObject("employee_id");
                        timeSheetDetailapprove.setEmployee_id(new Employee_id(employeeidapprove.getString("id"), employeeidapprove.getString("name")));

                        JSONObject useridapprove = detailapprove.getJSONObject("user_id");
                        timeSheetDetailapprove.setUser_id(new User_id(useridapprove.getString("id"), useridapprove.getString("name")));


                        JSONArray account_idsapprove = detailapprove.getJSONArray("account_ids");

                        ArrayList<Account_ids> account_idseslistapprove = new ArrayList<>();

                        for (int i = 0; i < account_idsapprove.length(); i++) {

                            JSONObject account_idsjsonObject = account_idsapprove.getJSONObject(i);
                            Account_ids account_ids1 = new Account_ids();
                            account_ids1.setId(account_idsjsonObject.getString("id"));
                            account_ids1.setName(account_idsjsonObject.getString("name"));
                            account_idseslistapprove.add(account_ids1);
                        }

                        timeSheetDetailapprove.setAccount_ids(new Account_ids(account_idseslistapprove));

                        TimeSheetDetaillistapprove.add(timeSheetDetailapprove);

                        timeSheetapprove.setTimeSheetDetailslist(TimeSheetDetaillistapprove);
                        mytimesheettoapprove.add(timeSheetapprove);
                    }

//                            list.add(timeSheet);


                    if (fragment instanceof TimeSheetsToApprove) {
                        ((TimeSheetsToApprove) fragment).setFetchDataList(mytimesheettoapprove);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onCallRequest() {
        movieList.clear();
        mytimesheettoapprove.clear();
        getMyTimeSheet();
    }

    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);

            if (message.equalsIgnoreCase("refresh")) {
                onCallRequest();
//                Toast.makeText(MainActivity.this, "Call", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}
