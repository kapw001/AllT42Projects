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

import java.util.ArrayList;
import java.util.List;

import com.pappaya.prms.R;
import com.pappaya.prms.interfaceses.CallFragment;
import com.pappaya.prms.model.TimeSheet;
import com.pappaya.prms.recycle.MyTimesApproveRecyclerView;

/**
 * Created by yasar on 25/11/16.
 */
public class TimeSheetsToApprove extends Fragment {

    private List<TimeSheet> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyTimesApproveRecyclerView mAdapter;
    private CallFragment callFragment;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callFragment = (CallFragment) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mytimesheetstoapprove, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewtimesheets);

        mAdapter = new MyTimesApproveRecyclerView(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareMovieData();


        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment.callFragment(R.id.addnewproject);
            }
        });

        fab.setColorFilter(getResources().getColor(R.color.colorPrimary));

        return v;

    }

    private void prepareMovieData() {
//        TimesheetsModel movie = new TimesheetsModel("Senthil", "10/11/16", "10/17/16", "4 Hrs", "Waiting for approval", "Java");
//        movieList.add(movie);
//
//        movie = new TimesheetsModel("Yasar", "11/11/16", "11/20/16", "10 Hrs", "Waiting for approval", "C++");
//        movieList.add(movie);
//
//        movie = new TimesheetsModel("Thirumal", "11/01/16", "11/07/16", "6 Hrs", "Approved", ".Net");
//        movieList.add(movie);
////
////        movie = new TimesheetsModel("Think42labs", "10/11/2016", "17/11/2016", "Hours:     5 hrs", "Open");
////        movieList.add(movie);
//
//        movie = new TimesheetsModel("Saravanan", "10/20/16", "10/27/16", "2 Hrs", "Rejected", "Odoo");
//        movieList.add(movie);
//
//
//        mAdapter.notifyDataSetChanged();
    }

    public void setFetchDataList(List<TimeSheet> movieList) {
        this.movieList = movieList;

        mAdapter.notifyDataSetChanged();
    }

    public void setFetchDataList1(List<TimeSheet> movieList) {
        this.movieList = movieList;

    }
}
