package com.pappaya.prms.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pappaya.prms.R;
import com.pappaya.prms.utils.Cons;

import java.text.ParseException;

public class DateTestActivity extends AppCompatActivity {

    private static final String TAG = "DateTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_test);

//        Log.e(TAG, "onCreate: " + Cons.getS("yyyy-MM-dd", "2010-09-29 08:45:22"));
        try {
            Log.e(TAG, "onCreate: " + Cons.getS("EEEE", "2010-09-29 08:45:22"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
