package com.pappayaed.pricepaldemo.approval;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pappayaed.R;
import com.pappayaed.pricepaldemo.approval.resondialog.ResonDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yasar on 9/3/18.
 */

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.RowApproval> {

    private static final String TAG = "Test";
    private List<Approvals> mList;
    private Context mContext;
    private FragmentManager fragmentManager;

    public ApprovalAdapter(List<Approvals> mList, Context mContext, FragmentManager fragmentManager) {
        this.mList = mList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
    }

    public void updateList(List<Approvals> mList) {
        this.mList = new ArrayList<>();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RowApproval onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_approval, parent, false);

        return new RowApproval(view);
    }

    private int currentPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull final RowApproval holder, final int position) {

        final Approvals approvals = mList.get(position);

        holder.purpose.setText(approvals.getPurpose());
        holder.description.setText(approvals.getDescription());
        holder.txttime.setText(approvals.getTime());


        if (currentPosition == position && !approvals.isVisible()) {

            approvals.setVisible(true);

            holder.approvereject.setVisibility(View.VISIBLE);
            holder.arrow.setRotation(-90);

        } else {
            holder.arrow.setRotation(0);
            approvals.setVisible(false);
            holder.approvereject.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                currentPosition = position;
                notifyDataSetChanged();

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResonDialogFragment resonDialogFragment = new ResonDialogFragment();
                resonDialogFragment.show(fragmentManager, "dialog");


            }
        });

        Log.e(TAG, "onBindViewHolder: " + holder.approvereject.isShown());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class RowApproval extends RecyclerView.ViewHolder {

        @BindView(R.id.purpose)
        TextView purpose;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.time)
        TextView txttime;

        @BindView(R.id.approvereject)
        RelativeLayout approvereject;

        @BindView(R.id.approval)
        Button approval;

        @BindView(R.id.reject)
        Button reject;

        @BindView(R.id.arrow)
        ImageView arrow;

        public RowApproval(View itemView) {
            super(itemView);
            setIsRecyclable(false);
            ButterKnife.bind(this, itemView);
        }
    }


}
