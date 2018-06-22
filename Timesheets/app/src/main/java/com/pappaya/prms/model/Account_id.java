package com.pappaya.prms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasar on 10/12/16.
 */
public class Account_id implements Parcelable {
    private String id;
    private String name;

    public Account_id() {
    }

    public Account_id(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Account_id(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Account_id> CREATOR = new Creator<Account_id>() {
        @Override
        public Account_id createFromParcel(Parcel in) {
            return new Account_id(in);
        }

        @Override
        public Account_id[] newArray(int size) {
            return new Account_id[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }
}
