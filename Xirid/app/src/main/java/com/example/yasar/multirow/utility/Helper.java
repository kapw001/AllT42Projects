package com.example.yasar.multirow.utility;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yasar on 3/11/16.
 */
public class Helper {

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    public static long getDay(String eventDateArg) {

        long days = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss aa");
        // Here Set your Event Date
        Date eventDate = null;
        try {
            eventDate = dateFormat.parse(eventDateArg);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date();
        if (!currentDate.after(eventDate)) {
            long diff = eventDate.getTime()
                    - currentDate.getTime();
            days = diff / (24 * 60 * 60 * 1000);
            return days;
//            diff -= days * (24 * 60 * 60 * 1000);
//            long hours = diff / (60 * 60 * 1000);
//            diff -= hours * (60 * 60 * 1000);
//            long minutes = diff / (60 * 1000);
//            diff -= minutes * (60 * 1000);
//            long seconds = diff / 1000;

//            if (days == 0) {
//                holder.validDate.setText("Today Only");
//            } else {
//                holder.validDate.setText("Valid for " + days + " more days");
//            }

        }


        return days;
    }


    public static void setImage(final String url, final ImageView imageView, final ProgressBar progressBar) {
        Bitmap bitmap = null;

        imageLoader.displayImage(url, imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Do whatever you want with Bitmap
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);

                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


}
