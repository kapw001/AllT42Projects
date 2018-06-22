package com.example.yasar.multirow;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.yasar.multirow.dummy.model.CityEvent;
import com.example.yasar.multirow.dummy.model.SliderModel;
import com.example.yasar.multirow.utility.Helper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.yasar.multirow.dummy.model.CityEvent.SLIDER_TYPE;
import static com.example.yasar.multirow.dummy.model.CityEvent.RECTANGLE_TYPE;
import static com.example.yasar.multirow.dummy.model.CityEvent.BANNER_TYPE;

public class DifferentRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    ImageLoader imageLoader = ImageLoader.getInstance();

    private static DifferentRowAdapter adapter;


    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));


    private List<CityEvent> mList;
    private static Context context;

    public DifferentRowAdapter(List<CityEvent> list, Context context) {
        this.mList = list;
        this.context = context;
        adapter = this;
    }

    public void UpdateList(List<CityEvent> mList) {
        this.mList.clear();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public void setFilter(List<CityEvent> countryModels) {
        this.mList.clear();
//        mList = new ArrayList<>();
        this.mList.addAll(countryModels);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case SLIDER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageslideshow, parent, false);
                return new SliderViewHolder(view);
            case RECTANGLE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridrecyclerview, parent, false);
                return new RectangleViewHolder(view, context);
            case BANNER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner, parent, false);
                return new BannerViewHolder(view, mList);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        CityEvent object = mList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case SLIDER_TYPE:

                    ((SliderViewHolder) holder).setImageSlider(mList.get(position).getSliderimage());
                    break;
                case RECTANGLE_TYPE://

                    ((RectangleViewHolder) holder).setRectangleImage(mList.get(position).getSliderimage());

                    break;

                case BANNER_TYPE:


                    final ArrayList<SliderModel> itemList = mList.get(position).getSliderimage();

                    ((BannerViewHolder) holder).businessName.setText(itemList.get(0).getShopName());


                    long days = Helper.getDay(itemList.get(0).getEndDate());


                    if (days == 0) {
                        ((BannerViewHolder) holder).validDate.setText("Today Only");
                    } else {
                        ((BannerViewHolder) holder).validDate.setText("Valid for " + days + " more days");
                    }


                    if (itemList.get(0).getNewitem().equalsIgnoreCase("true")) {
                        ((BannerViewHolder) holder).newimage.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                itemList.get(0).setNewitem("false");
                                ((BannerViewHolder) holder).newimage.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);

                    } else {
                        ((BannerViewHolder) holder).newimage.setVisibility(View.INVISIBLE);
                    }

                    if (!itemList.isEmpty()) {
//                        Log.e("Testing", "onBindViewHolder: Karthik " + itemList.get(0).getSliderImage());
                        if (itemList.get(0).getThumpimage() != null && !itemList.get(0).getThumpimage().equalsIgnoreCase("")) {
                            Helper.setImage(itemList.get(0).getThumpimage(), ((BannerViewHolder) holder).bannerimage, ((BannerViewHolder) holder).progressBar);
                        }

                        if (itemList.get(0).getSliderImage() != null && !itemList.get(0).getSliderImage().equalsIgnoreCase("")) {
                            Helper.setImage(itemList.get(0).getSliderImage(), ((BannerViewHolder) holder).bannerimage, ((BannerViewHolder) holder).progressBar);
                        }

                    }

                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            CityEvent object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
        private static final String TAG = "SliderViewHolder";
        //        private TextView mTitle;
        private SliderLayout mDemoSlider;
        private static ArrayList<SliderModel> sliderimage;

        private View iteView;

        private int currentPosithion = 0;

        private static int nextImageCount = 4;
        private static int count = 0;
        private ImageView seeall;

        public SliderViewHolder(View itemView) {
            super(itemView);
            this.iteView = itemView;

            mDemoSlider = (SliderLayout) itemView.findViewById(R.id.slider);
            seeall = (ImageView) itemView.findViewById(R.id.seeall);


            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomIndicator((PagerIndicator) itemView.findViewById(R.id.custom_indicator));
            mDemoSlider.setCustomAnimation(new CustonAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
////                    if (count < sliderimage.size() - 4) {
//////                        nextImageCount += 4;
////                    } else {
//////                        nextImageCount = 4;
////                        count = 0;
////                    }
//                    // setImageSlider(sliderimage);
//                    adapter.notifyDataSetChanged();
//
//                    Log.e(TAG, "run: " + nextImageCount + "           " + count);
//
//                }
//            }, 3000);

        }

        public void setImageSlider(final ArrayList<SliderModel> sliderimage) {

            this.sliderimage = sliderimage;
            if (!sliderimage.isEmpty()) {
                final HashMap<Integer, String> url_maps = new HashMap<Integer, String>();
                url_maps.clear();
                mDemoSlider.removeAllSliders();

//                while (count < nextImageCount) {
//                    int i = count;
//
//                    if (sliderimage.get(i).getSliderImage() != null && !sliderimage.get(i).getSliderImage().equalsIgnoreCase("")) {
//                        url_maps.put(i, sliderimage.get(i).getSliderImage());
//
//                    }
//                    count++;
//                }


                for (int i = 0; i < 5; i++) {

                    if (count >= sliderimage.size()) {
                        count = 0;
                    }

                    if (count < sliderimage.size()) {
                        if (sliderimage.get(count).getSliderImage() != null && !sliderimage.get(count).getSliderImage().equalsIgnoreCase("")) {
                            url_maps.put(count, sliderimage.get(count).getSliderImage());

                        }
                    }


                    count++;

                }

                Log.e(TAG, "setImageSlider: Size " + sliderimage.size() + " count:    " + count);

                for (Map.Entry name : url_maps.entrySet()) {
                    TextSliderView textSliderView = new TextSliderView(itemView.getContext());
                    // initialize a SliderLayout

                    textSliderView
                            .description(name.getValue().toString())
                            .image(url_maps.get(name.getKey()))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);
                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putInt("pos", Integer.parseInt(name.getKey().toString()));

                    mDemoSlider.addSlider(textSliderView);
                }


                if (url_maps.size() == 1) {
                    mDemoSlider.stopAutoCycle();
                    mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                } else {
                    mDemoSlider.startAutoCycle();
                    mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
                }
            }

            seeall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(view.getContext(), SeeAllSlideImageActivity.class);
                    in.putParcelableArrayListExtra("slide", sliderimage);
                    view.getContext().startActivity(in);
                }
            });


        }

        @Override
        public void onSliderClick(final BaseSliderView slider) {
            currentPosithion = slider.getBundle().getInt("pos");

            if (!sliderimage.get(currentPosithion).getWebsitelink().equalsIgnoreCase("Give Image website view link")) {


                final CharSequence[] items = {"Gallery", "Webview Link"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(slider.getContext());
                builder.setTitle("Open Ad");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Gallery")) {

                            ArrayList<String> imageslist = new ArrayList<>();


                            if (sliderimage.get(currentPosithion).getSliderImage() != null && !sliderimage.get(currentPosithion).getSliderImage().equalsIgnoreCase("")) {

                                imageslist.add(sliderimage.get(currentPosithion).getSliderImage());


                            }

                            if (sliderimage.get(currentPosithion).getFirstimage() != null && !sliderimage.get(currentPosithion).getFirstimage().equalsIgnoreCase("")) {

                                imageslist.add(sliderimage.get(currentPosithion).getFirstimage());


                            }
                            if (sliderimage.get(currentPosithion).getSecondimage() != null && !sliderimage.get(currentPosithion).getSecondimage().equalsIgnoreCase("")) {

                                imageslist.add(sliderimage.get(currentPosithion).getSecondimage());


                            }

                            if (sliderimage.get(currentPosithion).getThirdimage() != null && !sliderimage.get(currentPosithion).getThirdimage().equalsIgnoreCase("")) {

                                imageslist.add(sliderimage.get(currentPosithion).getThirdimage());


                            }
                            if (sliderimage.get(currentPosithion).getFourthimage() != null && !sliderimage.get(currentPosithion).getFourthimage().equalsIgnoreCase("")) {

                                imageslist.add(sliderimage.get(currentPosithion).getFourthimage());


                            }
                            if (sliderimage.get(currentPosithion).getFifthimage() != null && !sliderimage.get(currentPosithion).getFifthimage().equalsIgnoreCase("")) {

                                imageslist.add(sliderimage.get(currentPosithion).getFifthimage());


                            }


                            Intent in = new Intent(slider.getContext(), ImageGalleryView.class);


                            in.putStringArrayListExtra("imagelist", imageslist);
//        in.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) itemList);

                            slider.getContext().startActivity(in);

                        } else if (items[item].equals("Webview Link")) {

                            if (!sliderimage.get(currentPosithion).getWebsitelink().equalsIgnoreCase("Give Image website view link")) {

                                try {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sliderimage.get(currentPosithion).getWebsitelink()));
                                    slider.getContext().startActivity(browserIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(slider.getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Toast.makeText(slider.getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                            }


                        }
//                        else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
                    }
                });
                builder.show();
            } else {
                ArrayList<String> imageslist = new ArrayList<>();


                if (sliderimage.get(currentPosithion).getSliderImage() != null && !sliderimage.get(currentPosithion).getSliderImage().equalsIgnoreCase("")) {

                    imageslist.add(sliderimage.get(currentPosithion).getSliderImage());


                }

                if (sliderimage.get(currentPosithion).getFirstimage() != null && !sliderimage.get(currentPosithion).getFirstimage().equalsIgnoreCase("")) {

                    imageslist.add(sliderimage.get(currentPosithion).getFirstimage());


                }
                if (sliderimage.get(currentPosithion).getSecondimage() != null && !sliderimage.get(currentPosithion).getSecondimage().equalsIgnoreCase("")) {

                    imageslist.add(sliderimage.get(currentPosithion).getSecondimage());


                }

                if (sliderimage.get(currentPosithion).getThirdimage() != null && !sliderimage.get(currentPosithion).getThirdimage().equalsIgnoreCase("")) {

                    imageslist.add(sliderimage.get(currentPosithion).getThirdimage());

                }
                if (sliderimage.get(currentPosithion).getFourthimage() != null && !sliderimage.get(currentPosithion).getFourthimage().equalsIgnoreCase("")) {

                    imageslist.add(sliderimage.get(currentPosithion).getFourthimage());


                }
                if (sliderimage.get(currentPosithion).getFifthimage() != null && !sliderimage.get(currentPosithion).getFifthimage().equalsIgnoreCase("")) {

                    imageslist.add(sliderimage.get(currentPosithion).getFifthimage());

                }


                Intent in = new Intent(slider.getContext(), ImageGalleryView.class);


                in.putStringArrayListExtra("imagelist", imageslist);
//        in.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) itemList);

                slider.getContext().startActivity(in);

            }


        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//            currentPosithion = position;
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView newimage;

        private ProportionalImageView bannerimage;

        private ProgressBar progressBar;
        private List<SliderModel> itemList;

        private List<CityEvent> mList1;

        private TextView businessName, validDate;

        public BannerViewHolder(View itemView, List<CityEvent> mList1) {
            super(itemView);

            itemView.setOnClickListener(this);
            this.mList1 = mList1;

            bannerimage = (ProportionalImageView) itemView.findViewById(R.id.bannerimage);
            newimage = (ImageView) itemView.findViewById(R.id.newimage);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);

            businessName = (TextView) itemView.findViewById(R.id.businessname);
            validDate = (TextView) itemView.findViewById(R.id.valid);
        }

        //        public void setImageViewPager(List<SliderModel> itemList) {
//            this.itemList = itemList;
//        }
        private static final String TAG = "BannerViewHolder";

        @Override
        public void onClick(final View view) {

            if (!mList1.get(getPosition()).getSliderimage().get(0).getWebsitelink().equalsIgnoreCase("Give Image website view link")) {


                final CharSequence[] items = {"Gallery", "Webview Link"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Open Ad");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Gallery")) {

                            ArrayList<String> imageslist = new ArrayList<>();

                            itemList = mList1.get(getPosition()).getSliderimage();

                            if (itemList.get(0).getSliderImage() != null && !itemList.get(0).getSliderImage().equalsIgnoreCase("")) {

                                imageslist.add(itemList.get(0).getSliderImage());

                                Log.e(TAG, "onClick: 1 " + itemList.get(0).getSliderImage());

                            }

                            if (itemList.get(0).getFirstimage() != null && !itemList.get(0).getFirstimage().equalsIgnoreCase("")) {

                                imageslist.add(itemList.get(0).getFirstimage());

                                Log.e(TAG, "onClick: 1 " + itemList.get(0).getFirstimage());

                            }
                            if (itemList.get(0).getSecondimage() != null && !itemList.get(0).getSecondimage().equalsIgnoreCase("")) {

                                imageslist.add(itemList.get(0).getSecondimage());

                                Log.e(TAG, "onClick: 2 " + itemList.get(0).getSecondimage());


                            }

                            if (itemList.get(0).getThirdimage() != null && !itemList.get(0).getThirdimage().equalsIgnoreCase("")) {

                                imageslist.add(itemList.get(0).getThirdimage());

                                Log.e(TAG, "onClick: 3 " + itemList.get(0).getThirdimage());

                            }
                            if (itemList.get(0).getFourthimage() != null && !itemList.get(0).getFourthimage().equalsIgnoreCase("")) {

                                imageslist.add(itemList.get(0).getFourthimage());

                                Log.e(TAG, "onClick: 4 " + itemList.get(0).getFourthimage());

                            }
                            if (itemList.get(0).getFifthimage() != null && !itemList.get(0).getFifthimage().equalsIgnoreCase("")) {

                                imageslist.add(itemList.get(0).getFifthimage());

                                Log.e(TAG, "onClick: 5 " + itemList.get(0).getFifthimage());

                            }


                            Intent in = new Intent(view.getContext(), ImageGalleryView.class);


                            in.putStringArrayListExtra("imagelist", imageslist);
//        in.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) itemList);

                            view.getContext().startActivity(in);

                        } else if (items[item].equals("Webview Link")) {

                            if (!mList1.get(getPosition()).getSliderimage().get(0).getWebsitelink().equalsIgnoreCase("Give Image website view link")) {

                                try {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mList1.get(getPosition()).getSliderimage().get(0).getWebsitelink()));
                                    view.getContext().startActivity(browserIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(view.getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Toast.makeText(view.getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                            }


                        }
//                        else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
                    }
                });
                builder.show();
            } else {
                ArrayList<String> imageslist = new ArrayList<>();

                itemList = mList1.get(getPosition()).getSliderimage();

                if (itemList.get(0).getSliderImage() != null && !itemList.get(0).getSliderImage().equalsIgnoreCase("")) {

                    imageslist.add(itemList.get(0).getSliderImage());

                    Log.e(TAG, "onClick: 1 " + itemList.get(0).getSliderImage());

                }

                if (itemList.get(0).getFirstimage() != null && !itemList.get(0).getFirstimage().equalsIgnoreCase("")) {

                    imageslist.add(itemList.get(0).getFirstimage());

                    Log.e(TAG, "onClick: 1 " + itemList.get(0).getFirstimage());

                }
                if (itemList.get(0).getSecondimage() != null && !itemList.get(0).getSecondimage().equalsIgnoreCase("")) {

                    imageslist.add(itemList.get(0).getSecondimage());

                    Log.e(TAG, "onClick: 2 " + itemList.get(0).getSecondimage());


                }

                if (itemList.get(0).getThirdimage() != null && !itemList.get(0).getThirdimage().equalsIgnoreCase("")) {

                    imageslist.add(itemList.get(0).getThirdimage());

                    Log.e(TAG, "onClick: 3 " + itemList.get(0).getThirdimage());

                }
                if (itemList.get(0).getFourthimage() != null && !itemList.get(0).getFourthimage().equalsIgnoreCase("")) {

                    imageslist.add(itemList.get(0).getFourthimage());

                    Log.e(TAG, "onClick: 4 " + itemList.get(0).getFourthimage());

                }
                if (itemList.get(0).getFifthimage() != null && !itemList.get(0).getFifthimage().equalsIgnoreCase("")) {

                    imageslist.add(itemList.get(0).getFifthimage());

                    Log.e(TAG, "onClick: 5 " + itemList.get(0).getFifthimage());

                }


                Intent in = new Intent(view.getContext(), ImageGalleryView.class);


                in.putStringArrayListExtra("imagelist", imageslist);
//        in.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) itemList);

                view.getContext().startActivity(in);

            }


//            Toast.makeText(view.getContext(), "Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    public static class RectangleViewHolder extends RecyclerView.ViewHolder {
        private GridLayoutManager lLayout;
        private List<SliderModel> rowListItem;
        private RecyclerViewAdapter rcAdapter;


        public RectangleViewHolder(View itemView, Context context) {
            super(itemView);
            lLayout = new GridLayoutManager(context, 2);
            rowListItem = new ArrayList<>();

            RecyclerView rView = (RecyclerView) itemView.findViewById(R.id.gridrecyclerView);
            rView.setHasFixedSize(true);
            rView.setLayoutManager(lLayout);

            rView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            rView.setItemAnimator(new DefaultItemAnimator());


            rcAdapter = new RecyclerViewAdapter(context, rowListItem);
            rView.setAdapter(rcAdapter);
        }

        public void setRectangleImage(ArrayList<SliderModel> rowListItem1) {

            rowListItem.clear();
            rowListItem.addAll(rowListItem1);
            rcAdapter.notifyDataSetChanged();

        }
    }

    /**
     * Converting dp to pixel
     */
    private static int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

//    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
//
//        private int spanCount;
//        private int spacing;
//        private boolean includeEdge;
//
//        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
//            this.spanCount = spanCount;
//            this.spacing = spacing;
//            this.includeEdge = includeEdge;
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            int position = parent.getChildAdapterPosition(view); // item position
//            int column = position % spanCount; // item column
//
//            if (includeEdge) {
//                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//
//                if (position < spanCount) { // top edge
//                    outRect.top = spacing;
//                }
//                outRect.bottom = spacing; // item bottom
//            } else {
//                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
//                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//                if (position >= spanCount) {
//                    outRect.top = spacing; // item top
//                }
//            }
//        }
//    }
}