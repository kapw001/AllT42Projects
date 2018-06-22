package com.example.yasar.multirow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by yasar on 14/9/16.
 */
public class ObservableHorizontalScrollView extends RecyclerView {


    public ObservableHorizontalScrollView(Context context) {
        super(context);
    }

    public ObservableHorizontalScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableHorizontalScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private int overallXScroll = 0;
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Log.e("T", l + " " + t+ " Scroll "+ getScrollY()+ " "+getY() +"Old "+(t+oldt) );

        View v = getChildAt(0);



        overallXScroll = overallXScroll + t;
        Log.e("check","overall X  = " + overallXScroll);

        if (v != null) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                }
            });
        }


//        if (v!=null) {
        ImageView im = (ImageView) v.findViewById(R.id.f);

        Button b = (Button) v.findViewById(R.id.b);


        if (im != null && b != null){
            if (overallXScroll < im.getY() + im.getHeight() - 150) {
                b.setY(overallXScroll);
            }
    }

//        }


    }
}
