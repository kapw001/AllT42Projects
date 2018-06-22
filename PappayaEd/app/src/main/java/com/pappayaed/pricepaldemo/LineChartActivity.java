package com.pappayaed.pricepaldemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.pappayaed.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LineChartActivity extends AppCompatActivity {

    private LineChartAndroid lineChartAndroid;
    private Timer timer;
    private static final String TAG = "LineChartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChartAndroid = (LineChartAndroid) findViewById(R.id.myline);


        Calendar c = Calendar.getInstance();

        Log.e(TAG, "onCreate: " + c.getTime());

        ArrayList<String> labels = new ArrayList<>();
        labels.add("2014");
        labels.add("2015");
        labels.add("2016");
        labels.add("2017");

        Float[] f2 = {60.0f, 37.5f, 45.2f, 78.0f};

        lineChartAndroid.setEntry(f2, "10Th", Color.parseColor("#9AFF92"));

        Float[] f3 = {68.0f, 57.5f, 45.2f, 80.0f};

        lineChartAndroid.setEntry(f3, "12th", Color.parseColor("#1E3264"));

        Float[] f4 = {18.0f, 77.5f, 25.2f, 90.0f};

        lineChartAndroid.setEntry(f4, "12th", Color.parseColor("#FF5722"));

        lineChartAndroid.showLineChart(labels, true);

        timer = new Timer();


        final List<String> list = new ArrayList<>();

        list.add("05:25 PM");
        list.add("05:27 PM");
        list.add("05:34 PM");
        list.add("05:36 PM");
        list.add("05:38 PM");


        final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        final long ONE_MINUTE_IN_MILLIS = 60000;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Date date = new Date();

                String cTime = formatter.format(date);

                Date cDate = null;
                try {
                    cDate = formatter.parse(cTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                for (int i = 0; i < list.size(); i++) {


                    Date gDate = null;
                    try {
                        gDate = formatter.parse(list.get(i));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(gDate);

                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(cDate);

//                    long t = c1.getTime();
//                    Date afterAddingTenMins = new Date(t - (1 * ONE_MINUTE_IN_MILLIS));


//                LocalTime t1 = LocalTime.of(10, 10, 0);
//                LocalTime t2 = LocalTime.of(11, 11, 0);
//                    int result = c2.getTime().after(c1.getTime());
//                    if (result < 0) {
////                        System.out.println("Second");
//                    } else if (result > 0) {
//                        System.out.println("First" + "    " + list.get(i));
//                    } else {
////                        System.out.println("Same Time");
//                    }

                    if (c2.getTime().before(c1.getTime())) {
                        Log.e(TAG, "run: " + c2.getTime() + "     " + c1.getTime());
                    }

                }


            }
        }, 100, 1000);


    }
}
