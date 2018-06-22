package cashkaro.com.firemustlist.dashboard.request;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import cashkaro.com.firemustlist.App.App;
import cashkaro.com.firemustlist.dashboard.api.WebApi;
import cashkaro.com.firemustlist.dashboard.model.ResultResponse;
import cashkaro.com.firemustlist.dashboard.model.SchoolList;
import cashkaro.com.firemustlist.dashboard.model.VisitorList;
import cashkaro.com.firemustlist.retrofitservice.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 29/8/17.
 */

public class WebRequest {

    private static WebRequest webRequest;
    private Context context;

    private Retrofit retrofit;
    private WebApi webApi;

    public WebRequest(Context context) {
        this.context = context;
        this.retrofit = new RetrofitRequest(context).getRetrofit();
        this.webApi = retrofit.create(WebApi.class);
    }


    public static WebRequest getWebRequest(Context context) {

        if (webRequest == null) {
            webRequest = new WebRequest(context);
        }

        return webRequest;
    }

    public JSONObject getParms() {
        JSONObject jsonObject = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            jsonObject.put("params", params);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "getParms: " + jsonObject.toString());

        return new JSONObject();

    }

    public JSONObject getParms(String startdate, String endDate, List<Integer> uIdList) {
        JSONObject jsonObject = new JSONObject();
        JSONObject params = new JSONObject();


        try {
            params.put("fromdate", startdate);
            params.put("todate", endDate);
            params.put("university_id", new JSONArray(uIdList));
            jsonObject.put("params", params);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "getParms: " + jsonObject.toString());

        return new JSONObject();

    }

    public void getSchoolList(JSONObject jsonObject, final CallBackWebResponse callBackWebResponse) {

        webApi.getSchoolList(jsonObject.toString()).enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                Log.e(TAG, "onResponse: " + response.headers() + "  " + response.message() + "  " + response.raw());

                if (response.body().getResult() != null) {

                    List<SchoolList> schoolLists = response.body().getResult().getSchoolList();

                    for (int i = 0; i < response.body().getResult().getSchoolList().size(); i++) {
                        Log.e(TAG, "onResponse Dashboad: size" + response.body().getResult().getSchoolList().get(i).getName() + "   " + response.body().getResult().getSchoolList().size());
                    }

                    callBackWebResponse.showSchoolData(schoolLists);
                } else {
                    callBackWebResponse.fail("There is no data ");
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {

                callBackWebResponse.fail(t.getMessage());
            }
        });

    }

    public void getVisitorData(JSONObject jsonObject, final CallBackWebResponse callBackWebResponse) {

        webApi.getVisitorData(jsonObject.toString()).enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                if (response.body().getResult() != null) {

                    List<VisitorList> visitorLists = response.body().getResult().getVisitorList();

                    callBackWebResponse.showVisitorData(visitorLists);
                } else {
                    callBackWebResponse.fail("There is no data ");
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {

                callBackWebResponse.fail(t.getMessage());
            }
        });

    }


    public interface CallBackWebResponse {

        void showSchoolData(List<SchoolList> schoolLists);

        void showVisitorData(List<VisitorList> visitorLists);

        void fail(String failMsg);

    }


}
