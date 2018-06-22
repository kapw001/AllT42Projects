package com.example.yasar.multirow;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.yasar.multirow.dummy.model.CityEvent;
import com.example.yasar.multirow.dummy.model.SliderModel;

import java.util.ArrayList;
import java.util.List;

public class SeeAllSlideImageActivity extends AppCompatActivity {

    private ArrayList<SliderModel> arrayList;
    private DifferentRowAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private List<CityEvent> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_slide_image);
        arrayList = getIntent().getParcelableArrayListExtra("slide");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        for (SliderModel s : arrayList
                ) {
            ArrayList<SliderModel> bannerlist = new ArrayList<>();
            bannerlist.add(s);
            list.add(new CityEvent(bannerlist, CityEvent.BANNER_TYPE));
        }


        adapter = new DifferentRowAdapter(list, this);

        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
