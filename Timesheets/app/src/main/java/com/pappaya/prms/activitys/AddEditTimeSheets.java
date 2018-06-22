package com.pappaya.prms.activitys;

import android.app.TimePickerDialog;
import android.content.Context;
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
import com.pappaya.prms.model.ListProjects;
import com.pappaya.prms.sessionmanagement.SessionManager;
import com.pappaya.prms.utils.Utilitys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pappaya.prms.R;
import com.pappaya.prms.model.AddDates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddEditTimeSheets extends AppCompatActivity {

    private LinearLayout laycalendar, laytimer;
    private TextView calendaredit, timeredit;
    private EditText descriptionedit;

    private ArrayList<AddDates> list;
    private int position;

    private RadioButton radioButtonnon;
    private RadioButton radioButton;
    private RadioButton radioButtonGetValue;
    private RadioGroup radioGroup;

    private String currentdate, hour, des, billable;

    private String whichActivity;
    private Spinner projects;
    private ArrayList<ListProjects> listProjectses = new ArrayList<>();
    private CustomAdapter customAdapter;

    private SessionManager sessionManager;
    private static final String TAG = "AddEditTimeSheets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_time_sheets);
        sessionManager = new SessionManager(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//        list = getIntent().getParcelableArrayListExtra("data");
        position = getIntent().getIntExtra("position", 0);

//        Toast.makeText(AddEditTimeSheets.this, "" + position, Toast.LENGTH_SHORT).show();

        currentdate = getIntent().getStringExtra("date");
        hour = getIntent().getStringExtra("hour");
        des = getIntent().getStringExtra("des");
        billable = getIntent().getStringExtra("billable");
        whichActivity = getIntent().getStringExtra("activity");

//        Toast.makeText(AddEditTimeSheets.this, "" + billable, Toast.LENGTH_SHORT).show();

        laycalendar = (LinearLayout) findViewById(R.id.laycalendar);
        laytimer = (LinearLayout) findViewById(R.id.timerlay);
        projects = (Spinner) findViewById(R.id.project);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//        radioGroup.setSelected(true);

        radioButton = (RadioButton) findViewById(R.id.billable);
        radioButtonnon = (RadioButton) findViewById(R.id.nonbillable);

        if (billable.equalsIgnoreCase("true")) {
            radioButton.setChecked(true);
            radioButtonnon.setChecked(false);
        } else {
            radioButtonnon.setChecked(true);
            radioButton.setChecked(false);
        }


        radioButtonGetValue = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radioButtonGetValue = (RadioButton) findViewById(checkedId);
            }
        });

        calendaredit = (TextView) findViewById(R.id.calendaredit);
        timeredit = (TextView) findViewById(R.id.timeredit);
        descriptionedit = (EditText) findViewById(R.id.descriptionedit);

        calendaredit.setText(currentdate);
        timeredit.setText(hour);
        descriptionedit.setText(des);
        final String startDate = getIntent().getStringExtra("startdate");
        final String startEnd = getIntent().getStringExtra("enddate");

        laycalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utilitys.callCallender(calendaredit, AddEditTimeSheets.this);
                Utilitys.callCallenderReturn(currentdate, calendaredit, AddEditTimeSheets.this, new Utilitys.CallValue() {
                    @Override
                    public void value(String v) {
                        if (v != null) {

                            if (new Date(v).compareTo(new Date(startDate)) >= 0 && new Date(v).compareTo(new Date(startEnd)) <= 0) {
//                                Toast.makeText(AddNewProjectTimesheets.this, "OK", Toast.LENGTH_SHORT).show();
                                calendaredit.setText(v);

                            } else {
                                calendaredit.setText(currentdate);
                                Toast.makeText(AddEditTimeSheets.this, "Please select date from " + startDate + " to " + startEnd, Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            calendaredit.setText(currentdate);
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

        Utilitys.showProgressDialog(AddEditTimeSheets.this);
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
                Utilitys.hideProgressDialog(AddEditTimeSheets.this);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                Utilitys.hideProgressDialog(AddEditTimeSheets.this);
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
        TimePickerDialog mTimePicker = new TimePickerDialog(AddEditTimeSheets.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = selectedHour + "." + selectedMinute;
                if (Double.parseDouble(time) > 0) {
                    timeredit.setText(selectedHour + "." + selectedMinute);
                } else {
                    Toast.makeText(AddEditTimeSheets.this, "Invalid hours", Toast.LENGTH_SHORT).show();
                }
            }
        }, hour, minute, true);//Yes 24 hour time
//        mTimePicker.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mTimePicker.setTitle("Select Time");

        mTimePicker.show();
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

//            list.get(0).setDates(calendaredit.getText().toString());
//            list.get(0).setHours(timeredit.getText().toString());
//            list.get(0).setComments(descriptionedit.getText().toString());
            boolean test = false;
            if (whichActivity.equalsIgnoreCase("Weekly")) {
                List list = Weekly.checkTotal;
//                if (list.size() > 0) {
                ((AddDates) list.get(position)).setUnit_amount(timeredit.getText().toString());
                test = Utilitys.totalUnit(list, calendaredit.getText().toString(), "0");
//                }

            } else {
                List<Object> list = WeeklyActivity.checkTotal;
//                if (list.size() > 0) {
                ((AddDates) list.get(position)).setUnit_amount(timeredit.getText().toString());
                test = Utilitys.totalUnit(list, calendaredit.getText().toString(), "0");
//                }
            }

//            Toast.makeText(AddEditTimeSheets.this, "" + test, Toast.LENGTH_SHORT).show();


            if (test) {
                callResult();
            } else {
                Toast.makeText(AddEditTimeSheets.this, "Invalid time", Toast.LENGTH_SHORT).show();
            }


            return true;
        } else if (item.getItemId() == android.R.id.home) {
            callResultEmpty();
            finish(); // close this activity and return to preview activity (if there is any)
        }


        return super.onOptionsItemSelected(item);
    }


    private void callResult() {

        currentdate = calendaredit.getText().toString();
        hour = timeredit.getText().toString();
        des = descriptionedit.getText().toString();

        String billable = radioButtonGetValue.getText().toString().equalsIgnoreCase("Billable") ? "true" : "false";
        if (listProjectses.size() > 0) {

            final String project = listProjectses.get(projects.getSelectedItemPosition()).getProjectname();
            final String projectid = listProjectses.get(projects.getSelectedItemPosition()).getId();

            Intent intent = new Intent();
            intent.putExtra("back", "false");
            intent.putExtra("position", position);
            intent.putExtra("date", currentdate);
            intent.putExtra("hour", hour);
            intent.putExtra("des", des);
            intent.putExtra("project", project);
            intent.putExtra("projectid", projectid);
            intent.putExtra("billable", billable);
//            intent.putParcelableArrayListExtra("data", list);

            setResult(2, intent);
            finish();//finishing activity
        } else {
            Toast.makeText(AddEditTimeSheets.this, "No project available to create timesheet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        callResultEmpty();
        super.onBackPressed();

    }

    private void callResultEmpty() {
//
//        currentdate = calendaredit.getText().toString();
//        hour = timeredit.getText().toString();
//        des = descriptionedit.getText().toString();
//
//        String billable = radioButtonGetValue.getText().toString().equalsIgnoreCase("Billable") ? "true" : "false";


        Intent intent = new Intent();
        intent.putExtra("back", "true");

        setResult(2, intent);
        finish();//finishing activity

    }

}
