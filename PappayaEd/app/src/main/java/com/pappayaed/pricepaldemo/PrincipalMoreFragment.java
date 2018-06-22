package com.pappayaed.pricepaldemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pappayaed.R;
import com.pappayaed.pricepaldemo.appointment.AppointmentsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalMoreFragment extends Fragment {


    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.principal_Name)
    TextView principalName;
    @BindView(R.id.appointments)
    RelativeLayout appointments;
    @BindView(R.id.examid)
    View examid;
    @BindView(R.id.logout)
    RelativeLayout logout;
    Unbinder unbinder;

    public PrincipalMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_more, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.appointments)
    public void onAppointmentClick() {
        startActivity(new Intent(getContext(), AppointmentsActivity.class));

    }

    @OnClick(R.id.logout)
    public void onLogoutClick() {

        startActivity(new Intent(getContext(), AppointmentsActivity.class));

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
