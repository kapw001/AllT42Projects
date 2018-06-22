package com.example.yasar.multirow;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yasar on 6/10/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<SliderModel> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<SliderModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_row, parent, false);
//        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_row, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView, itemList);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {

        if (!itemList.isEmpty()) {

            holder.businessName.setText(itemList.get(position).getShopName());

            long days = Helper.getDay(itemList.get(position).getEndDate());

            if (days == 0) {
                holder.validDate.setText("Today Only");
            } else {
                holder.validDate.setText("Valid for " + days + " more days");
            }


            if (itemList.get(position).getNewitem().equalsIgnoreCase("true")) {
                holder.newimage.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        itemList.get(position).setNewitem("false");
                        holder.newimage.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
            } else {
                holder.newimage.setVisibility(View.INVISIBLE);
            }


            if (itemList.get(position).getThumpimage() != null && !itemList.get(position).getThumpimage().equalsIgnoreCase("")) {
                Helper.setImage(itemList.get(position).getThumpimage(), holder.rectangleimagefirst, holder.progressBar);
            }

            if (itemList.get(position).getSliderImage() != null && !itemList.get(position).getSliderImage().equalsIgnoreCase("")) {
                Helper.setImage(itemList.get(position).getSliderImage(), holder.rectangleimagefirst, holder.progressBar);
            }

        }

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


}

class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView newimage;
    public ProportionalImageView rectangleimagefirst;
    public ProgressBar progressBar;
    private List<SliderModel> itemList;

    public TextView businessName, validDate;

    public RecyclerViewHolders(View itemView, List<SliderModel> itemList) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.itemList = itemList;

        rectangleimagefirst = (ProportionalImageView) itemView.findViewById(R.id.rectanglefirstimage);
        newimage = (ImageView) itemView.findViewById(R.id.newimage);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        businessName = (TextView) itemView.findViewById(R.id.businessname);
        validDate = (TextView) itemView.findViewById(R.id.valid);
    }

    private static final String TAG = "RecyclerViewHolders";

    @Override
    public void onClick(final View view) {


        if (!itemList.get(getPosition()).getWebsitelink().equalsIgnoreCase("Give Image website view link")) {


            final CharSequence[] items = {"Gallery", "Webview Link"};
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Open Ad");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Gallery")) {

                        ArrayList<String> imageslist = new ArrayList<>();

                        if (itemList.get(getPosition()).getSliderImage() != null && !itemList.get(getPosition()).getSliderImage().equalsIgnoreCase("")) {

                            imageslist.add(itemList.get(getPosition()).getSliderImage());

                            Log.e(TAG, "onClick: 1 " + itemList.get(getPosition()).getSliderImage());

                        }


                        if (itemList.get(getPosition()).getFirstimage() != null && !itemList.get(getPosition()).getFirstimage().equalsIgnoreCase("")) {

                            imageslist.add(itemList.get(getPosition()).getFirstimage());

                            Log.e(TAG, "onClick: 1 " + itemList.get(getPosition()).getFirstimage());

                        }


                        if (itemList.get(getPosition()).getSecondimage() != null && !itemList.get(getPosition()).getSecondimage().equalsIgnoreCase("")) {

                            imageslist.add(itemList.get(getPosition()).getSecondimage());

                            Log.e(TAG, "onClick: 2 " + itemList.get(getPosition()).getSecondimage());

                        }

                        if (itemList.get(getPosition()).getThirdimage() != null && !itemList.get(getPosition()).getThirdimage().equalsIgnoreCase("")) {

                            imageslist.add(itemList.get(getPosition()).getThirdimage());

                            Log.e(TAG, "onClick: 3 " + itemList.get(getPosition()).getThirdimage());


                        }

                        if (itemList.get(getPosition()).getFourthimage() != null && !itemList.get(getPosition()).getFourthimage().equalsIgnoreCase("")) {

                            imageslist.add(itemList.get(getPosition()).getFourthimage());

                            Log.e(TAG, "onClick: 4 " + itemList.get(getPosition()).getFourthimage());

                        }

                        if (itemList.get(getPosition()).getFifthimage() != null && !itemList.get(getPosition()).getFifthimage().equalsIgnoreCase("")) {

                            imageslist.add(itemList.get(getPosition()).getFifthimage());

                            Log.e(TAG, "onClick: 5 " + itemList.get(getPosition()).getFifthimage());

                        }


                        Intent in = new Intent(view.getContext(), ImageGalleryView.class);


                        in.putStringArrayListExtra("imagelist", imageslist);
//        in.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) itemList);

                        view.getContext().startActivity(in);


                    } else if (items[item].equals("Webview Link")) {

                        if (!itemList.get(getPosition()).getWebsitelink().equalsIgnoreCase("Give Image website view link")) {

                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemList.get(getPosition()).getWebsitelink()));
                                view.getContext().startActivity(browserIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(view.getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                            }


                        } else {

                            Toast.makeText(view.getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                        }


                    }

//                    else if (items[item].equals("Cancel")) {
//                        dialog.dismiss();
//                    }
                }
            });
            builder.show();
        } else {

            ArrayList<String> imageslist = new ArrayList<>();

            if (itemList.get(getPosition()).getSliderImage() != null && !itemList.get(getPosition()).getSliderImage().equalsIgnoreCase("")) {

                imageslist.add(itemList.get(getPosition()).getSliderImage());

                Log.e(TAG, "onClick: 1 " + itemList.get(getPosition()).getSliderImage());

            }


            if (itemList.get(getPosition()).getFirstimage() != null && !itemList.get(getPosition()).getFirstimage().equalsIgnoreCase("")) {

                imageslist.add(itemList.get(getPosition()).getFirstimage());

                Log.e(TAG, "onClick: 1 " + itemList.get(getPosition()).getFirstimage());

            }


            if (itemList.get(getPosition()).getSecondimage() != null && !itemList.get(getPosition()).getSecondimage().equalsIgnoreCase("")) {

                imageslist.add(itemList.get(getPosition()).getSecondimage());

                Log.e(TAG, "onClick: 2 " + itemList.get(getPosition()).getSecondimage());

            }

            if (itemList.get(getPosition()).getThirdimage() != null && !itemList.get(getPosition()).getThirdimage().equalsIgnoreCase("")) {

                imageslist.add(itemList.get(getPosition()).getThirdimage());

                Log.e(TAG, "onClick: 3 " + itemList.get(getPosition()).getThirdimage());


            }

            if (itemList.get(getPosition()).getFourthimage() != null && !itemList.get(getPosition()).getFourthimage().equalsIgnoreCase("")) {

                imageslist.add(itemList.get(getPosition()).getFourthimage());

                Log.e(TAG, "onClick: 4 " + itemList.get(getPosition()).getFourthimage());

            }

            if (itemList.get(getPosition()).getFifthimage() != null && !itemList.get(getPosition()).getFifthimage().equalsIgnoreCase("")) {

                imageslist.add(itemList.get(getPosition()).getFifthimage());

                Log.e(TAG, "onClick: 5 " + itemList.get(getPosition()).getFifthimage());

            }


            Intent in = new Intent(view.getContext(), ImageGalleryView.class);


            in.putStringArrayListExtra("imagelist", imageslist);
//        in.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) itemList);

            view.getContext().startActivity(in);

        }


//        Toast.makeText(view.getContext(), "Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}