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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.pappaya.prms.interfaceses.ApproveOrRejectedInterFace;
import com.pappaya.prms.interfaceses.Call;
import com.pappaya.prms.model.TimeSheetActivitys;
import com.pappaya.prms.recycle.ComplexRecyclerViewAdapterApproveRejectActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pappaya.prms.R;
import com.pappaya.prms.interfaceses.AddDeleteRow;
import com.pappaya.prms.interfaceses.CallFragment;
import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.Projects;
import com.pappaya.prms.sessionmanagement.SessionManager;
import com.pappaya.prms.utils.JsonRequest;
import com.pappaya.prms.utils.Utilitys;

import org.json.JSONException;
import org.json.JSONObject;

public class ApproveRejectActicity extends AppCompatActivity implements AddDeleteRow, ApproveOrRejectedInterFace {
    private static final String TAG = "ApproveRejectActicity";
    private List<Object> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ComplexRecyclerViewAdapterApproveRejectActivity mAdapter;
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

    private Call call;
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    String timesheetid = "";

    SessionManager sessionManager;
    private LinearLayout lname;
    private TextView empName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly);

        sessionManager = new SessionManager(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        lname = (LinearLayout) findViewById(R.id.lname);
        lname.setVisibility(View.VISIBLE);
        empName = (TextView) findViewById(R.id.employeename);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewweekly);

        card_view = (CardView) findViewById(R.id.card_view);

        edit = (Button) findViewById(R.id.edit);
        submit = (Button) findViewById(R.id.submit);
        addprojects = (ImageView) findViewById(R.id.addprojects);

        startDate = (TextView) findViewById(R.id.startdate);

        endDate = (TextView) findViewById(R.id.enddate);
        totalhours = (TextView) findViewById(R.id.totalhours);

        mAdapter = new ComplexRecyclerViewAdapterApproveRejectActivity(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        addprojects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), AddNewProjectTimesheets.class);
                startActivityForResult(in, RESULT_CODEANOTHER);
            }
        });

        if (getIntent().getBooleanExtra("menuenable", true) && getIntent().getBooleanExtra("updatetimesheet", true)) {
            addprojects.setVisibility(View.INVISIBLE);
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

        empName.setText(getIntent().getStringExtra("name") + "");


        ArrayList<TimeSheetActivitys> timeSheetList = getIntent().getParcelableArrayListExtra("open");

        timesheetid = getIntent().getStringExtra("sheetid");

        getSampleArrayList(getIntent().getStringExtra("startdate"), getIntent().getStringExtra("enddate"), "", getIntent().getStringExtra("project"), getIntent().getStringExtra("hours"), timeSheetList);

    }


    private void getSampleArrayList(String datefrom, String dateto, String billable, String projects, String hours, ArrayList<TimeSheetActivitys> TimeSheetActivitysList) {

        startDate.setText(datefrom);
        endDate.setText(dateto);


//        list.add(new Projects(projects, billable));

        if (!projects.isEmpty()) {

//            list.add(new Projects(projects, billable));
        }
        String project = "";
        double total_hours = 0.0;
        String name = "";
        for (int i = 0; i < TimeSheetActivitysList.size(); i++) {

            TimeSheetActivitys timeSheetActivitys = TimeSheetActivitysList.get(i);

            if (!TimeSheetActivitysList.get(i).getAccount_id().getName().contentEquals(name)) {

//                list.add(new Projects(TimeSheetActivitysList.get(i).getAccount_id().getName(), ""));
                project = TimeSheetActivitysList.get(i).getAccount_id().getName();
            }

            name = TimeSheetActivitysList.get(i).getAccount_id().getName();
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

//            list.add(new AddDates(cdate, timeSheetActivitys.getUnit_amount() + " Hrs", timeSheetActivitys.getDisplay_name()));
            list.add(new AddDates(cdate, timeSheetActivitys.getUnit_amount(), timeSheetActivitys.getDisplay_name(), timeSheetActivitys.getId(), timeSheetActivitys.getAccount_id().getId(), project, timeSheetActivitys.getStatus(), timeSheetActivitys.getIs_billable()));
        }

        totalhours.setText("Total Hours: " + total_hours + " Hrs");


        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void ApproveOrRejected(final String btnstats, final int position) {
        AddDates addDates = (AddDates) list.get(position);
        Utilitys.showProgressDialog(ApproveRejectActicity.this);
        final String btns;

        if (btnstats.equalsIgnoreCase("Approved")) {
            btns = "approved";
        } else {
            btns = "refused";
        }

        JSONObject params = new JSONObject();
        try {

            params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
            params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
            params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
            params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
            params.put("sheet_id", timesheetid);
            params.put("line_id", Integer.parseInt(addDates.getLine_id()));
            params.put("status", btns);

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

        Log.e(TAG, "ApproveOrRejected: " + jsonObject);

        JsonRequest.makeRequest(this, jsonObject, "update_sheet_status", new JsonRequest.RequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {

                Utilitys.hideProgressDialog(ApproveRejectActicity.this);
                ((AddDates) list.get(position)).setStatus(btns);
                mAdapter.notifyDataSetChanged();

                Log.e(TAG, "onSuccess:Update Timesheet   " + response);
                sendMessage();
//                finish();

            }

            @Override
            public void OnFail(VolleyError error) {
                Log.e(TAG, "OnFail: " + error);
                Utilitys.hideProgressDialog(ApproveRejectActicity.this);
            }
        }, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/");


    }

    @Override
    public void delete(int position) {

        list.remove(position);
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
            intent.putExtra("billable", data.getIs_billable());
            intent.putExtra("startdate", getIntent().getStringExtra("startdate"));
            intent.putExtra("enddate", getIntent().getStringExtra("enddate"));

//            Toast.makeText(getActivity(), "" + position + " " + data.getDates() + "  " + data.getHours() + "  " + data.getComments(), Toast.LENGTH_SHORT).show();

//            intent.putParcelableArrayListExtra("data", data.getList());


            startActivityForResult(intent, RESULT_CODE);
        }


    }

    @Override
    public void addNewProjects() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getBooleanExtra("menuenable", true) && getIntent().getBooleanExtra("updatetimesheet", true)) {
//            getMenuInflater().inflate(R.menu.weeklymenu, menu);
        } else if (getIntent().getBooleanExtra("menuenable", true)) {
//            getMenuInflater().inflate(R.menu.approvereject, menu);
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
        if (id == R.id.approve) {

            JSONObject params = new JSONObject();
            try {

                params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
                params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
                params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
                params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
                params.put("sheet_id", timesheetid);
                params.put("state", "done");

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
            Toast.makeText(getApplicationContext(), "Timesheet approved!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.reject) {
            Toast.makeText(getApplicationContext(), "Timesheet rejected!", Toast.LENGTH_SHORT).show();
            JSONObject params = new JSONObject();
            try {

                params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
                params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
                params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
                params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
                params.put("sheet_id", timesheetid);
                params.put("state", "draft");

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

        } else if (id == R.id.save) {
            Toast.makeText(getApplicationContext(), "Timesheet saved!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.submit) {
            Toast.makeText(getApplicationContext(), "Timesheet submit!", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == android.R.id.home) {

            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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
//            Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
//            ArrayList<AddDates> d = data.getParcelableArrayListExtra("data");

//            Toast.makeText(getActivity(), "" + position + "  " + data.getStringExtra("date"), Toast.LENGTH_SHORT).show();

            AddDates s = (AddDates) list.get(position);
//            s.getComments();
//            s.setComments("dzfsdfsd");

            s.setName(data.getStringExtra("des"));
            s.setUnit_amount(data.getStringExtra("hour"));
            s.setDate(data.getStringExtra("date"));
//            Toast.makeText(getActivity(), "" + s.getComments(), Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();

        } else if (requestCode == RESULT_CODEANOTHER) {

            if (data.getBooleanExtra("isAdded", false)) {
                list.add(new Projects(data.getStringExtra("project"), data.getStringExtra("billable")));
//              AddDates(timeStamp, "0 Hrs", "Description")
//                list.add(new AddDates(data.getStringExtra("date"), data.getStringExtra("hour"), data.getStringExtra("des")));
                mAdapter.notifyDataSetChanged();
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
//        }
    }
}
