package com.pappaya.prms.activitys;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pappaya.prms.recycle.ComplexRecyclerViewAdapterEditable;
import com.pappaya.prms.utils.Utilitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.pappaya.prms.R;
import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.Projects;

/**
 * Created by yasar on 25/11/16.
 */
public class WeeklyEditable extends AppCompatActivity {

    private static final String TAG = "WeeklyEditable";

    private List<Object> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ComplexRecyclerViewAdapterEditable mAdapter;

    private Button save, cancel;

    private ImageView addprojects;

    private EditText startdate, enddate;
    public static String myFormat = "MM/dd/yy"; //In which you need put here
    public static SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    private LinearLayout savecancellayout;

    private LinearLayoutManager mLayoutManager;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeklyeditable);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        startdate = (EditText) findViewById(R.id.startdate);
        enddate = (EditText) findViewById(R.id.enddate);

        savecancellayout = (LinearLayout) findViewById(R.id.savecancellayout);

        enddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Utilitys.callCallender(enddate, WeeklyEditable.this);
//                    DialogFragment datePickerFragment = new DatePickerFragment() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int day) {
//                            Log.d(TAG, "onDateSet");
//                            Calendar c = Calendar.getInstance();
//                            c.set(year, month, day);
//                            enddate.setText(sdf.format(c.getTime()));
//
//
//                        }
//                    };
//                    datePickerFragment.show(getFragmentManager(), "datePicker");
                }
            }
        });

        startdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Utilitys.callCallender(startdate, WeeklyEditable.this);
//                    DialogFragment datePickerFragment = new DatePickerFragment() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int day) {
//                            Log.d(TAG, "onDateSet");
//                            Calendar c = Calendar.getInstance();
//                            c.set(year, month, day);
//                            startdate.setText(sdf.format(c.getTime()));
////                            enddate.requestFocus();
//
//                        }
//                    };
//                    datePickerFragment.show(getFragmentManager(), "datePicker");
                }
            }
        });


        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        addprojects = (ImageView) findViewById(R.id.addprojects);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewweekly);

        mAdapter = new ComplexRecyclerViewAdapterEditable(this, getSampleArrayList());
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        recyclerView.addOnScrollListener(mScrollListener);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView,
//                                   int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                visibleItemCount = mLayoutManager.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//
//                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                    //bottom of recyclerview
//                    savecancellayout.setVisibility(View.VISIBLE);
//                } else {
//                    savecancellayout.setVisibility(View.GONE);
//                }
//
//                Log.e(TAG, "onScrolled: " + pastVisiblesItems);
//            }
//        });


        addprojects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(WeeklyEditable.this, AddNewProjectTimesheets.class);
                startActivity(in);
            }
        });
        addprojects.setColorFilter(getResources().getColor(R.color.colorPrimary));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
////                if (recyclerView.getAdapter().getItemCount() != 0) {
//            super.onScrolled(recyclerView, dx, dy);
//            int visibleItemCount = mLayoutManager.getChildCount();
//            int totalItemCount = mLayoutManager.getItemCount();
//            int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
//
//            if (pastVisibleItems + visibleItemCount >= totalItemCount) {
//                //End of list
//                savecancellayout.setVisibility(View.VISIBLE);
//            } else {
//                savecancellayout.setVisibility(View.GONE);
//            }
////                }
//        }
//    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editablemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Object> getSampleArrayList() {
        ArrayList<Object> items = new ArrayList<>();
        items.add(new Projects("Projects 1", "Billable"));
//        items.add(new AddDates("10/11/16", "6 hrs", "Comments"));
//        items.add(new AddDates("10/11/16", "8 hrs", "Comments"));
//        items.add(new AddDates("10/11/16", "2 hrs", "Comments"));
//        items.add(new AddDates("10/11/16", "4 hrs", "Comments"));
//        items.add(new AddDates("10/11/16", "6 hrs", "Comments"));
//        items.add(new AddDates("10/11/16", "8 hrs", "Comments"));


        return items;
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //blah
        }
    }
}
