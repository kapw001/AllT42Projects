package com.pappaya.prms.sessionmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import com.pappaya.prms.activitys.LoginActivity;

/**
 * Created by yasar on 24/11/16.
 */
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";


    public static final String KEY_IMAGE = "image";
    public static final String KEY_ID = "id";
    public static final String KEY_USERID = "userid";

    // Email address (make variable public to access from outside)
    public static final String KEY_ISEMPLOYEE = "employee";

    public static final String KEY_EMPLOYEENAME = "employeename";
    public static final String KEY_URL = "url";
    public static final String KEY_SHEETID = "sheetid";
    public static final String KEY_HOSTURL = "hosturl";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String username, String password, String emploayeeName, String image, String isemployee, String url, String id, String userid, String hosturl) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, username);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_EMPLOYEENAME, emploayeeName);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_ISEMPLOYEE, isemployee);
        editor.putString(KEY_URL, url);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_HOSTURL, hosturl);

        // commit changes
        editor.commit();
    }


//    /**
//     * Check login method wil check user login status
//     * If false it will redirect user to login page
//     * Else won't do anything
//     */
//    public void checkLogin() {
//        // Check login status
//        if (!this.isLoggedIn()) {
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(_context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            _context.startActivity(i);
//        }
//
//    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        user.put(KEY_ISEMPLOYEE, pref.getString(KEY_ISEMPLOYEE, null));
        user.put(KEY_EMPLOYEENAME, pref.getString(KEY_EMPLOYEENAME, null));
        user.put(KEY_URL, pref.getString(KEY_URL, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_HOSTURL, pref.getString(KEY_HOSTURL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
