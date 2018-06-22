package com.pappayaed.pricepaldemo.approval.resondialog;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pappayaed.R;
import com.pappayaed.pricepaldemo.CustomButtonView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResonDialogFragment extends DialogFragment {


    @BindView(R.id.reason)
    EditText reason;
    @BindView(R.id.cancel)
    CustomButtonView cancel;
    @BindView(R.id.ok)
    CustomButtonView ok;
    Unbinder unbinder;

    public ResonDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reson_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.cancel)
    public void onCancel() {

//        cancel.setValueColor(Color.BLUE);

//        getDialog().dismiss();
    }

    @OnClick(R.id.ok)
    public void onOk() {
        getDialog().dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
