package com.pappaya.prms.recycle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pappaya.prms.R;
import com.pappaya.prms.interfaceses.AddDeleteRow;
import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.Projects;

//import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
//import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

/**
 * Created by yasar on 25/11/16.
 */
public class ComplexRecyclerViewAdapterWeeklyActivity extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;

    private final int PROJECTS = 0, ADDDATES = 1, ADDDATES2 = 2;

    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    private AddDeleteRow addDeleteRow;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapterWeeklyActivity(List<Object> items, Activity weekly) {
        this.items = items;
        addDeleteRow = (AddDeleteRow) weekly;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Projects) {
            return PROJECTS;
        } else if (items.get(position) instanceof AddDates) {
            AddDates addDates = (AddDates) items.get(position);
            if (addDates.getStatus().equalsIgnoreCase("draft") || addDates.getStatus().equalsIgnoreCase("refused") || addDates.getStatus().equalsIgnoreCase("")) {
                return ADDDATES;
            } else {
                return ADDDATES2;
            }

        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case PROJECTS:
                View v1 = inflater.inflate(R.layout.row_projects, viewGroup, false);
                viewHolder = new ViewHolderProjects(v1);
                break;
            case ADDDATES:
                View v2 = inflater.inflate(R.layout.newdateeditanother, viewGroup, false);
                viewHolder = new ViewHolderAddDates(v2);
                break;
            case ADDDATES2:
                View v22 = inflater.inflate(R.layout.newdateeditanothernoedit, viewGroup, false);
                viewHolder = new ViewHolderAddDates2(v22);
                break;
            default:
                View v3 = inflater.inflate(R.layout.row_default, viewGroup, false);
                viewHolder = new ViewHolderAddDates(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //More to come
        switch (viewHolder.getItemViewType()) {
            case PROJECTS:
                ViewHolderProjects vh1 = (ViewHolderProjects) viewHolder;
                configureViewHolderProjects(vh1, position);
                break;
            case ADDDATES:


                ViewHolderAddDates vh2 = (ViewHolderAddDates) viewHolder;
//                Object data=items.get(position);
//
//                binderHelper.bind(vh2.swipeLayout, data);
                binderHelper.bind(vh2.swipeLayout, items.get(position).toString());
                binderHelper.setOpenOnlyOne(true);

                configureViewHolderAddDates(vh2, position);
                break;
            case ADDDATES2:


                ViewHolderAddDates2 vh22 = (ViewHolderAddDates2) viewHolder;
//                Object data=items.get(position);
//
//                binderHelper.bind(vh2.swipeLayout, data);
//                binderHelper.bind(vh22.swipeLayout, items.get(position).toString());
//                binderHelper.setOpenOnlyOne(true);

                configureViewHolderAddDates(vh22, position);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                break;
        }
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    private void configureViewHolderProjects(ViewHolderProjects vh1, int position) {
        Projects projects = (Projects) items.get(position);
        if (projects != null) {
            vh1.getProjects().setText(projects.getProjectName());
            vh1.getBillable().setText(projects.getDescription());
        }
    }

    private void configureViewHolderAddDates(ViewHolderAddDates vh1, int position) {
        AddDates addDates = (AddDates) items.get(position);
        if (addDates != null) {
            vh1.getTxtcomments().setText(addDates.getName());
            vh1.getTxttimer().setText(addDates.getUnit_amount());
            vh1.getProject().setText(addDates.getProject());

//            String date = addDates.getDates();
            Date date = new Date(addDates.getDate());
//            String timeStamp = new SimpleDateFormat("E").format(date);

            vh1.getDateinname().setText(new SimpleDateFormat("EEEE").format(date));
            vh1.getCurrendatedate().setText(new SimpleDateFormat("d").format(date));
            vh1.getCurrentdateinyear().setText(new SimpleDateFormat("MMM, yyyy").format(date));

            vh1.getStatus().setText(addDates.getStatus());

            if (addDates.getIs_billable().equalsIgnoreCase("true")) {
                vh1.getIsbillable().setText("Billable");
            } else {
                vh1.getIsbillable().setText("Non-Billable");
            }


        }
    }

    private void configureViewHolderAddDates(ViewHolderAddDates2 vh1, int position) {
        AddDates addDates = (AddDates) items.get(position);
        if (addDates != null) {
            vh1.getTxtcomments().setText(addDates.getName());
            vh1.getTxttimer().setText(addDates.getUnit_amount());
            vh1.getProject().setText(addDates.getProject());

//            String date = addDates.getDates();
            Date date = new Date(addDates.getDate());
//            String timeStamp = new SimpleDateFormat("E").format(date);

            vh1.getDateinname().setText(new SimpleDateFormat("EEEE").format(date));
            vh1.getCurrendatedate().setText(new SimpleDateFormat("d").format(date));
            vh1.getCurrentdateinyear().setText(new SimpleDateFormat("MMM, yyyy").format(date));

            vh1.getStatus().setText(addDates.getStatus());

            if (addDates.getIs_billable().equalsIgnoreCase("true")) {
                vh1.getIsbillable().setText("Billable");
            } else {
                vh1.getIsbillable().setText("Non-Billable");
            }


        }
    }


    public class ViewHolderProjects extends RecyclerView.ViewHolder {

        private TextView projects, billable;

        public ViewHolderProjects(View v) {
            super(v);
            projects = (TextView) v.findViewById(R.id.projects);
            billable = (TextView) v.findViewById(R.id.billable);
        }

        public TextView getProjects() {
            return projects;
        }

        public void setProjects(TextView projects) {
            this.projects = projects;
        }

        public TextView getBillable() {
            return billable;
        }

        public void setBillable(TextView billable) {
            this.billable = billable;
        }
    }

    public class ViewHolderAddDates extends RecyclerView.ViewHolder {

        //        private TextView dates, hours, comments;
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        private TextView txtcomments, txttimer, dateinname, currendatedate, currentdateinyear, project;
        private EditText editcomments;
        private TextView status, isbillable;
        private Button approve, reject;
        private LinearLayout timepickerlayout;

        private SwipeRevealLayout swipeLayout;

        private LinearLayout bottom_wrapper;

        private RelativeLayout edit, delete;

        private ImageView imageViewEdit, imageViewDelete;

        public ViewHolderAddDates(final View v) {
            super(v);
//            dates = (TextView) v.findViewById(R.id.dates);
//            hours = (TextView) v.findViewById(R.id.hours);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe);
//            bottom_wrapper = (LinearLayout) itemView.findViewById(R.id.bottom_wrapper);
            imageViewEdit = (ImageView) itemView.findViewById(R.id.imageedit);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.imagedelete);

            imageViewEdit.setColorFilter(itemView.getResources().getColor(R.color.colorPrimary));
            imageViewDelete.setColorFilter(itemView.getResources().getColor(R.color.red));

            edit = (RelativeLayout) itemView.findViewById(R.id.tvEdit);
            delete = (RelativeLayout) itemView.findViewById(R.id.tvDelete);
            txtcomments = (TextView) v.findViewById(R.id.txtcomments);
            txttimer = (TextView) v.findViewById(R.id.txttimer);
            project = (TextView) v.findViewById(R.id.project);
            status = (TextView) v.findViewById(R.id.status);
            isbillable = (TextView) v.findViewById(R.id.isbillable);

            approve = (Button) v.findViewById(R.id.approve);
            reject = (Button) v.findViewById(R.id.reject);

            dateinname = (TextView) v.findViewById(R.id.dateinname);
            currendatedate = (TextView) v.findViewById(R.id.currendatedate);
            currentdateinyear = (TextView) v.findViewById(R.id.currentdateinyear);

            timepickerlayout = (LinearLayout) v.findViewById(R.id.timepickerlayout);


            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(v.getContext(), "Edit", Toast.LENGTH_SHORT).show();

                    addDeleteRow.addorEdit(getPosition());

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(v.getContext(), "Delete", Toast.LENGTH_SHORT).show();
//                    items.remove(getPosition());
                    addDeleteRow.delete(getPosition());


                }
            });


        }

        public TextView getIsbillable() {
            return isbillable;
        }

        public void setIsbillable(TextView isbillable) {
            this.isbillable = isbillable;
        }

        public TextView getStatus() {
            return status;
        }

        public void setStatus(TextView status) {
            this.status = status;
        }

        public Button getApprove() {
            return approve;
        }

        public void setApprove(Button approve) {
            this.approve = approve;
        }

        public Button getReject() {
            return reject;
        }

        public void setReject(Button reject) {
            this.reject = reject;
        }

        public TextView getProject() {
            return project;
        }

        public void setProject(TextView project) {
            this.project = project;
        }

        public TextView getTxtcomments() {
            return txtcomments;
        }

        public void setTxtcomments(TextView txtcomments) {
            this.txtcomments = txtcomments;
        }

        public TextView getTxttimer() {
            return txttimer;
        }

        public void setTxttimer(TextView txttimer) {
            this.txttimer = txttimer;
        }

        public TextView getDateinname() {
            return dateinname;
        }

        public void setDateinname(TextView dateinname) {
            this.dateinname = dateinname;
        }

        public TextView getCurrendatedate() {
            return currendatedate;
        }

        public void setCurrendatedate(TextView currendatedate) {
            this.currendatedate = currendatedate;
        }

        public EditText getEditcomments() {
            return editcomments;
        }

        public void setEditcomments(EditText editcomments) {
            this.editcomments = editcomments;
        }

        public TextView getCurrentdateinyear() {
            return currentdateinyear;
        }

        public void setCurrentdateinyear(TextView currentdateinyear) {
            this.currentdateinyear = currentdateinyear;
        }


    }

    public class ViewHolderAddDates2 extends RecyclerView.ViewHolder {

        //        private TextView dates, hours, comments;
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        private TextView txtcomments, txttimer, dateinname, currendatedate, currentdateinyear, project;
        private EditText editcomments;
        private LinearLayout timepickerlayout;

        private SwipeRevealLayout swipeLayout;

        private LinearLayout bottom_wrapper;
        private TextView status, isbillable;
        private Button approve, reject;

        private RelativeLayout edit, delete;

        private ImageView imageViewEdit, imageViewDelete;

        public ViewHolderAddDates2(final View v) {
            super(v);

            txtcomments = (TextView) v.findViewById(R.id.txtcomments);
            txttimer = (TextView) v.findViewById(R.id.txttimer);
            project = (TextView) v.findViewById(R.id.project);

            dateinname = (TextView) v.findViewById(R.id.dateinname);
            currendatedate = (TextView) v.findViewById(R.id.currendatedate);
            currentdateinyear = (TextView) v.findViewById(R.id.currentdateinyear);
            status = (TextView) v.findViewById(R.id.status);
            isbillable = (TextView) v.findViewById(R.id.isbillable);

            approve = (Button) v.findViewById(R.id.approve);
            reject = (Button) v.findViewById(R.id.reject);

            timepickerlayout = (LinearLayout) v.findViewById(R.id.timepickerlayout);

        }

        public TextView getStatus() {
            return status;
        }

        public void setStatus(TextView status) {
            this.status = status;
        }

        public TextView getIsbillable() {
            return isbillable;
        }

        public void setIsbillable(TextView isbillable) {
            this.isbillable = isbillable;
        }

        public Button getApprove() {
            return approve;
        }

        public void setApprove(Button approve) {
            this.approve = approve;
        }

        public Button getReject() {
            return reject;
        }

        public void setReject(Button reject) {
            this.reject = reject;
        }

        public TextView getProject() {
            return project;
        }

        public void setProject(TextView project) {
            this.project = project;
        }

        public TextView getTxtcomments() {
            return txtcomments;
        }

        public void setTxtcomments(TextView txtcomments) {
            this.txtcomments = txtcomments;
        }

        public TextView getTxttimer() {
            return txttimer;
        }

        public void setTxttimer(TextView txttimer) {
            this.txttimer = txttimer;
        }

        public TextView getDateinname() {
            return dateinname;
        }

        public void setDateinname(TextView dateinname) {
            this.dateinname = dateinname;
        }

        public TextView getCurrendatedate() {
            return currendatedate;
        }

        public void setCurrendatedate(TextView currendatedate) {
            this.currendatedate = currendatedate;
        }

        public EditText getEditcomments() {
            return editcomments;
        }

        public void setEditcomments(EditText editcomments) {
            this.editcomments = editcomments;
        }

        public TextView getCurrentdateinyear() {
            return currentdateinyear;
        }

        public void setCurrentdateinyear(TextView currentdateinyear) {
            this.currentdateinyear = currentdateinyear;
        }


    }

    private class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewSimpleTextViewHolder(View itemView) {
            super(itemView);
        }
    }


}