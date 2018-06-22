package com.pappayaed.pricepaldemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.pappayaed.R;

/**
 * Created by yasar on 14/3/18.
 */

public class CustomButtonView extends AppCompatButton {


    public CustomButtonView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

//        setBackground(getResources().getDrawable(R.drawable.roundrectgreen));


        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ColorOptionsView, 0, 0);
        int valueColor = a.getColor(R.styleable.ColorOptionsView_valueColor,
                ContextCompat.getColor(context, R.color.colorPrimary));

        a.recycle();


        setBackground(ContextCompat.getDrawable(context, R.drawable.roundrectgreen));
        Drawable background = getBackground();
        ((GradientDrawable) background).setColor(valueColor);


//        if (background instanceof ShapeDrawable) {
//            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(context, valueColor));
//        } else if (background instanceof GradientDrawable) {
//            ((GradientDrawable) background).setColor(ContextCompat.getColor(context, valueColor));
//        } else if (background instanceof ColorDrawable) {
//            ((ColorDrawable) background).setColor(ContextCompat.getColor(context,valueColor));
//        }

    }

    public void setValueColor(@ColorInt int id) {

        Drawable background = getBackground();
        ((GradientDrawable) background).setColor(id);
    }

}
