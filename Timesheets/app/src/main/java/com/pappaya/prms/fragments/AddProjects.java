package com.pappaya.prms.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pappaya.prms.interfaceses.Call;

import com.pappaya.prms.R;
import com.pappaya.prms.model.TimeSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasar on 6/12/16.
 */
public class AddProjects extends Fragment {
    private List<TimeSheet> movieList = new ArrayList<>();
    private Button addproject;

    private Call callFragment;

    private static final String TAG = "AddProjects";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        callFragment = (Call) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_projects, container, false);

        addproject = (Button) v.findViewById(R.id.addnewproject);

        addproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment.call(R.id.addnewproject);
            }
        });

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
}
