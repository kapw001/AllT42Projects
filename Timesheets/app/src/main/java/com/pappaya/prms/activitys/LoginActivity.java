package com.pappaya.prms.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pappaya.prms.sessionmanagement.SessionManager;
import com.pappaya.prms.utils.Cons;
import com.pappaya.prms.utils.ProgressUtil;
import com.pappaya.prms.utils.Utilitys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.pappaya.prms.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private EditText userName, passWord, url;
    private Button login;

    private SessionManager sessionManager;

    private static final String urlstr = "http://192.168.1.23:8069/mobile/login?user=karthik@think42labs.com&passw=test";
    //timesheet
    //

    private ImageView img;

    private Spinner hostname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        img = (ImageView) findViewById(R.id.img);

        userName = (EditText) findViewById(R.id.email);

        hostname = (Spinner) findViewById(R.id.hostname);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (s.length() == 0) {
//                    userName.setError("Enter a username");
                } else {
                    userName.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passWord = (EditText) findViewById(R.id.password);

        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 0) {
//                    passWord.setError("Enter a password");
                } else {
                    passWord.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        url = (EditText) findViewById(R.id.url);
        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() == 0) {
//                    url.setError("Enter a Tenant Id.");
                } else {
                    url.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login:


//                Toast.makeText(LoginActivity.this, "" + hostnametxt[hostname.getSelectedItemPosition()], Toast.LENGTH_SHORT).show();

                login();

//                try {
//                    loginTest();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

//                new getData().execute();
//                login();
//                Intent in = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(in);
//                finish();
                break;
        }
    }


    private void login() {
        final String email = userName.getText().toString();
        final String password = passWord.getText().toString();
        final String urltxt = url.getText().toString();


//        if (email.length() <= 0) {
//            userName.setError("Enter a username");
////            return;
//        } else if (password.length() <= 0) {
//            passWord.setError("Enter a password");
////            return;
//        } else {
//            userName.setError(null);
//            passWord.setError(null);
//            url.setError(null);
//        }


        if (email.length() > 0 && password.length() > 0 && urltxt.length() > 0) {
            Utilitys.showProgressDialog(LoginActivity.this);
            final String[] hostnametxt = getResources().getStringArray(R.array.url);
            final String u = "https://" + urltxt + "." + hostnametxt[hostname.getSelectedItemPosition()] + "/mobile/";
//            final String u = "http://192.168.1.243:8069/mobile/";//"https://" + urltxt + "." + hostnametxt[hostname.getSelectedItemPosition()] + "/mobile/";

            JSONObject params = new JSONObject();
            try {
                params.put("db", urltxt);
                params.put("login", email);
                params.put("password", password);
                params.put("hostname", hostnametxt[hostname.getSelectedItemPosition()]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();


            try {
                jsonObject.put("jsonrpc", "2.0");
                jsonObject.put("method", "call");
                jsonObject.put("id", "2");
                jsonObject.put("params", params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Toast.makeText(LoginActivity.this, "" + u, Toast.LENGTH_SHORT).show();

//            Log.e(TAG, "login: " + u);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, u + "login",
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: " + response.toString());
//
//                    String res = response.toString();


                    try {
                        JSONObject result = response.getJSONObject("result");

                        if (result.getString("status").equalsIgnoreCase("200")) {

                            JSONObject detail = result.getJSONObject("detail");

                            sessionManager.createLoginSession(email, password, detail.getString("first_name") + " " + detail.getString("last_name"), detail.getString("image"), detail.getString("manager"), urltxt, detail.getString("id"), detail.getString("user_id"), hostnametxt[hostname.getSelectedItemPosition()]);


                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(in);
                            finish();
                        } else {

//                            Toast.makeText(LoginActivity.this, "Please check your credentials.", Toast.LENGTH_SHORT).show();
                            ProgressUtil.showDialog(LoginActivity.this, "Warning", "Please check your credentials.", userName, getApplicationContext());
                        }
//                        Log.e(TAG, "onResponse: " + result.toString());
//
//                        Log.e(TAG, "onResponse: " + result.getString("message"));

                    } catch (NullPointerException e) {
//                        Toast.makeText(LoginActivity.this, "Please check your credentials.", Toast.LENGTH_SHORT).show();
                        ProgressUtil.showDialog(LoginActivity.this, "Warning", "Please check your credentials.", userName, getApplicationContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Utilitys.hideProgressDialog(LoginActivity.this);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: " + error);

                    Utilitys.hideProgressDialog(LoginActivity.this);
//                    Toast.makeText(LoginActivity.this, "Please check your credentials.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                    ProgressUtil.showDialog(LoginActivity.this, "Warning", "Please check your credentials.", url, getApplicationContext());

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjReq);
        } else {
            Utilitys.hideProgressDialog(LoginActivity.this);
            Toast.makeText(LoginActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

}
