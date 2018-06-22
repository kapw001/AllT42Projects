package com.pappaya.prms.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.pappaya.prms.R;

/**
 * Created by yasar on 10/12/16.
 */
public class ProgressUtil {

    public static ProgressDialog mProgressDialog;

    public static void showProgressbar(Activity context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading..........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    public static void hideProgressbar() {
        mProgressDialog.hide();
    }


    public static void showDialog(final Activity activity, String title, String msg, final EditText editText, final Context context) {
//        final EditTextFocus editTextFocus = (EditTextFocus) activity;
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
//                        editText.setFocusableInTouchMode(true);
//                        editText.requestFocus();
//                        editText.performClick();
                        editText.requestFocus();
//                        editText.onKeyUp(KeyEvent.KEYCODE_DPAD_CENTER, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_CENTER));
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);

//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
//                        if (editText.requestFocus()) {
//                            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                            editText.clearFocus();
//
//                        }
                    }
                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
                .setIcon(R.drawable.alerticon)
                .show();
    }

//    interface EditTextFocus {
//        void onFocus(EditText editText);
//    }

}
