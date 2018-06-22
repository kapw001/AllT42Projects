package com.pappayaed.ui.parentprofile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pappayaed.R;
import com.pappayaed.RecyclerViewCommon.DividerItemDecoration;
import com.pappayaed.RecyclerViewCommon.RecyclerViewAdapter;
import com.pappayaed.RecyclerViewCommon.RecyclerViewAdapterMultiView;
import com.pappayaed.base.BaseActivity;
import com.pappayaed.common.Utils;
import com.pappayaed.ui.showprofile.IProfileIntractor;
import com.pappayaed.ui.showprofile.IProfilePresenter;
import com.pappayaed.ui.showprofile.IProfileView;
import com.pappayaed.ui.showprofile.ProfileIntractorImpl;
import com.pappayaed.ui.showprofile.ProfilePresenterImpl;
import com.pappayaed.ui.showprofile.StudentList;

import com.pappayaed.ui.showprofile.UserDetails;
import com.pappayaed.ui.studentprofile.Header;
import com.pappayaed.ui.studentprofile.StudentProfileActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ParentActivity extends BaseActivity implements IProfileView, RecyclerViewAdapterMultiView.OnRecyclerViewItemClickListener {

    private static final String TAG = "ParentActivity";

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_id)
    TextView profileId;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.back)
    ImageView back;

    private RecyclerViewAdapterMultiView mAdapter;

    private DividerItemDecoration dividerItemDecoration;

    private IProfilePresenter iProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            setCustomView("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();


        IProfileIntractor iProfileIntractor = new ProfileIntractorImpl(dataSource);

        iProfilePresenter = new ProfilePresenterImpl(this, iProfileIntractor);

        callServices();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.back)
    public void onBack() {

        super.onBackPressed();

    }

    private void callServices() {

        if (iProfilePresenter != null) {
            showLoading();

            iProfilePresenter.displayProfile();
            iProfilePresenter.getAllProfile();
        } else Log.e(TAG, "Presenter not initialized........................................ ");
    }

    private void init() {

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);

        mAdapter = new RecyclerViewAdapterMultiView(Collections.EMPTY_LIST);

        recyclerview.setAdapter(mAdapter);

        dividerItemDecoration = new DividerItemDecoration(this, R.drawable.divider, Collections.EMPTY_LIST);

        recyclerview.addItemDecoration(dividerItemDecoration);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    public void displayProfile(String name, String userType, String image) {

        profileName.setText(name);
        profileId.setText(userType);
        Bitmap imagebmp = Utils.decodeBitmap(this, image);

        profileImage.setImageBitmap(imagebmp);
        Utils.setBorderColor(profileImage);
    }

    @Override
    public void gotoStudentProfileActivity() {

    }


    @Override
    public void setData(List<UserDetails> list) {

        hideLoading();

    }

    @Override
    public void setData(Map<Object, Object> map) {
        hideLoading();

        List<Integer> removeLine = new ArrayList<>();

        List list = new ArrayList();

        list.add(new Header("Profile"));

        removeLine.add(0);

        List profileModelList = (List<UserDetails>) map.get("profile");

        list.addAll(profileModelList);


        List<StudentList> childList = (List<StudentList>) map.get("child");

        if (childList.size() > 0) {

            list.add(new Header("Child Profile"));

            int s = list.size() - 1;

            removeLine.add(s);
            list.addAll(childList);
        }


        dividerItemDecoration.update(removeLine);

        mAdapter.updateData(list);

    }

    @Override
    public void showLoading() {

        Utils.showProgress(this, "Loading");

    }

    @Override
    public void hideLoading() {

        Utils.hideProgress();
    }

    @Override
    public void setError(String msg) {
        hideLoading();

    }

    @Override
    public void setEmptyData() {
        hideLoading();

    }

    @Override
    public void onItemClick(View view, Object o, int position) {

//        Flowable.just(o != null ? o : new Object()).map(new Function<Object, StudentList>() {
//
//            @Override
//            public StudentList apply(Object o) throws Exception {
//
//
//                if (o instanceof StudentList) {
//
//                    StudentList studentList = (StudentList) o;
//
//
//                    return studentList;
//
//                }
//
//                return new StudentList();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<StudentList>() {
//            @Override
//            public void accept(StudentList studentList) throws Exception {
//
//                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
//                intent.putExtra("studentlist", (Serializable) studentList);
//                startActivity(intent);
//
//            }
//        });


        if (o instanceof StudentList) {

            StudentList studentList = (StudentList) o;
            Intent intent = new Intent(this, StudentProfileActivity.class);
            intent.putExtra("studentlist", (Serializable) studentList);
            startActivity(intent);

        } else {

            Log.e(TAG, "onItemClick: There is no student profile ");

        }

    }

}
