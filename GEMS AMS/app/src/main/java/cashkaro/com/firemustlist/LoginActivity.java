package cashkaro.com.firemustlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cashkaro.com.firemustlist.App.App;
import cashkaro.com.firemustlist.api.LoginApi;
import cashkaro.com.firemustlist.dashboard.DashboardActivity;
import cashkaro.com.firemustlist.dashboard.model.SchoolList;
import cashkaro.com.firemustlist.model.Result;
import cashkaro.com.firemustlist.model.ResultResponse;
import cashkaro.com.firemustlist.retrofitservice.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {


    private EditText name, password, domainurl;

    private Retrofit retrofit;
    private static final String TAG = "LoginActivity";

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

//        retrofit = new RetrofitRequest(this).getRetrofit();

        domainurl = (EditText) findViewById(R.id.dominname);


        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("firemustlist", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("logedin", false)) {


            if (sharedPreferences.getString("user_type", "").equalsIgnoreCase("School Administrator")) {

                Intent intent = new Intent(getApplicationContext(), SchoolListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {

                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }


        name = (EditText)

                findViewById(R.id.name);

        password = (EditText)

                findViewById(R.id.password);


        Log.e(TAG, "onCreate: saved url " + sharedPreferences.getString("url", ""));


//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("url", domainurl.getText().toString());
//        editor.commit();

    }

    public void onLogin(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("url", domainurl.getText().toString());
        editor.commit();

        retrofit = new RetrofitRequest(this).getRetrofit();
//        Log.e(TAG, "onLogin: " + domainurl.getText().toString());

        name.setError(null);
        password.setError(null);
        domainurl.setError(null);

//        if (domainurl.getText().toString().length() > 0) {
//            domainurl.setError("Please enter domain url");
//        } else
//
        if (!isValidEmail(name.getText().toString())) {
            name.setError("Enter a valid email.");
        } else if (password.getText().toString().length() <= 0) {
            password.setError("Enter a password");
        } else {


            if (Utils.isOnline(getApplicationContext())) {

                Utils.showProgress(LoginActivity.this, "Loading");

                try {
                    LoginApi loginApi = retrofit.create(LoginApi.class);

                    JSONObject jsonObject = new JSONObject();
                    JSONObject params = new JSONObject();
                    params.put("login", name.getText().toString());
                    params.put("password", password.getText().toString());
                    jsonObject.put("params", params);

                    Log.e(TAG, "onLogin: post data  " + jsonObject.toString());

                    loginApi.login(jsonObject.toString()).enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                            Utils.hideProgress();

                            if (response.body().getResult().getStatus() == true) {

                                Result result = response.body().getResult();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_id", result.getUserId().toString());
                                editor.putString("user_type", result.getUserType().toString());
                                editor.putString("login", result.getLogin().toString());
                                editor.putString("password", result.getPassword().toString());
                                editor.putBoolean("logedin", true);
                                editor.commit();

                                if (sharedPreferences.getString("user_type", "").equalsIgnoreCase("School Administrator")) {

                                    Intent intent = new Intent(getApplicationContext(), SchoolListActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }

//                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Enter a valid credentials", Toast.LENGTH_SHORT).show();
                                name.setError("Enter a valid email.");
                                password.setError("Enter a valid password");
                            }

                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {
                            Utils.hideProgress();
                            Log.e(TAG, "onFailure: " + t.getMessage() + "   " + call.request().body());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

        }

//        Intent intent = new Intent(this, FireMusterActivity.class);
//        startActivity(intent);

    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }
}
