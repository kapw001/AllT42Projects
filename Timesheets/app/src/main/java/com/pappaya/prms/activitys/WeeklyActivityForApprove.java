package com.pappaya.prms.activitys;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.pappaya.prms.R;
import com.pappaya.prms.interfaceses.AddDeleteRow;
import com.pappaya.prms.interfaceses.Call;
import com.pappaya.prms.interfaceses.CallFragment;
import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.TimeSheetActivitys;
import com.pappaya.prms.recycle.ComplexRecyclerViewAdapterWeeklyActivity;
import com.pappaya.prms.sessionmanagement.SessionManager;
import com.pappaya.prms.utils.AccountIdCompare;
import com.pappaya.prms.utils.DateComapare;
import com.pappaya.prms.utils.JsonRequest;
import com.pappaya.prms.utils.Utilitys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WeeklyActivityForApprove extends AppCompatActivity implements AddDeleteRow {
    private List<Object> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ComplexRecyclerViewAdapterWeeklyActivity mAdapter;
    private CallFragment callFragment;

    private Button edit, submit;
    private ImageView addprojects;

    private TextView startDate, endDate, totalhours;

    private int edit_position;
    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    private int RESULT_CODEFIRST = 1;
    private int RESULT_CODE = 2;
    private int RESULT_CODEANOTHER = 3;

    private CardView card_view;

    ArrayList<Object> list = new ArrayList<>();
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
    private Call call;

    List<AddDates> addDatesList = new ArrayList<>();

    SessionManager sessionManager;

    String timesheetid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly);
        sessionManager = new SessionManager(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewweekly);

        card_view = (CardView) findViewById(R.id.card_view);

        edit = (Button) findViewById(R.id.edit);
        submit = (Button) findViewById(R.id.submit);
        addprojects = (ImageView) findViewById(R.id.addprojects);

        startDate = (TextView) findViewById(R.id.startdate);

        endDate = (TextView) findViewById(R.id.enddate);
        totalhours = (TextView) findViewById(R.id.totalhours);

        mAdapter = new ComplexRecyclerViewAdapterWeeklyActivity(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        Toast.makeText(WeeklyActivityForApprove.this, "" + getIntent().getStringExtra("startdate"), Toast.LENGTH_SHORT).show();

        addprojects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), AddNewProjectTimesheets.class);
                in.putExtra("startdate", getIntent().getStringExtra("startdate"));
                in.putExtra("enddate", getIntent().getStringExtra("enddate"));
                startActivityForResult(in, RESULT_CODEANOTHER);
            }
        });
        addprojects.setColorFilter(getResources().getColor(R.color.colorPrimary));
//        getSampleArrayList();
        if (getIntent().getBooleanExtra("menuenable", true)) {
            addprojects.setVisibility(View.VISIBLE);
        } else {
            addprojects.setVisibility(View.INVISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), WeeklyEditable.class);
                startActivity(in);
            }
        });

        ArrayList<TimeSheetActivitys> timeSheetList = getIntent().getParcelableArrayListExtra("open");


        timesheetid = getIntent().getStringExtra("sheetid");
        Log.e(TAG, "onCreate: sheetid  " + getIntent().getStringExtra("sheetid"));


//        if (timeSheetList.size() > 0) {
//            String a = timeSheetList.get(0).getAccount_id().getName();
//            Log.e(TAG, "onResponse:Accont ID Name " + a);
//
//        }


        getSampleArrayList(getIntent().getStringExtra("startdate"), getIntent().getStringExtra("enddate"), "", getIntent().getStringExtra("project"), getIntent().getStringExtra("hours"), timeSheetList);


//        getSampleArrayList(getIntent().getStringExtra("startdate"), getIntent().getStringExtra("enddate"), getIntent().getStringExtra("billable"), getIntent().getStringExtra("project"), getIntent().getStringExtra("hours"));

    }

    private static final String TAG = "WeeklyActivity";


    private void getSampleArrayList(String datefrom, String dateto, String billable, String projects, String hours, ArrayList<TimeSheetActivitys> TimeSheetActivitysList) {

        Collections.sort(TimeSheetActivitysList, new DateComapare());
        Collections.sort(TimeSheetActivitysList, new AccountIdCompare());

        startDate.setText(datefrom);
        endDate.setText(dateto);

//        totalhours.setText("Total Hours: " + hours + " Hrs");

//        if (!projects.isEmpty()) {
//
//            list.add(new Projects(projects, billable));
//        }

        String project = "";

        String name = "";
        double total_hours = 0.0;
        for (int i = 0; i < TimeSheetActivitysList.size(); i++) {

            if (!TimeSheetActivitysList.get(i).getAccount_id().getName().contentEquals(name)) {

//                list.add(new Projects(TimeSheetActivitysList.get(i).getAccount_id().getName(), ""));
                project = TimeSheetActivitysList.get(i).getAccount_id().getName();
            }

            name = TimeSheetActivitysList.get(i).getAccount_id().getName();

            TimeSheetActivitys timeSheetActivitys = TimeSheetActivitysList.get(i);

//            Log.e(TAG, "getSampleArrayList: " + timeSheetActivitys.getAccount_id().getName().toString());

            String dateactivitys = timeSheetActivitys.getCdate();
            total_hours += Double.parseDouble(timeSheetActivitys.getUnit_amount());

            Date datedateactivitys = null;
            try {

                datedateactivitys = parser.parse(dateactivitys);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
            final String cdate = formatter.format(datedateactivitys);

            list.add(new AddDates(cdate, timeSheetActivitys.getUnit_amount(), timeSheetActivitys.getDisplay_name(), timeSheetActivitys.getId(), timeSheetActivitys.getAccount_id().getId(), project));

            addDatesList.add(new AddDates(cdate, timeSheetActivitys.getUnit_amount(), timeSheetActivitys.getDisplay_name(), timeSheetActivitys.getId(), timeSheetActivitys.getAccount_id().getId(), project));
        }
        totalhours.setText("Total Hours: " + total_hours);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void delete(int position) {


        if (addDatesList.get(position).getLine_id().equalsIgnoreCase("0")) {
            addDatesList.remove(position);
        } else {
            addDatesList.get(position).setIs_timesheet(false);
        }
//        addDatesList.set(position, new AddDates(addDatesList.get(position).getDate(), addDatesList.get(position).getUnit_amount(), addDatesList.get(position).getName(), addDatesList.get(position).getLine_id(), addDatesList.get(position).getAccount_id()));
//        Log.e(TAG, "delete: " + addDatesList.get(position).is_timesheet());
        list.remove(position);

//        Log.e(TAG, "delete: " + addDatesList.toString());

        double total_hours = 0.0;
        for (int i = 0; i < addDatesList.size(); i++) {
            total_hours += Double.parseDouble(addDatesList.get(i).getUnit_amount());
        }

        totalhours.setText("Total Hours: " + total_hours);


        mAdapter.notifyDataSetChanged();
    }

    private int pos;

    @Override
    public void addorEdit(int position) {
        int pos = position;

        if (list.get(position) instanceof AddDates) {
            AddDates data = (AddDates) list.get(position);
//            pos = position;

//
            Intent intent = new Intent(getApplicationContext(), AddEditTimeSheets.class);

            intent.putExtra("position", pos);
            intent.putExtra("date", data.getDate());
            intent.putExtra("hour", data.getUnit_amount());
            intent.putExtra("des", data.getName());

            startActivityForResult(intent, RESULT_CODE);
        }


    }

    @Override
    public void addNewProjects() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.weeklymenu, menu);
        if (getIntent().getBooleanExtra("menuenable", true) && getIntent().getBooleanExtra("updatetimesheet", true)) {
            getMenuInflater().inflate(R.menu.weeklymenu, menu);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {

//            JSONArray jsArray = new JSONArray(addDatesList);
//            Log.e(TAG, "onOptionsItemSelected: Json Array " + jsArray.toString());
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < addDatesList.size(); i++) {


                addDatesList.get(i).setDate(Utilitys.converDateServer(addDatesList.get(i).getDate()));

                JSONObject jsonObject = new JSONObject();
                AddDates addDates = addDatesList.get(i);
                try {
                    jsonObject.put("date", addDates.getDate());
                    jsonObject.put("account_id", addDates.getAccount_id());
                    jsonObject.put("is_timesheet", addDates.is_timesheet());
                    jsonObject.put("line_id", Integer.parseInt(addDates.getLine_id()));
                    jsonObject.put("name", addDates.getName());
                    jsonObject.put("unit_amount", Double.parseDouble(addDates.getUnit_amount()));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            JSONArray jsArray = new JSONArray(addDatesList);

            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(addDatesList).getAsJsonArray();
            Log.e(TAG, "onOptionsItemSelected: Gson " + myCustomArray);
            JSONObject params = new JSONObject();
            try {

                params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
                params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
                params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
                params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
                params.put("sheet_id", timesheetid);

                params.put("lines", jsonArray);
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


            Log.e(TAG, "onOptionsItemSelected: post format " + jsonObject.toString());


            JsonRequest.makeRequest(this, jsonObject, "update", new JsonRequest.RequestCallback() {
                @Override
                public void onSuccess(JSONObject response) {

                    Log.e(TAG, "onSuccess:Update Timesheet   " + response);
                    sendMessage();
                    finish();

                }

                @Override
                public void OnFail(VolleyError error) {
                    Log.e(TAG, "OnFail: " + error);
                }
            }, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/");


//        try {
//            JSONObject jsonObject22 = new JSONObject(addDatesList.toString());
//            Log.e(TAG, "onOptionsItemSelected: " + jsonObject22.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//            Log.e(TAG, "onOptionsItemSelected: List Json " + addDatesList.toString());

            for (int i = 0; i < addDatesList.size(); i++) {

                addDatesList.get(i).setDate(Utilitys.converDate(addDatesList.get(i).getDate()));
            }

//            Toast.makeText(getApplicationContext(), "Timesheet saved!", Toast.LENGTH_SHORT).show();
//            Intent in = new Intent(getActivity(), WeeklyEditable.class);
//            startActivity(in);
        } else if (id == R.id.submit)

        {

            JSONObject params = new JSONObject();
            try {

                params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
                params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
                params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
                params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
                params.put("sheet_id", timesheetid);
                params.put("state", "confirm");

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

            JsonRequest.makeRequest(this, jsonObject, "update_status", new JsonRequest.RequestCallback() {
                @Override
                public void onSuccess(JSONObject response) {

                    Log.e(TAG, "onSuccess:Update Timesheet   " + response);
                    sendMessage();
                    finish();

                }

                @Override
                public void OnFail(VolleyError error) {
                    Log.e(TAG, "OnFail: " + error);
                }
            }, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/");
//            Toast.makeText(getApplicationContext(), "Timesheet submit!", Toast.LENGTH_SHORT).show();
//            Intent in = new Intent(getActivity(), WeeklyEditable.class);
//            startActivity(in);
        } else if (item.getItemId() == android.R.id.home)

        {

            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.

                onOptionsItemSelected(item);

    }


    private void sendMessage() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("refreshapirequest");
        // You can also include some extra data.
        intent.putExtra("message", "refresh");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == Activity.RESULT_OK) {

        if (requestCode == RESULT_CODE) {
            int position = data.getIntExtra("position", 0);

            AddDates s = (AddDates) list.get(position);
            AddDates s1 = (AddDates) addDatesList.get(position);

            s.setName(data.getStringExtra("des"));
            s.setUnit_amount(data.getStringExtra("hour"));
            s.setDate(data.getStringExtra("date"));

            s1.setName(data.getStringExtra("des"));
            s1.setUnit_amount(data.getStringExtra("hour"));
            s1.setDate(data.getStringExtra("date"));

            mAdapter.notifyDataSetChanged();

        } else if (requestCode == RESULT_CODEANOTHER) {

            if (data.getBooleanExtra("isAdded", false)) {
//                list.add(new Projects(data.getStringExtra("project"), data.getStringExtra("billable")));
                list.add(new AddDates(data.getStringExtra("date"), data.getStringExtra("hour"), data.getStringExtra("des"), "0", data.getStringExtra("projectid"), data.getStringExtra("project")));
                addDatesList.add(new AddDates(data.getStringExtra("date"), data.getStringExtra("hour"), data.getStringExtra("des"), "0", data.getStringExtra("projectid"), data.getStringExtra("project")));
                mAdapter.notifyDataSetChanged();

                double total_hours = 0.0;
                for (int i = 0; i < addDatesList.size(); i++) {
                    total_hours += Double.parseDouble(addDatesList.get(i).getUnit_amount());
                }

                totalhours.setText("Total Hours: " + total_hours);
            }

        } else if (requestCode == RESULT_CODEFIRST) {
            if (data.getBooleanExtra("isAdded", false)) {
//                getSampleArrayList(data.getStringExtra("datefrom"), data.getStringExtra("dateto"), data.getStringExtra("billable"), data.getStringExtra("project"), data.getStringExtra("hours"));
            } else {
                call.call(R.id.weekly);
            }
        } else if (requestCode == 4) {

            call.call(R.id.weekly);

        } else if (requestCode == 5) {

        }

    }
}
