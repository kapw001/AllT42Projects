package com.example.yasar.multirow;

import android.app.Application;
import android.os.StrictMode;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by yasar on 7/10/16.
 */
public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                .permitAll().build();
//        StrictMode.setThreadPolicy(policy);

//        Picasso.Builder builder = newimage Picasso.Builder(this);
//        builder.downloader(newimage OkHttpDownloader(this, Integer.MAX_VALUE));
//        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
//        built.setLoggingEnabled(true);
//        Picasso.setSingletonInstance(built);

    }
}