package com.example.yasar.multirow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.yasar.multirow.dummy.model.SliderModel;
import com.example.yasar.multirow.utility.Helper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageGalleryView extends AppCompatActivity {

    private static final String TAG = "ImageGalleryView";
    ArrayList<String> imagelist;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mCustomPagerAdapter = new CustomPagerAdapter(this);

        imagelist = getIntent().getStringArrayListExtra("imagelist");


        Log.e(TAG, "onCreate: " + imagelist.size());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.relativeLayout));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
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

    class CustomPagerAdapter extends PagerAdapter {
        PhotoViewAttacher mAttacher;
        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagelist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            final TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.imageView);
            final ProgressBar progressbar = (ProgressBar) itemView.findViewById(R.id.progressBar);


            Helper.setImage(imagelist.get(position), imageView, progressbar);

//            imageLoader.loadImage(imagelist.get(position), new SimpleImageLoadingListener() {
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    // Do whatever you want with Bitmap
//
//                    imageView.setImageBitmap(loadedImage);
//                    progressbar.setVisibility(View.INVISIBLE);
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    super.onLoadingFailed(imageUri, view, failReason);
//                    progressbar.setVisibility(View.INVISIBLE);
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//                    super.onLoadingCancelled(imageUri, view);
//                    progressbar.setVisibility(View.INVISIBLE);
//                }
//
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    super.onLoadingStarted(imageUri, view);
//
//                }
//            });

//            new DownloadImageTask(imageView, progressbar).execute(imagelist.get(position));
//            imageView.setImageResource(mResources[position]);
//            Picasso.with(mContext).load(imagelist.get(position)).error(R.mipmap.ic_launcher).into(imageView, new Callback() {
//                @Override
//                public void onSuccess() {
//                    progressbar.setVisibility(View.INVISIBLE);
//                }
//
//                @Override
//                public void onError() {
//
//                    progressbar.setVisibility(View.INVISIBLE);
//
//                }
//            });

//            mAttacher = new PhotoViewAttacher(imageView);
//            mAttacher.update();
            container.addView(itemView);

            return itemView;
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            TouchImageView bmImage;
            ProgressBar progressbar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressbar.setVisibility(View.VISIBLE);
            }

            public DownloadImageTask(TouchImageView bmImage, ProgressBar progressbar) {
                this.bmImage = bmImage;
                this.progressbar = progressbar;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    try {
                        mIcon11 = BitmapFactory.decodeStream(in);
                    } catch (OutOfMemoryError e) {

                        Log.e(TAG, "onPostExecute: Out of Memory :" + e);

                    }

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                progressbar.setVisibility(View.GONE);
                bmImage.setImageBitmap(result);
//                bmImage.setScaleType(ImageView.ScaleType.FIT_XY);

//                try {
//                    bmImage.setImage(ImageSource.bitmap(result));
//                } catch (OutOfMemoryError e) {
//
//                    Log.e(TAG, "onPostExecute: Out of Memory :" + e);
//
//                }

            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }


    }
}
