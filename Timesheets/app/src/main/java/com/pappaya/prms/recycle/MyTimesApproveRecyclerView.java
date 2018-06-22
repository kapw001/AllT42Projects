package com.pappaya.prms.recycle;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pappaya.prms.activitys.ApproveRejectActicity;
import com.pappaya.prms.activitys.WeeklyActivity;
import com.pappaya.prms.model.Account_id;
import com.pappaya.prms.model.Account_ids;
import com.pappaya.prms.model.TimeSheet;
import com.pappaya.prms.model.TimeSheetActivitys;
import com.pappaya.prms.utils.Utilitys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pappaya.prms.R;

import com.pappaya.prms.model.TimeSheetDetail;

/**
 * Created by yasar on 25/11/16.
 */
public class MyTimesApproveRecyclerView extends RecyclerView.Adapter<MyTimesApproveRecyclerView.MyViewHolder> {

    private List<TimeSheet> timesheetsModelsList;


    public MyTimesApproveRecyclerView(List<TimeSheet> timesheetsModelsList) {
        this.timesheetsModelsList = timesheetsModelsList;
    }


    @Override
    public MyTimesApproveRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_timesheetstoapprove, parent, false);

        return new MyViewHolder(view);
    }

    private static final String TAG = "MyTimesApproveRecyclerView";

    @Override
    public void onBindViewHolder(MyTimesApproveRecyclerView.MyViewHolder holder, int position) {

        if (!timesheetsModelsList.isEmpty()) {
            final List<TimeSheetDetail> timesheetsModel = timesheetsModelsList.get(position).getTimeSheetDetailslist();
            for (int i = 0; i < timesheetsModel.size(); i++) {
                final TimeSheetDetail m = timesheetsModel.get(i);
//                Log.e(TAG, "onBindViewHolder: " + m.getDate_from() + "  Test  " + m.getTotal_timesheet());


                holder.startdate.setText(Utilitys.displayDate(m.getDate_from()));
                holder.enddate.setText(Utilitys.displayDate(m.getDate_to()));
                holder.hours.setText(m.getTotal_timesheet());

                holder.employeename.setText(m.getEmployee_id().getName());

//                ArrayList<Account_ids> account_idsArrayList = m.getAccount_ids().getAccount_idsArrayList();

                ArrayList<TimeSheetActivitys> listproject = timesheetsModelsList.get(position).getTimeSheetActivityseslist();

                Set<String> hs = new HashSet<>();

                for (int j = 0; j < listproject.size(); j++) {
                    Account_id account_id = listproject.get(j).getAccount_id();
                    hs.add(account_id.getName());
                }


                String projects = "";
                for (String projectname : hs
                        ) {
                    projects += projectname + ", ";
                }
                String latcharremove = "";
                if (projects.length() > 0) {
                    latcharremove = projects.substring(0, projects.length() - 2);
                }


                holder.project.setText(latcharremove);
//        holder.status.setText(timesheetsModel.getStatus());
                if (m.getState().toLowerCase().equalsIgnoreCase("done")) {
                    holder.txtstatus.setText("Approved");
                    holder.status.setBackgroundResource(R.drawable.approved);
                } else if (m.getState().equalsIgnoreCase("draft")) {
                    holder.txtstatus.setText("Open");
                    holder.status.setBackgroundResource(R.drawable.openicon);
                } else if (m.getState().equalsIgnoreCase("confirm")) {
                    holder.txtstatus.setText("Waiting Approval");
                    holder.status.setBackgroundResource(R.drawable.waitingforapproval);
                } else if (m.getState().equalsIgnoreCase("new")) {
                    holder.txtstatus.setText("New");
                } else if (m.getState().equalsIgnoreCase("rejected")) {
//            drawable.setColor(Color.RED);
                    holder.txtstatus.setText("Rejected");
                    holder.status.setBackgroundResource(R.drawable.rejected);

                }

                final String proj = projects;
                final String formattedDatefrom = Utilitys.displayDate(m.getDate_from());
                final String formattedDateto = Utilitys.displayDate(m.getDate_to());

                final ArrayList<TimeSheetActivitys> openActivity = timesheetsModelsList.get(position).getTimeSheetActivityseslist();
                final String sheetid = timesheetsModel.get(i).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        if (m.getState().equalsIgnoreCase("confirm")) {
                        Intent in = new Intent(view.getContext(), ApproveRejectActicity.class);
                        in.putParcelableArrayListExtra("open", openActivity);
                        in.putExtra("startdate", formattedDatefrom);
                        in.putExtra("enddate", formattedDateto);
                        in.putExtra("hours", m.getTotal_timesheet());
                        in.putExtra("project", proj);
                        in.putExtra("sheetid", sheetid);
                        in.putExtra("name", m.getEmployee_id().getName());
                        in.putExtra("billable", "");
                        in.putExtra("menuenable", true);
                        in.putExtra("updatetimesheet", false);
                        view.getContext().startActivity(in);
//                        } else {
//                            Intent in = new Intent(view.getContext(), WeeklyActivity.class);
//                            in.putParcelableArrayListExtra("open", openActivity);
//                            in.putExtra("startdate", formattedDatefrom);
//                            in.putExtra("enddate", formattedDateto);
//                            in.putExtra("hours", m.getTotal_timesheet());
//                            in.putExtra("project", proj);
//                            in.putExtra("sheetid", sheetid);
//                            in.putExtra("billable", "");
//                            in.putExtra("menuenable", true);
//                            in.putExtra("updatetimesheet", true);
//                            view.getContext().startActivity(in);
//
//
//                        }


                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return timesheetsModelsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView startdate, enddate, hours, project, employeename, txtstatus;

        public ImageView imgtime, imgprojects, status, empicon;

        public MyViewHolder(View view) {
            super(view);

            employeename = (TextView) view.findViewById(R.id.employeename);
            startdate = (TextView) view.findViewById(R.id.startdate);
            enddate = (TextView) view.findViewById(R.id.enddate);
            project = (TextView) view.findViewById(R.id.project);
            hours = (TextView) view.findViewById(R.id.txttimer);
            imgtime = (ImageView) view.findViewById(R.id.imgtime);
            status = (ImageView) view.findViewById(R.id.status);
            txtstatus = (TextView) view.findViewById(R.id.txtstatus);

            imgprojects = (ImageView) view.findViewById(R.id.imgproject);
            empicon = (ImageView) view.findViewById(R.id.empicon);
//            imgtime.setColorFilter(view.getResources().getColor(R.color.colorPrimary));
//            imgprojects.setColorFilter(view.getResources().getColor(R.color.colorPrimary));
//            empicon.setColorFilter(view.getResources().getColor(R.color.colorPrimary));
        }
    }


}
