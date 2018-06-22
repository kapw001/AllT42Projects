package cashkaro.com.firemustlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cashkaro.com.firemustlist.App.App;
import cashkaro.com.firemustlist.adapter.RecyclerViewAdapter;
import cashkaro.com.firemustlist.adapter.SchoolListAdapter;
import cashkaro.com.firemustlist.api.LoginApi;
import cashkaro.com.firemustlist.dashboard.DashboardActivity;
import cashkaro.com.firemustlist.database.DatabaseHandler;
import cashkaro.com.firemustlist.model.Result;
import cashkaro.com.firemustlist.model.ResultResponse;
import cashkaro.com.firemustlist.model.SchoolList;
import cashkaro.com.firemustlist.model.VisitorInfo;
import cashkaro.com.firemustlist.retrofitservice.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class SchoolListActivity extends AppCompatActivity implements SchoolListAdapter.OnCheckBoxClick {

    private RecyclerView recyclerView;
    private SchoolListAdapter recyclerViewAdapter;
    private List<SchoolList> schoolLists;

    private SharedPreferences sharedPreferences;
    private static final String TAG = "SchoolListActivity";

    private Retrofit retrofit;

    private DatabaseHandler databaseHandler;

    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);

        retrofit = new RetrofitRequest(this).getRetrofit();

        msg = (TextView) findViewById(R.id.msg);
        databaseHandler = new DatabaseHandler(this);

        sharedPreferences = getSharedPreferences("firemustlist", MODE_PRIVATE);
        schoolLists = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerViewAdapter = new SchoolListAdapter(this, schoolLists);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                linearLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        if (Utils.isOnline(this)) {

            loadSchoolList();
        } else {
            List<SchoolList> databaselist = databaseHandler.getAllSchoolList();


            if (sharedPreferences.getBoolean("SchoolAdministrator", false)) {
                SchoolList schoolList = databaselist.get(0);
                startActivityFun(schoolList.getId(), schoolList.getName());
                finish();
            } else {


                if (databaselist.size() <= 0) {
                    msg.setVisibility(View.VISIBLE);
                }

                schoolLists.clear();
                schoolLists.addAll(databaselist);
                recyclerViewAdapter.updateList(databaselist);
            }
        }


        getSupportActionBar().setTitle("School List");
    }


    private void loadSchoolList() {

        Utils.showProgress(this, "Loading");

        try {
            LoginApi loginApi = retrofit.create(LoginApi.class);

            JSONObject jsonObject = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("user_id", Integer.parseInt(sharedPreferences.getString("user_id", "0")));
            params.put("user_type", sharedPreferences.getString("user_type", "nothing"));
            jsonObject.put("params", params);

            Log.e(TAG, "School list : post data  " + jsonObject.toString());

            loginApi.getSchoolList(jsonObject.toString()).enqueue(new Callback<ResultResponse>() {
                @Override
                public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {

                    Utils.hideProgress();

                    Log.e(TAG, "onResponse: schoollist  " + response.headers() + "  " + response.message() + "  " + response.raw());

                    try {
                        if (response.body().getResult().getStatus() == true) {


                            for (int i = 0; i < response.body().getResult().getSchoolList().size(); i++) {
                                Log.e(TAG, "onResponse: size" + response.body().getResult().getSchoolList().get(i) + "   " + response.body().getResult().getSchoolList().size());
                            }


                            if (response.body().getResult().getUserType().toLowerCase().equalsIgnoreCase("School Administrator".toLowerCase())) {
                                startActivityFun(response.body().getResult().getSchoolList().get(0).getId(), response.body().getResult().getSchoolList().get(0).getName());

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("SchoolAdministrator", true);
                                editor.commit();

                                List<SchoolList> list = new ArrayList<SchoolList>();
                                list.add(new SchoolList(response.body().getResult().getSchoolList().get(0).getId(), response.body().getResult().getSchoolList().get(0).getName()));

                                databaseHandler.deleteAllSchoolListRecord();

                                databaseHandler.addAllSchoolList(list);

                                finish();

                            } else {
                                List<SchoolList> list = response.body().getResult().getSchoolList();
                                databaseHandler.deleteAllSchoolListRecord();

                                databaseHandler.addAllSchoolList(list);

                                List<SchoolList> databaselist = databaseHandler.getAllSchoolList();
                                schoolLists.clear();
                                schoolLists.addAll(databaselist);
                                recyclerViewAdapter.updateList(databaselist);
                            }
                            msg.setVisibility(View.GONE);

                        } else {
//                            msg.setVisibility(View.VISIBLE);
//                            Toast.makeText(SchoolListActivity.this, "There is no school", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
//                        msg.setVisibility(View.VISIBLE);
//                        Toast.makeText(SchoolListActivity.this, "There is no school", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<ResultResponse> call, Throwable t) {
                    Utils.hideProgress();
//                    Log.e(TAG, "onFailure: " + t.getMessage());
//                    msg.setVisibility(View.VISIBLE);

                    List<SchoolList> databaselist = databaseHandler.getAllSchoolList();

                    if (databaselist.size() <= 0) {
                        msg.setVisibility(View.VISIBLE);
                    }

                    schoolLists.clear();
                    schoolLists.addAll(databaselist);
                    recyclerViewAdapter.updateList(databaselist);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnItemClick(int pos) {

        SchoolList schoolList = schoolLists.get(pos);

        startActivityFun(schoolList.getId(), schoolList.getName());

        Log.e(TAG, "OnItemClick: Name " + schoolList.getName() + " id  " + schoolList.getId());

    }


    private void startActivityFun(int id, String name) {


        if (sharedPreferences.getString("user_type", "").equalsIgnoreCase("School Administrator")) {

            Intent intent = new Intent(this, FireMusterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("school_id", id);
            intent.putExtra("school_name", name);
            startActivity(intent);
        } else {

            Intent intent = new Intent(this, FireMusterWhoIsINActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("school_id", id);
            intent.putExtra("school_name", name);
            startActivity(intent);
        }


//        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();

    }
}
