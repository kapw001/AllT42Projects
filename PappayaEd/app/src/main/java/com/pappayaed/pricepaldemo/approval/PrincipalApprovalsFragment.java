package com.pappayaed.pricepaldemo.approval;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pappayaed.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalApprovalsFragment extends Fragment {


    private static final String TAG = "PrincipalApprovalsFragm";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;
    @BindView(R.id.button4)
    Button button4;

    private List<Approvals> mList;

    private ApprovalAdapter approvalAdapter;

    public PrincipalApprovalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approvals, container, false);
        unbinder = ButterKnife.bind(this, view);

        mList = getList();
        approvalAdapter = new ApprovalAdapter(mList, getContext(), getFragmentManager());

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(approvalAdapter);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private List<Approvals> getList() {

        List<Approvals> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Approvals approvals = new Approvals();
            approvals.setPurpose("Purpose " + i);
            approvals.setDescription("Fragments have a different view lifecycle than activities. When binding a fragment in onCreateView, set the views to null in onDestroyView. Butter Knife returns an Unbinder instance when you call bind to do this for you. Call its unbind method in the appropriate lifecycle callback.");
            approvals.setTime(i + ":10 AM");
            list.add(approvals);
        }

        return list;

    }

    @OnClick(R.id.button4)
    public void onTest() {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Log.e(TAG, "onTest: " + gson.toJson(mList));

    }
}
