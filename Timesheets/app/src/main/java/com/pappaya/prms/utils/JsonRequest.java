package com.pappaya.prms.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pappaya.prms.sessionmanagement.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yasar on 25/12/16.
 */
public class JsonRequest {

    private static int timeout = 60000;
    private static final String TAG = "JsonRequest";

    public static void makeRequest(Context context, JSONObject jsonObject, String method, final RequestCallback requestCallback, String hosturl) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        Log.e(TAG, "makeRequest: Original Request " + jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, hosturl + method,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestCallback.onSuccess(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestCallback.OnFail(error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                timeout,0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(timeout,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(timeout,
////                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        System.setProperty("http.keepAlive", "false");

        requestQueue.add(jsonObjReq);
    }


    public interface RequestCallback {
        void onSuccess(JSONObject response);

        void OnFail(VolleyError error);
    }

}
