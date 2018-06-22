package com.pappaya.prms.activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pappaya.prms.R;
import com.pappaya.prms.sessionmanagement.SessionManager;

public class SplashScreen extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        setContentView(R.layout.activity_splash_screen);
//        Intent in = new Intent(SplashScreen.this, LoginActivity.class);
//        startActivity(in);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!sessionManager.isLoggedIn()) {
                    Intent in = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Intent in = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }

            }
        }, 2000);

    }
}
