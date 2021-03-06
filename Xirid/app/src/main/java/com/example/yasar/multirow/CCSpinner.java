package com.example.yasar.multirow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by yasar on 18/10/16.
 */
public class CCSpinner extends Spinner {
    public CCSpinner(Context context) {
        super(context);
    }

    public CCSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CCSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CCSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CCSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}