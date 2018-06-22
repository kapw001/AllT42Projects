package autodex.com.autodex.activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import com.android.volley.error.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.NetworkHelper;
import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends BaseActivity {

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final String TAG = "SplashScreenActivity";

    private String token;
    private String pW;
    private String pN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_splash_screen);


        if (sessionManager.isLoggedIn()) {

//        SessionManager.getInstance(this).changeToken("sdfsdfsdfsd");
            token = sessionManager.getDetails().get(KEY_TOKEN);
            pW = SessionManager.getInstance(getApplicationContext()).getKeyPassword();
            pN = SessionManager.getInstance(getApplicationContext()).getKeyPhonenumber();

            Observable.create(emitter -> webPostRequest.postGETData(WebUrl.NOTIFICATION_LIST, token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {

                    emitter.onNext(response);
                }

                @Override
                public void onResponse(String response) {
                    emitter.onNext(response);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    emitter.onNext(error);
                }

                @Override
                public void callRequest() {

                }
            })).flatMap(new Function<Object, ObservableSource<Object>>() {
                @Override
                public ObservableSource<Object> apply(@NonNull final Object o) throws Exception {
                    return Observable.create(emitter -> {

                        Log.e(TAG, "subscribe: " + o.toString());

                        if (o instanceof VolleyError) {


                            if (((VolleyError) o).networkResponse.statusCode == 401) {
                                Map<Object, Object> params = new HashMap<>();
                                params.put("autoDexNum", pN);
                                params.put("password", pW);
                                webPostRequest.postJSONData(WebUrl.LOGIN, params, "", new WebResponse() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        emitter.onNext(response);
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        emitter.onNext(response);
                                    }

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        emitter.onError(error);
                                    }

                                    @Override
                                    public void callRequest() {

                                    }
                                });
                            } else {

                                emitter.onNext(true);
                            }

                        } else {

                            emitter.onNext(true);

                        }


                    });
                }
            }).subscribe(this::onSucces, this::onError);
        } else {

            callActivity();
        }

    }


    private void onError(Throwable throwable) {

        callActivity();

    }

    private void onSucces(Object o) {

        if (o instanceof JSONObject) {

            JSONObject response = (JSONObject) o;
            try {

                JSONObject jsonObject = response.getJSONObject("userId");

                String userId = jsonObject.getString("id");

                JSONObject jsonObject1 = response.getJSONObject("headers");

                final String token = jsonObject1.getString("Authorization");

                sessionManager.createSession(pN, pW, userId, token);
                callActivity();

            } catch (JSONException e) {

                Log.e("splash", "onResponse: " + e.getMessage());
                callActivity();
            }

        } else {

            callActivity();
        }

    }

    private void callActivity() {
//        Toast.makeText(getApplicationContext(), "FIne", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

}
