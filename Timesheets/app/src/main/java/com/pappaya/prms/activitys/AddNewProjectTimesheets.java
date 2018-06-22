package com.pappaya.prms.activitys;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pappaya.prms.fragments.Weekly;
import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.ListProjects;
import com.pappaya.prms.model.TimeSheet;
import com.pappaya.prms.sessionmanagement.SessionManager;
import com.pappaya.prms.utils.Cons;
import com.pappaya.prms.utils.Utilitys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pappaya.prms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddNewProjectTimesheets extends AppCompatActivity {
    private static final String TAG = "AddNewProjectTimesheets";
    private List<TimeSheet> movieList = new ArrayList<>();
    private LinearLayout laycalendar, laytimer;
    private TextView calendaredit, timeredit;
    private EditText descriptionedit;

    private Spinner projects;

    private RadioButton radioButton;
    private RadioGroup radioGroup;

    private SessionManager sessionManager;

    private ArrayList<ListProjects> listProjectses = new ArrayList<>();

    private ArrayList<AddDates> addDatesTotalCountList;

    private CustomAdapter customAdapter;

    private String whichActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_project_timesheets);

        sessionManager = new SessionManager(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        laycalendar = (LinearLayout) findViewById(R.id.laycalendar);
        laytimer = (LinearLayout) findViewById(R.id.timerlay);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//        radioGroup.setSelected(true);
        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
            }
        });

        calendaredit = (TextView) findViewById(R.id.calendaredit);
        timeredit = (TextView) findViewById(R.id.timeredit);
        descriptionedit = (EditText) findViewById(R.id.descriptionedit);
        projects = (Spinner) findViewById(R.id.project);

        final String startDate = getIntent().getStringExtra("startdate");
        final String startEnd = getIntent().getStringExtra("enddate");
        whichActivity = getIntent().getStringExtra("activity");
        addDatesTotalCountList = getIntent().getParcelableArrayListExtra("list");


//        Toast.makeText(AddNewProjectTimesheets.this, "" + Utilitys.convertDate(startDate), Toast.LENGTH_SHORT).show();


        laycalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utilitys.callCallender(calendaredit, AddNewProjectTimesheets.this);
                Utilitys.callCallenderReturn(startDate, calendaredit, AddNewProjectTimesheets.this, new Utilitys.CallValue() {
                    @Override
                    public void value(String v) {
                        if (v != null) {

//                            Calendar s = Utilitys.convertStringToCalendar(startDate, "MM/dd/YY");
//                            Calendar d = Utilitys.convertStringToCalendar(startEnd, "MM/dd/YY");
//                            Calendar vv = Utilitys.convertStringToCalendar(v, "MM/dd/YY");

                            if (new Date(v).compareTo(new Date(startDate)) >= 0 && new Date(v).compareTo(new Date(startEnd)) <= 0) {
//                                Toast.makeText(AddNewProjectTimesheets.this, "OK", Toast.LENGTH_SHORT).show();
                                calendaredit.setText(v);
                                laycalendar.setBackgroundResource(R.drawable.rounded_edittext_states);
                            } else {
                                laycalendar.setBackgroundResource(R.drawable.rounded_edittext_stateserror);
                                Toast.makeText(AddNewProjectTimesheets.this, "Please select date from " + startDate + " to " + startEnd, Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            laycalendar.setBackgroundResource(R.drawable.rounded_edittext_stateserror);
                        }

                    }
                });
            }
        });

        laytimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTimer();
            }
        });

        customAdapter = new CustomAdapter(this, listProjectses);

        projects.setAdapter(customAdapter);

        JSONObject params = new JSONObject();
        try {
            params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
            params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
            params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
            params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utilitys.showProgressDialog(AddNewProjectTimesheets.this);
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("jsonrpc", "2.0");
            jsonObject.put("method", "call");
            jsonObject.put("id", "2");
            jsonObject.put("params", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/" + "project_list",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response.toString());

                try {


                    JSONObject result = response.getJSONObject("result");

                    JSONArray jsonArray = result.getJSONArray("ids");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        listProjectses.add(new ListProjects(jsonObject1.getString("id"), jsonObject1.getString("name")));

                    }
                    customAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utilitys.hideProgressDialog(AddNewProjectTimesheets.this);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                Utilitys.hideProgressDialog(AddNewProjectTimesheets.this);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);

    }

    class CustomAdapter extends BaseAdapter {
        Context context;

        LayoutInflater inflter;
        ArrayList<ListProjects> listProjectses;


        public CustomAdapter(Context applicationContext, ArrayList<ListProjects> listProjectses) {
            this.context = applicationContext;
            this.listProjectses = listProjectses;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return listProjectses.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.custom_spinner_items, null);
            TextView names = (TextView) view.findViewById(R.id.textView);
            names.setText(listProjectses.get(i).getProjectname());
            return view;
        }
    }

    private void callTimer() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(AddNewProjectTimesheets.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = selectedHour + "." + selectedMinute;
                if (Double.parseDouble(time) > 0) {
                    timeredit.setText(selectedHour + "." + selectedMinute);
                    laytimer.setBackgroundResource(R.drawable.rounded_edittext_states);
                } else {
                    Toast.makeText(AddNewProjectTimesheets.this, "Invalid hours", Toast.LENGTH_SHORT).show();
                }

            }


        }, hour, minute, true);//Yes 24 hour time
//        mTimePicker.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mTimePicker.setTitle("Select Time");

        mTimePicker.show();

        mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                laytimer.setBackgroundResource(R.drawable.rounded_edittext_stateserror);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addedit, menu);
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

//            boolean test;
//            if (whichActivity.equalsIgnoreCase("Weekly")) {
//                List<Object> list = Weekly.checkTotal;
//                test = Utilitys.totalUnit(list, calendaredit.getText().toString(), timeredit.getText().toString());
//            } else {
//                List<Object> list = WeeklyActivity.checkTotal;
//                test = Utilitys.totalUnit(list, calendaredit.getText().toString(), timeredit.getText().toString());
//            }
//
//            if (test) {
            callResult(true);
//            } else {
//                Toast.makeText(AddNewProjectTimesheets.this, "Invalid errors", Toast.LENGTH_SHORT).show();
//            }


//            Toast.makeText(AddNewProjectTimesheets.this, "" + Utilitys.totalUnit(WeeklyActivity.checkTotal, calendaredit.getText().toString(),timeredit.getText().toString()), Toast.LENGTH_SHORT).show();

            return true;
        } else if (item.getItemId() == android.R.id.home) {
            callback(false);
//            finish(); // close this activity and return to preview activity (if there is any)
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        callback();
        super.onBackPressed();

    }

    private void callResult(boolean isAdded) {

        String currentdate = calendaredit.getText().toString();
        String hour = timeredit.getText().toString();
        String des = descriptionedit.getText().toString();
        String billable = radioButton.getText().toString().equalsIgnoreCase("Billable") ? "true" : "false";

        if (listProjectses.size() > 0) {

            final String project = listProjectses.get(projects.getSelectedItemPosition()).getProjectname();
            final String projectid = listProjectses.get(projects.getSelectedItemPosition()).getId();

            if (!currentdate.equalsIgnoreCase("") && !hour.equalsIgnoreCase("") && !project.equalsIgnoreCase("")) {
//        if (!currentdate.equalsIgnoreCase("") && !hour.equalsIgnoreCase("")) {

                boolean test;
                if (whichActivity.equalsIgnoreCase("Weekly")) {
                    List list = Weekly.checkTotal;
                    test = Utilitys.totalUnit(list, calendaredit.getText().toString(), timeredit.getText().toString());
                } else {
                    List<Object> list = WeeklyActivity.checkTotal;
                    test = Utilitys.totalUnit(list, calendaredit.getText().toString(), timeredit.getText().toString());
                }

                if (test) {
                    Intent intent = new Intent();

                    intent.putExtra("isAdded", isAdded);
                    intent.putExtra("date", currentdate);
                    intent.putExtra("hour", hour);
                    intent.putExtra("des", des);
                    intent.putExtra("billable", billable);
                    intent.putExtra("project", project);
                    intent.putExtra("projectid", projectid);
//            intent.putParcelableArrayListExtra("data", list);

                    setResult(3, intent);
                    finish();//finishing activity
                } else {
                    Toast.makeText(AddNewProjectTimesheets.this, "Invalid time", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(AddNewProjectTimesheets.this, "Fill the form", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddNewProjectTimesheets.this, "No project available to create timesheet", Toast.LENGTH_SHORT).show();
        }

    }

    private void callback(boolean isAdded) {
        Intent intent = new Intent();

        intent.putExtra("isAdded", false);
        setResult(3, intent);
        finish();//finishing activity
    }

    private void callback() {
        Intent intent = new Intent();

        setResult(5, intent);
        finish();//finishing activity
    }
}
