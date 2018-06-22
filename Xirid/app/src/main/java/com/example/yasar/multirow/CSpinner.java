package com.example.yasar.multirow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * Created by yasar on 19/10/16.
 */
public class CSpinner extends Spinner {

    public CSpinner(Context context) {
        super(context);
    }

    public CSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    int measureContentWidth(SpinnerAdapter adapter, Drawable background) {
        if (adapter == null) {
            return 0;
        }
        View view = adapter.getView(getSelectedItemPosition(), null, this);
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
        }
        view.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED));
        int width = view.getMeasuredWidth();
        if (background != null) {
            Rect mTempRect = new Rect();
            background.getPadding(mTempRect);
            width += mTempRect.left + mTempRect.right;
        }
        return width;
    }
}
