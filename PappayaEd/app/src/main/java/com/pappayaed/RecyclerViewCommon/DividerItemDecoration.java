package com.pappayaed.RecyclerViewCommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.pappayaed.ui.studentprofile.Header;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yasar on 26/3/18.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DividerItemDecoration";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private List<Integer> list = new ArrayList<>();

    private Drawable divider;

    /**
     * Default divider will be used
     */
    public DividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public DividerItemDecoration(Context context, int resId, List<Integer> removeLineBetween) {
        divider = ContextCompat.getDrawable(context, resId);
        this.list = removeLineBetween;
    }

    public void update(List<Integer> removeLineBetween) {
        this.list = removeLineBetween;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {


        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {


            if (!list.contains(i)) {

                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }
}