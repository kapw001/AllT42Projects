package com.pappaya.prms.recycle;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import com.pappaya.prms.R;
import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.Projects;

/**
 * Created by yasar on 25/11/16.
 */
public class ComplexRecyclerViewAdapterEditable extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;

    private final int PROJECTS = 0, ADDDATES = 1;

    private Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapterEditable(Activity activity, List<Object> items) {
        this.items = items;
        this.activity = activity;
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
            return ADDDATES;
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
                View v1 = inflater.inflate(R.layout.row_projectseditale, viewGroup, false);
                viewHolder = new ViewHolderProjects(v1);
                break;
            case ADDDATES:
                View v2 = inflater.inflate(R.layout.row_adddateseditable, viewGroup, false);
                viewHolder = new ViewHolderAddDates(v2);
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
                configureViewHolderAddDates(vh2, position);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                break;
        }
    }


    private void configureViewHolderProjects(ViewHolderProjects vh1, int position) {
        Projects projects = (Projects) items.get(position);
        if (projects != null) {
//            vh1.getProjects().setText("Project : " + projects.getProjectName());
//            vh1.getBillable().setText(projects.getDescription());
        }
    }

    private void configureViewHolderAddDates(ViewHolderAddDates vh1, int position) {
        AddDates addDates = (AddDates) items.get(position);
        if (addDates != null) {
//            vh1.getComments().setText(addDates.getComments());
            vh1.getDates().setText(addDates.getDate());
//            vh1.getHours().setText(addDates.getHours());
        }
    }


    public class ViewHolderProjects extends RecyclerView.ViewHolder {

        private Spinner projects;
        private Spinner billable;

        public ViewHolderProjects(View v) {
            super(v);
            projects = (Spinner) v.findViewById(R.id.projectsspinner);
            billable = (Spinner) v.findViewById(R.id.billable);

//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), R.layout.spinner_row, v.getContext().getResources().getStringArray(R.array.project));
//            projects.setAdapter(adapter);
        }

        public Spinner getProjects() {
            return projects;
        }

        public void setProjects(Spinner projects) {
            this.projects = projects;
        }

        public Spinner getBillable() {
            return billable;
        }

        public void setBillable(Spinner billable) {
            this.billable = billable;
        }
    }

    public class ViewHolderAddDates extends RecyclerView.ViewHolder {

        private EditText comments;
        private TextView dates;
        private Spinner hours;

        public ViewHolderAddDates(View v) {
            super(v);
            dates = (TextView) v.findViewById(R.id.dates);
            hours = (Spinner) v.findViewById(R.id.hours);
            comments = (EditText) v.findViewById(R.id.comments);
//            dates.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
//                        Utilitys.callCallender(dates, activity);
////                        DialogFragment datePickerFragment = new WeeklyEditable.DatePickerFragment() {
////                            @Override
////                            public void onDateSet(DatePicker view, int year, int month, int day) {
////
////                                Calendar c = Calendar.getInstance();
////                                c.set(year, month, day);
////                                dates.setText(WeeklyEditable.sdf.format(c.getTime()));
////
////
////                            }
////                        };
////                        datePickerFragment.show(fragmentManager, "datePicker");
//                    }
//                }
//            });
        }

        public TextView getDates() {
            return dates;
        }

        public void setDates(EditText dates) {
            this.dates = dates;
        }

        public Spinner getHours() {
            return hours;
        }

        public void setHours(Spinner hours) {
            this.hours = hours;
        }

        public TextView getComments() {
            return comments;
        }

        public void setComments(EditText comments) {
            this.comments = comments;
        }
    }

    private class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewSimpleTextViewHolder(View itemView) {
            super(itemView);
        }
    }
}