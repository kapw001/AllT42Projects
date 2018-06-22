package com.pappaya.prms.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pappaya.prms.R;
import com.pappaya.prms.model.Account_id;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.pappaya.prms.interfaceses.CallFragment;
import com.pappaya.prms.model.Account_ids;
import com.pappaya.prms.model.TimeSheet;
import com.pappaya.prms.model.TimeSheetActivitys;
import com.pappaya.prms.model.TimeSheetDetail;
import com.pappaya.prms.model.User_id;
import com.pappaya.prms.recycle.MyTimesRecyclerView;
import com.pappaya.prms.sessionmanagement.SessionManager;

/**
 * Created by yasar on 25/11/16.
 */
public class MyTimesheets extends Fragment {
    private static final String TAG = "MyTimesheets";
    private List<TimeSheet> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyTimesRecyclerView mAdapter;
    private CallFragment callFragment;

    private SessionManager sessionManager;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callFragment = (CallFragment) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mytimesheets, container, false);
        sessionManager = new SessionManager(getActivity());
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewtimesheets);
        prepareMovieData();
        mAdapter = new MyTimesRecyclerView(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment.callFragment(R.id.addnewproject);
            }
        });

        fab.setColorFilter(getResources().getColor(R.color.colorPrimary));

//        getMyTimeSheet();


        return v;

    }

    private void prepareMovieData() {
//        TimesheetsModel movie = new TimesheetsModel("11/10/16", "11/17/16", "24 Hrs", "Open", "Think42labs");
//        movieList.add(movie);
//
//        movie = new TimesheetsModel("11/15/16", "11/22/16", "10 Hrs", "Waiting for approval", "C++");
//        movieList.add(movie);
//
//        movie = new TimesheetsModel("11/01/16", "11/07/16", "6 Hrs", "Approved", "IOS");
//        movieList.add(movie);
//
//        movie = new TimesheetsModel("11/10/16", "11/17/16", "5 hrs", "Open", "Android");
//        movieList.add(movie);
//
//        movie = new TimesheetsModel("10/20/16", "11/27/16", "2 hrs", "Rejected", "Java");
//        movieList.add(movie);


//        mAdapter.notifyDataSetChanged();
    }

    public void setFetchDataList(List<TimeSheet> movieList) {
        this.movieList = movieList;

        mAdapter.notifyDataSetChanged();
    }

    public void setFetchDataList1(List<TimeSheet> movieList) {
        this.movieList = movieList;

    }


    private void getMyTimeSheet() {
//        final ArrayList<TimeSheet> list = new ArrayList<>();

//        String u = "http://192.168.1.23:8069/mobile/timesheet?user=" + sessionManager.getUserDetails().get(SessionManager.KEY_NAME) + "&passw=" + sessionManager.getUserDetails().get(SessionManager.KEY_PASSWORD);
        String u = "http://192.168.0.135:8069/en_GB/mobile/timesheet?user=karthik@think42labs.com&passw=test";
        Toast.makeText(getActivity(), "Call", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray mytimesheet = jsonObject.getJSONArray("my_time_sheet");

                            JSONObject my_time_sheetobject = mytimesheet.getJSONObject(0);

                            TimeSheet timeSheet = new TimeSheet();

                            JSONArray activities = my_time_sheetobject.getJSONArray("activities");

                            ArrayList<TimeSheetActivitys> timeSheetActivityseslist = new ArrayList<>();

                            for (int i = 0; i < activities.length(); i++) {
                                JSONObject activitiesjsonObject = activities.getJSONObject(i);
                                TimeSheetActivitys timeSheetActivitys = new TimeSheetActivitys();
                                timeSheetActivitys.setDisplay_name(activitiesjsonObject.getString("display_name"));
                                timeSheetActivitys.setCdate(activitiesjsonObject.getString("date"));
                                timeSheetActivitys.setId(activitiesjsonObject.getString("id"));
                                timeSheetActivitys.setName(activitiesjsonObject.getString("name"));

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
                            timeSheetDetail.setDate_from(detail.getString("date_from"));
                            timeSheetDetail.setDate_to(detail.getString("date_to"));

                            JSONObject userid = detail.getJSONObject("user_id");
                            timeSheetDetail.setUser_id(new User_id(userid.getString("id"), userid.getString("name")));

                            JSONArray account_ids = detail.getJSONArray("account_ids");

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
                            mAdapter.notifyDataSetChanged();

//
//                            for (int i = 0; i < movieList.size(); i++) {
////                                Account_id account_id = timeSheetActivityseslist.get(i).getAccount_id();
////                                Log.e(TAG, "onResponse: " + account_id.getId() + "     " + account_id.getName());
//
////                                Account_ids accountIds = movieList.get(i).getTimeSheetDetailslist().get(0).getAccount_ids();
////
////                                Log.e(TAG, "onResponse: " + accountIds.toString() + "  " + accountIds.getName() + " " + accountIds.getAccount_idsArrayList());
//
//
//                            }

//                            Log.e(TAG, "onResponse: " + mytimesheet.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        ProgressUtil.hideProgressbar();
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}
