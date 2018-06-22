package com.pappayaed.ui.showprofile;

import com.pappayaed.data.DataSource;
import com.pappayaed.data.listener.DataListener;
import com.pappayaed.data.parser.Parser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yasar on 26/3/18.
 */

public class ProfileIntractorImpl implements IProfileIntractor {


    private DataSource dataSource;

    public ProfileIntractorImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void getProfile(OnProfileListener onProfileListener) {

        String profileImage = dataSource.getProfileImage();
        String profileName = dataSource.getProfileName();
        String userType = dataSource.getUserType();

        onProfileListener.displayProfile(profileName, userType, profileImage);
    }

    @Override
    public void getAllProfile(final OnFinishedListener onFinishedListener) {

        Map<Object, Object> json = new HashMap<>();
        Map<Object, Object> params = new HashMap<>();
        params.put("login", dataSource.getEmailOrUsername());
        params.put("password", dataSource.getPassword());
        params.put("user_types", dataSource.getUserType());

        json.put("params", params);

        final String js = new JSONObject(json).toString();



        dataSource.getAllProfile(js, new DataListener() {
            @Override
            public void onSuccess(Object object) {

                Parser.allProfileParse(object.toString(), onFinishedListener, dataSource);

            }

            @Override
            public void onFail(Throwable throwable) {

                onFinishedListener.onFail(throwable);
            }

            @Override
            public void onNetworkFailure() {

                onFinishedListener.onNetworkFailure();
            }
        });


    }
}
