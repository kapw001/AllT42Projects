package com.pappayaed.pricepaldemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.pappayaed.R;
import com.pappayaed.fragmentnavigation.FragmentNavigationManager;
import com.pappayaed.fragmentnavigation.NavigationManager;
import com.pappayaed.preference.SessionManagenent;

public class PricipalBottomTapActivity extends AppCompatActivity {

    private NavigationManager navigationManager;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    addFragment(1);
                    return true;
                case R.id.navigation_approvals:
                    addFragment(3);
                    return true;
                case R.id.navigation_more:
                    addFragment(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricipal_bottom_tap);
        navigationManager = FragmentNavigationManager.obtain(this);

        addFragment(1);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void addFragment(int pos) {
        if (pos == 1) {
            navigationManager.principalFragment(getString(R.string.title_dashboard));
//            getSupportActionBar().setTitle(getString(R.string.title_leave));
        } else if (pos == 2) {
            navigationManager.principalMoreFragment(getString(R.string.title_more));
//            getSupportActionBar().setTitle(getString(R.string.title_more));
        } else if (pos == 3) {
            navigationManager.principalApproalsFragment(getString(R.string.title_approvals));
        }


    }

}
