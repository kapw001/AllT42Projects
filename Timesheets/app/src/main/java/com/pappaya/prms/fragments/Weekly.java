package com.pappaya.prms.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.pappaya.prms.interfaceses.Call;
import com.pappaya.prms.interfaceses.CallJsonRequest;
import com.pappaya.prms.model.Account_id;
import com.pappaya.prms.model.TimeSheet;
import com.pappaya.prms.model.TimeSheetActivitys;
import com.pappaya.prms.sessionmanagement.SessionManager;
import com.pappaya.prms.utils.DateComapare1;
import com.pappaya.prms.utils.DateComapare2;
import com.pappaya.prms.utils.JsonRequest;
import com.pappaya.prms.utils.Utilitys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.pappaya.prms.R;
import com.pappaya.prms.activitys.AddEditTimeSheets;
import com.pappaya.prms.activitys.AddNewProjectTimesheets;
import com.pappaya.prms.activitys.AskStartDateEndDate;
import com.pappaya.prms.activitys.WeeklyEditable;
import com.pappaya.prms.interfaceses.AddDeleteRow;
import com.pappaya.prms.interfaceses.CallFragment;
import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.Projects;
import com.pappaya.prms.recycle.ComplexRecyclerViewAdapter;

/**
 * Created by yasar on 25/11/16.
 */
public class Weekly extends Fragment implements AddDeleteRow {
    public static List<TimeSheet> movieList = new ArrayList<>();
    private static final String TAG = "Weekly";
    private RecyclerView recyclerView;
    private ComplexRecyclerViewAdapter mAdapter;
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

    ArrayList list = new ArrayList<>();

    private Call call;

    List<AddDates> addDatesList = new ArrayList<>();
    List<AddDates> addDatesListSubmit = new ArrayList<>();
    public static List checkTotal = new ArrayList<>();
    SessionManager sessionManager;
    String timesheetid;

    private CallJsonRequest callJsonRequest;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callFragment = (CallFragment) activity;
        call = (Call) activity;
        callJsonRequest = (CallJsonRequest) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.weekly, container, false);
        Intent in = new Intent(getActivity(), AskStartDateEndDate.class);
//        in.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) movieList);
        startActivityForResult(in, RESULT_CODEFIRST);

        sessionManager = new SessionManager(getActivity());

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewweekly);

        card_view = (CardView) v.findViewById(R.id.card_view);

        card_view.setVisibility(View.INVISIBLE);

        edit = (Button) v.findViewById(R.id.edit);
        submit = (Button) v.findViewById(R.id.submit);
        addprojects = (ImageView) v.findViewById(R.id.addprojects);

        startDate = (TextView) v.findViewById(R.id.startdate);
        totalhours = (TextView) v.findViewById(R.id.totalhours);
        endDate = (TextView) v.findViewById(R.id.enddate);


        mAdapter = new ComplexRecyclerViewAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        addprojects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTotal.clear();
                checkTotal.addAll(list);
                Intent in = new Intent(getActivity(), AddNewProjectTimesheets.class);
                in.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) movieList);
                in.putExtra("startdate", Utilitys.displayDateAnother(passStartDate));
                in.putExtra("enddate", Utilitys.displayDateAnother(passEndDate));
                in.putExtra("activity", "Weekly");
//                in.putExtra("list", (Parcelable) addDatesList);

                startActivityForResult(in, RESULT_CODEANOTHER);
            }
        });

        addprojects.setColorFilter(getResources().getColor(R.color.colorPrimary));
//        getSampleArrayList();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), WeeklyEditable.class);
                startActivity(in);
            }
        });

//        Calendar calendar = Calendar.getInstance();
//        String startdate = new SimpleDateFormat("MM/dd/yy").format(calendar.getTime());

//        startDate.setText(new SimpleDateFormat("MM/dd/yy").format(calendar.getTime()));
//        calendar.add(Calendar.DATE, 6);
//        endDate.setText(new SimpleDateFormat("MM/dd/yy").format(calendar.getTime()));


        return v;

    }

    public void setFetchDataList(List<TimeSheet> movieList) {
        this.movieList = movieList;

        Log.e(TAG, "setFetchDataList: " + movieList.size());
    }

    public void setFetchDataList1(List<TimeSheet> movieList) {
        this.movieList = movieList;

        Log.e(TAG, "setFetchDataList: " + movieList.size());


    }

    public void saveData() {
        if (addDatesList.size() > 0) {
            Utilitys.showProgressDialog(getActivity());
//            item.setVisible(false);

            JSONArray jsonArray = new JSONArray();


            for (int i = 0; i < addDatesList.size(); i++) {

                addDatesList.get(i).setDate(Utilitys.converDateServer(addDatesList.get(i).getDate()));

                JSONObject jsonObject = new JSONObject();
                AddDates addDates = addDatesList.get(i);
                try {
                    jsonObject.put("date", addDates.getDate());
                    jsonObject.put("account_id", addDates.getAccount_id());
                    jsonObject.put("is_timesheet", addDates.is_timesheet());
                    jsonObject.put("is_billable", Boolean.parseBoolean(addDates.getIs_billable()));
                    if (addDates.getStatus().equalsIgnoreCase("refused") || addDates.getStatus().equalsIgnoreCase("draft") || addDates.getStatus().equalsIgnoreCase("open") || addDates.getStatus().equalsIgnoreCase("Open")) {
                        jsonObject.put("status", "draft");
                    } else {
                        jsonObject.put("status", addDates.getStatus());
                    }
                    jsonObject.put("line_id", Integer.parseInt(addDates.getLine_id()));
                    jsonObject.put("name", addDates.getName());
                    jsonObject.put("unit_amount", Double.parseDouble(addDates.getUnit_amount()));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(addDatesList).getAsJsonArray();
            Log.i(TAG, "onOptionsItemSelected: Gson " + myCustomArray);
            JSONObject params = new JSONObject();
            try {

                params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
                params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
                params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
                params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
                params.put("sheet_id", Integer.parseInt(timesheetid));

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


            JsonRequest.makeRequest(getActivity(), jsonObject, "update", new JsonRequest.RequestCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.e(TAG, "onSuccess: SaveData Response" + response);


                    try {

                        JSONObject jsonObject = response.getJSONObject("result");

                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            list.clear();
                            addDatesList.clear();
                            addDatesListSubmit.clear();

                            JSONObject mytimesheetapprove = jsonObject.getJSONObject("detail");

                            JSONObject my_time_sheetobjectapprove = mytimesheetapprove;
                            JSONArray activitiesapprove = my_time_sheetobjectapprove.getJSONArray("activities");

                            for (int i = 0; i < activitiesapprove.length(); i++) {
                                JSONObject activitiesjsonObject = activitiesapprove.getJSONObject(i);
                                JSONObject account_idjsonObject = activitiesjsonObject.getJSONObject("account_id");
                                addDatesListSubmit.add(new AddDates(activitiesjsonObject.getString("date"), activitiesjsonObject.getString("unit_amount"), activitiesjsonObject.getString("display_name"), activitiesjsonObject.getString("id"), account_idjsonObject.getString("id"), account_idjsonObject.getString("name"), activitiesjsonObject.getString("status"), activitiesjsonObject.getString("is_billable")));

                            }


                            addDatesList.clear();
                            list.clear();
                            Collections.sort(addDatesListSubmit, new DateComapare2());
                            list.addAll(addDatesListSubmit);
                            addDatesList.addAll(addDatesListSubmit);
                            for (int i = 0; i < addDatesList.size(); i++) {
                                addDatesList.get(i).setDate(Utilitys.converDate(addDatesList.get(i).getDate()));
                            }
                            try {
                                Toast.makeText(getActivity(), "Timesheet saved successfully", Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {
                                Log.e(TAG, "onSuccess: " + e);
                            }

                            mAdapter.notifyDataSetChanged();
                            Utilitys.hideProgressDialog(getActivity());

                            callJsonRequest.onCallRequest();

                        }


//                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utilitys.hideProgressDialog(getActivity());

                        callJsonRequest.onCallRequest();
                    }


                }

                @Override
                public void OnFail(VolleyError error) {
                    Log.e(TAG, "OnFail: " + error);
                    Utilitys.hideProgressDialog(getActivity());
                }
            }, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/");


            for (int i = 0; i < addDatesList.size(); i++) {

                addDatesList.get(i).setDate(Utilitys.converDate(addDatesList.get(i).getDate()));
            }


        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        menu.getItem(0).setVisible(false);
        inflater.inflate(R.menu.weeklymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            saveData();
//            item.setVisible(false);

        } else if (id == R.id.submit) {

            if (addDatesListSubmit.size() > 0) {
                Utilitys.showProgressDialog(getActivity());
                JSONArray jsonArray = new JSONArray();

//            Log.e(TAG, "onOptionsItemSelected: " + addDatesList.toString());

                for (int i = 0; i < addDatesListSubmit.size(); i++) {


//                    addDatesListSubmit.get(i).setDate(Utilitys.converDateServer(addDatesListSubmit.get(i).getDate()));

                    JSONObject jsonObject = new JSONObject();
                    AddDates addDates = addDatesListSubmit.get(i);
                    try {
                        jsonObject.put("date", addDates.getDate());
                        jsonObject.put("account_id", addDates.getAccount_id());
                        jsonObject.put("is_timesheet", addDates.is_timesheet());
                        jsonObject.put("is_billable", Boolean.parseBoolean(addDates.getIs_billable()));
                        if (addDates.getStatus().equalsIgnoreCase("refused") || addDates.getStatus().equalsIgnoreCase("draft") || addDates.getStatus().equalsIgnoreCase("open") || addDates.getStatus().equalsIgnoreCase("Open")) {
                            jsonObject.put("status", "pending");
                        } else {
                            jsonObject.put("status", addDates.getStatus());
                        }
                        jsonObject.put("line_id", Integer.parseInt(addDates.getLine_id()));
                        jsonObject.put("name", addDates.getName());
                        jsonObject.put("unit_amount", Double.parseDouble(addDates.getUnit_amount()));
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                JSONObject params = new JSONObject();
                try {

                    params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
                    params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
                    params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
                    params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
                    params.put("sheet_id", Integer.parseInt(timesheetid));

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

                JsonRequest.makeRequest(getActivity(), jsonObject, "update", new JsonRequest.RequestCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        Log.e(TAG, "onSuccess:Update Timesheet   " + response);

                        Utilitys.hideProgressDialog(getActivity());
                        try {
                            Toast.makeText(getActivity(), "Timesheet submitted successfully", Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onSuccess: " + e);
                        }


//                        JSONObject params = new JSONObject();
//                        try {
//
//                            params.put("db", sessionManager.getUserDetails().get(SessionManager.KEY_URL));
//                            params.put("login", sessionManager.getUserDetails().get(SessionManager.KEY_NAME));
//                            params.put("password", sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD));
//                            params.put("hostname", sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL));
//                            params.put("sheet_id", timesheetid);
////                            params.put("state", "confirm");
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        JSONObject jsonObject = new JSONObject();
//
//
//                        try {
//                            jsonObject.put("jsonrpc", "2.0");
//                            jsonObject.put("method", "call");
//                            jsonObject.put("id", "2");
//                            jsonObject.put("params", params);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        JsonRequest.makeRequest(getActivity(), jsonObject, "update_status", new JsonRequest.RequestCallback() {
//                            @Override
//                            public void onSuccess(JSONObject response) {
//
//                            }
//
//                            @Override
//                            public void OnFail(VolleyError error) {
//                                Log.e(TAG, "OnFail: " + error);
//                            }
//                        }, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/");

                        callJsonRequest.onCallRequest();
//                    getActivity().finish();
                        callFragment.callFragment(R.id.callbackid);
                    }

                    @Override
                    public void OnFail(VolleyError error) {
                        Log.e(TAG, "OnFail: " + error);
                        Utilitys.hideProgressDialog(getActivity());
                    }
                }, "https://" + sessionManager.getUserDetails().get(SessionManager.KEY_URL) + "." + sessionManager.getUserDetails().get(SessionManager.KEY_HOSTURL) + "/mobile/");

            } else {
                try {
                    Toast.makeText(getActivity(), "Please save timesheet before submit", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    Log.e(TAG, "onSuccess: " + e);
                }

                Utilitys.hideProgressDialog(getActivity());
            }
        }
//        else if (id == R.id.addtimesheet) {
//            getActivity().getFragmentManager().popBackStack();
//        }

        return super.onOptionsItemSelected(item);
    }

    private String passStartDate;
    private String passEndDate;

    private void getSampleArrayList(String datefrom, String dateto, String billable, String projects, String projectid, String sheetid) {
        passStartDate = datefrom;
        passEndDate = dateto;
        timesheetid = sheetid;

        startDate.setText(Utilitys.displayDateAnother(datefrom));
        endDate.setText(Utilitys.displayDateAnother(dateto));

        Calendar c = Calendar.getInstance();

        String newDatefrom = datefrom;


//        ArrayList<Object> items = new ArrayList<>();

//        list.add(new Projects(projects, billable, projectid, sheetid));


        Date s = new Date(newDatefrom);
        Date e = new Date(dateto);
        Calendar nc = Calendar.getInstance();
        nc.setTime(e);
        nc.add(Calendar.DATE, 1);
        String dgg = new SimpleDateFormat("MM/dd/yy").format(nc.getTime());
        e = new Date(dgg);

        c.setTime(s);

        double total_hours = 0.0;

        while (s.before(e)) {
            String timeStamp = new SimpleDateFormat("MM/dd/yy").format(c.getTime());
            list.add(new AddDates(timeStamp, "8", "", "0", projectid, projects, billable));
            addDatesList.add(new AddDates(timeStamp, "8", "", "0", projectid, projects, "draft", billable));
            c.add(Calendar.DATE, 1);
            String d = new SimpleDateFormat("MM/dd/yy").format(c.getTime());
            s = new Date(d);


        }

        for (int i = 0; i < addDatesList.size(); i++) {
            total_hours += Double.parseDouble(addDatesList.get(i).getUnit_amount());
        }
        totalhours.setText("Total Hours: " + Utilitys.round(total_hours, 2));
        mAdapter.notifyDataSetChanged();
        setHasOptionsMenu(true);
        card_view.setVisibility(View.VISIBLE);

    }

    @Override
    public void delete(int position) {

        if (addDatesList.size() > 0) {

//            List<Object> linesIdList=list;
            AddDates addDatesLineId = (AddDates) list.get(position);
            String lineid = addDatesLineId.getLine_id();

            if (addDatesList.get(position).getLine_id().equalsIgnoreCase("0")) {
                addDatesList.remove(position);
            } else {
                for (int i = 0; i < addDatesList.size(); i++) {
                    if (lineid.equalsIgnoreCase(addDatesList.get(i).getLine_id())) {
                        addDatesList.get(i).setIs_timesheet(false);
                        Log.e(TAG, "delete: " + addDatesList.get(position).is_timesheet() + " line id :" + addDatesList.get(position).getLine_id());
                    }
                }

            }
            list.remove(position);
            double total_hours = 0.0;
            for (int i = 0; i < list.size(); i++) {
                AddDates addDates = (AddDates) list.get(i);
                total_hours += Double.parseDouble(addDates.getUnit_amount());
            }


            totalhours.setText("Total Hours: " + Utilitys.round(total_hours, 2));


            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void addorEdit(int position) {
        int pos = position;

        if (list.get(position) instanceof AddDates) {
            AddDates data = (AddDates) list.get(position);

            checkTotal.clear();
            checkTotal.addAll(list);
            Intent intent = new Intent(getActivity(), AddEditTimeSheets.class);

            intent.putExtra("position", pos);
            intent.putExtra("date", data.getDate());
            intent.putExtra("hour", data.getUnit_amount());
            intent.putExtra("des", data.getName());
            intent.putExtra("billable", data.getIs_billable());
            intent.putExtra("startdate", passStartDate);
            intent.putExtra("enddate", passEndDate);
            intent.putExtra("activity", "Weekly");
            startActivityForResult(intent, RESULT_CODE);
        }


    }

    @Override
    public void addNewProjects() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == Activity.RESULT_OK) {

        if (requestCode == RESULT_CODE) {
            if (data.getStringExtra("back").equalsIgnoreCase("false")) {
                int position = data.getIntExtra("position", 0);

                AddDates s = (AddDates) list.get(position);
                AddDates s1 = (AddDates) addDatesList.get(position);


                s.setName(data.getStringExtra("des"));
                s.setUnit_amount(data.getStringExtra("hour"));
                s.setDate(data.getStringExtra("date"));
                s.setIs_billable(data.getStringExtra("billable"));
                s.setAccount_id(data.getStringExtra("projectid"));
                s.setProject(data.getStringExtra("project"));

                s1.setName(data.getStringExtra("des"));
                s1.setUnit_amount(data.getStringExtra("hour"));
                s1.setDate(data.getStringExtra("date"));
                s1.setIs_billable(data.getStringExtra("billable"));
                s1.setAccount_id(data.getStringExtra("projectid"));
                s1.setProject(data.getStringExtra("project"));

                double total_hours = 0.0;
                for (int i = 0; i < list.size(); i++) {
                    AddDates addDates = (AddDates) list.get(i);
                    total_hours += Double.parseDouble(addDates.getUnit_amount());
                }


                totalhours.setText("Total Hours: " + Utilitys.round(total_hours, 2));

                mAdapter.notifyDataSetChanged();
            }

        } else if (requestCode == RESULT_CODEANOTHER) {

            if (data.getBooleanExtra("isAdded", false)) {
//                list.add(new Projects(data.getStringExtra("project"), data.getStringExtra("billable")));
//              AddDates(timeStamp, "0 Hrs", "Description")
                list.add(new AddDates(data.getStringExtra("date"), data.getStringExtra("hour"), data.getStringExtra("des"), "0", data.getStringExtra("projectid"), data.getStringExtra("project"), data.getStringExtra("billable")));
                addDatesList.add(new AddDates(data.getStringExtra("date"), data.getStringExtra("hour"), data.getStringExtra("des"), "0", data.getStringExtra("projectid"), data.getStringExtra("project"), "draft", data.getStringExtra("billable")));
                mAdapter.notifyDataSetChanged();

                double total_hours = 0.0;
                for (int i = 0; i < list.size(); i++) {
                    AddDates addDates = (AddDates) list.get(i);
                    total_hours += Double.parseDouble(addDates.getUnit_amount());
                }


                totalhours.setText("Total Hours: " + Utilitys.round(total_hours, 2));
                mAdapter.notifyDataSetChanged();
            }

        } else if (requestCode == RESULT_CODEFIRST) {
            if (data.getBooleanExtra("isAdded", false)) {
                getSampleArrayList(data.getStringExtra("datefrom"), data.getStringExtra("dateto"), data.getStringExtra("billable"), data.getStringExtra("project"), data.getStringExtra("projectid"), data.getStringExtra("sheetid"));
            } else {
                call.call(R.id.callbackid);
            }
        } else if (requestCode == 4) {

            call.call(R.id.callbackid);

        } else if (requestCode == 5) {

        }
//        }
    }
}
