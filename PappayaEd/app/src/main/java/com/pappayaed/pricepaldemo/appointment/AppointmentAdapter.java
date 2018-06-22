package com.pappayaed.pricepaldemo.appointment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pappayaed.R;
import com.pappayaed.pricepaldemo.approval.Approvals;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yasar on 9/3/18.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.RowApproval> {

    private static final String TAG = "Test";
    private List<Appointments> mList;
    private Context mContext;

    public AppointmentAdapter(List<Appointments> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void updateList(List<Appointments> mList) {
        this.mList = new ArrayList<>();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RowApproval onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_appointment, parent, false);

        return new RowApproval(view);
    }

    private int currentPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull final RowApproval holder, final int position) {

        final Appointments appointments = mList.get(position);

        holder.txttime.setText(appointments.getTime());
        holder.txtdes.setText(appointments.getDes());

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class RowApproval extends RecyclerView.ViewHolder {


        @BindView(R.id.txtdes)
        TextView txtdes;
        @BindView(R.id.txttime)
        TextView txttime;


        public RowApproval(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
