package com.pappaya.prms.activitys;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pappaya.prms.model.ListProjects;
import com.pappaya.prms.model.TimeSheet;
import com.pappaya.prms.model.TimeSheetDetail;
import com.pappaya.prms.sessionmanagement.SessionManager;
import com.pappaya.prms.utils.Cons;
import com.pappaya.prms.utils.DateComapare;
import com.pappaya.prms.utils.JsonRequest;
import com.pappaya.prms.utils.Utilitys;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pappaya.prms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AskStartDateEndDate extends AppCompatActivity {

    private static final String TAG = "AskStartDateEndDate";

    private List<TimeSheet> movieList;
    private LinearLayout laycalendar, laycalendar1;
    private TextView calendareditdatefrom, calendareditdateto, timeredit;
    private EditText descriptionedit;

    private Spinner projects;

    private RadioButton radioButton;
    private RadioGroup radioGroup;

    private SessionManager sessionManager;

    private ArrayList<ListProjects> listProjectses = new ArrayList<>();

    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_start_date_end_date);

        sessionManager = new SessionManager(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        laycalendar = (LinearLayout) findViewById(R.id.laycalendar);
        laycalendar1 = (LinearLayout) findViewById(R.id.laycalendar1);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//        radioGroup.setSelected(true);
        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
            }
        });

        calendareditdatefrom = (TextView) findViewById(R.id.calendareditdatefrom);
        calendareditdateto = (TextView) findViewById(R.id.calendareditdateto);
        timeredit = (TextView) findViewById(R.id.timeredit);
        descriptionedit = (EditText) findViewById(R.id.descriptionedit);
        projects = (Spinner) findViewById(R.id.project);

        Calendar c = Calendar.getInstance();

        String startd = new SimpleDateFormat("MM/dd/yy").format(c.getTime());

        c.add(Calendar.DATE, 6);

        String endd = new SimpleDateFormat("MM/dd/yy").format(c.getTime());

        customAdapter = new CustomAdapter(this, listProjectses);

        projects.setAdapter(customAdapter);

        calendareditdatefrom.setText(startd);
        calendareditdateto.setText(endd);

        laycalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilitys.callCallenderReturn(calendareditdatefrom.getText().toString(), calendareditdatefrom, AskStartDateEndDate.this, new Utilitys.CallValue() {
                    @Override
                    public void value(String v) {
                        if (v != null) {
                            calendareditdatefrom.setText(v);
                            laycalendar.setBackgroundResource(R.drawable.rounded_edittext_states);
                            addSevenDays(Utilitys.getDateA(calendareditdatefrom.getText().toString()));
                        } else {
                            if (calendareditdatefrom.getText().length() > 0) {
                                laycalendar.setBackgroundResource(R.drawable.rounded_edittext_states);
                            } else {
                                laycalendar.setBackgroundResource(R.drawable.rounded_edittext_stateserror);
                            }

                        }

                    }
                });
            }
        });

        laycalendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utilitys.callCallender(calendareditdateto, AskStartDateEndDate.this);
                Utilitys.callCallenderReturn(calendareditdateto.getText().toString(), calendareditdateto, AskStartDateEndDate.this, new Utilitys.CallValue() {
                    @Override
                    public void value(String v) {
                        if (v != null) {
                            calendareditdateto.setText(v);
                            laycalendar1.setBackgroundResource(R.drawable.rounded_edittext_states);
                        } else {
                            if (calendareditdatefrom.getText().length() > 0) {
                                laycalendar.setBackgroundResource(R.drawable.rounded_edittext_states);
                            } else {
                                laycalendar.setBackgroundResource(R.drawable.rounded_edittext_stateserror);
                            }
                        }

                    }
                });
            }
        });
        JSONObject params = new JSONObject();
        try {
            params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
            params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
            params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
            params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL).replace("/mobile/", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utilitys.showProgressDialog(AskStartDateEndDate.this);
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
                Utilitys.hideProgressDialog(AskStartDateEndDate.this);

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


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                Utilitys.hideProgressDialog(AskStartDateEndDate.this);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);


//        movieList = getIntent().getParcelableArrayListExtra("list");

//        Log.e(TAG, "onCreate: " + movieList.size() + " its working");
    }

    public void addSevenDays(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);

        c.add(Calendar.DATE, 6);

        String getDate = new SimpleDateFormat("MM/dd/yy").format(c.getTime());

        calendareditdateto.setText(getDate);
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
            try {
                callResult(true);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return true;
        } else if (item.getItemId() == android.R.id.home) {
            callBack(false);
        }


        return super.onOptionsItemSelected(item);
    }

    private void callResult(final boolean isAdded) throws ParseException {

        final String datefrom = calendareditdatefrom.getText().toString();
        final String dateto = calendareditdateto.getText().toString();
        final String billable = radioButton.getText().toString().equalsIgnoreCase("Billable") ? "true" : "false";
        ;
        Utilitys.showProgressDialog(AskStartDateEndDate.this);
        if (listProjectses.size() > 0) {
            final String project = listProjectses.get(projects.getSelectedItemPosition()).getProjectname();
            final String projectid = listProjectses.get(projects.getSelectedItemPosition()).getId();

//        Log.e(TAG, "callResult: " + MainActivity.movieList.toString());
//
//        Log.e(TAG, "callResult: " + MainActivity.movieList.get(0).getTimeSheetDetailslist().get(0).getDate_from());


            Date from_date = Utilitys.getDateA(datefrom);
            Date to_date = Utilitys.getDateA(dateto);
            Log.e(TAG, "callResult: " + from_date);

            if (!datefrom.equalsIgnoreCase("") && !dateto.equalsIgnoreCase("") && !project.equalsIgnoreCase("")) {
                if (getDifferenceDays(new Date(datefrom), new Date(dateto)) >= 6) {
                    boolean isTimeSheetAvailable = false;
                    for (int i = 0; i < MainActivity.movieList.size(); i++) {

                        TimeSheetDetail timeSheetDetail = MainActivity.movieList.get(i).getTimeSheetDetailslist().get(0);

                        Log.e(TAG, "callResult: " + timeSheetDetail.getDate_from() + "   " + timeSheetDetail.getDate_to());
                        Date from_time_date = Utilitys.stringToDateUsingFormater(timeSheetDetail.getDate_from());
                        Date to_time_date = Utilitys.stringToDateUsingFormater(timeSheetDetail.getDate_to());
                        if (!from_date.before(from_time_date) && !from_date.after(to_time_date) || !to_date.before(from_time_date) && !to_date.after(to_time_date)) {
                            isTimeSheetAvailable = true;
                        }

                    }
                    if (!isTimeSheetAvailable) {

                        JSONObject params = new JSONObject();
                        try {

                            params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
                            params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
                            params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
                            params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
                            params.put("name", "Week");
                            params.put("from_date", Utilitys.converDateServer(datefrom));
                            params.put("to_date", Utilitys.converDateServer(dateto));
                            params.put("lines", new JSONObject());
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

                        Log.e(TAG, "callResult: Post Data:    " + jsonObject);

                        JsonRequest.makeRequest(this, jsonObject, "create", new JsonRequest.RequestCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {

                                Log.e(TAG, "onSuccess: " + response);
                                Utilitys.hideProgressDialog(AskStartDateEndDate.this);
                                try {


                                    JSONObject result = response.getJSONObject("result");

                                    String sheetid = result.getString("sheet_id");
                                    Intent intent = new Intent();
                                    intent.putExtra("isAdded", isAdded);
                                    intent.putExtra("datefrom", datefrom);
                                    intent.putExtra("dateto", dateto);
                                    intent.putExtra("billable", billable);
                                    intent.putExtra("project", project);
                                    intent.putExtra("projectid", projectid);
                                    intent.putExtra("sheetid", sheetid);
//                      intent.putParcelableArrayListExtra("data", list);

                                    setResult(1, intent);
                                    finish();//finishing activity


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void OnFail(VolleyError error) {
                                Log.e(TAG, "OnFail: " + error);
                                Utilitys.hideProgressDialog(AskStartDateEndDate.this);
                            }
                        }, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/");


                    } else {
                        Utilitys.hideProgressDialog(AskStartDateEndDate.this);
                        Toast.makeText(AskStartDateEndDate.this, "Timesheet can not be overlapped", Toast.LENGTH_SHORT).show();
//                        Utilitys.hideProgressDialog(AskStartDateEndDate.this);
                    }


                } else {
                    Toast.makeText(AskStartDateEndDate.this, "Please choose at least a week", Toast.LENGTH_SHORT).show();
                    Utilitys.hideProgressDialog(AskStartDateEndDate.this);
                }


            } else {
                laycalendar.setBackgroundResource(R.drawable.rounded_edittext_stateserror);
                laycalendar1.setBackgroundResource(R.drawable.rounded_edittext_stateserror);
                Toast.makeText(AskStartDateEndDate.this, "Fill the Form", Toast.LENGTH_SHORT).show();
                Utilitys.hideProgressDialog(AskStartDateEndDate.this);
            }
        } else {
            Utilitys.hideProgressDialog(AskStartDateEndDate.this);
            Toast.makeText(AskStartDateEndDate.this, "No available project to create timesheet", Toast.LENGTH_SHORT).show();
        }

    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onBackPressed() {
        callBack(false);
        super.onBackPressed();

    }

    private void callBack(boolean isAdded) {
        Intent intent = new Intent();

        intent.putExtra("isAdded", false);


        setResult(1, intent);
//        finish();//finishing activity
        finish(); // close this activity and return to preview activity (if there is any)
    }

    private void callBack() {
        Intent intent = new Intent();

        setResult(4, intent);
//        finish();//finishing activity
        finish(); // close this activity and return to preview activity (if there is any)
    }
}
