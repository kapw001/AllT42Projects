package com.pappaya.prms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasar on 10/12/16.
 */
public class TimeSheetActivitys implements Parcelable {

    private String display_name;

    private Account_id account_id;

    private String unit_amount;

    private String cdate;

    private String id;

    private String name;

    private String is_billable;

    private String status;

    public TimeSheetActivitys() {

    }

    public String getIs_billable() {
        return is_billable;
    }

    public void setIs_billable(String is_billable) {
        this.is_billable = is_billable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TimeSheetActivitys(String display_name, Account_id account_id, String unit_amount, String cdate, String id, String name, String is_billable, String status) {
        this.display_name = display_name;
        this.account_id = account_id;
        this.unit_amount = unit_amount;
        this.cdate = cdate;
        this.id = id;
        this.name = name;
        this.is_billable = is_billable;
        this.status = status;
    }

    public TimeSheetActivitys(String display_name, Account_id account_id, String unit_amount, String cdate, String id, String name) {
        this.display_name = display_name;
        this.account_id = account_id;
        this.unit_amount = unit_amount;
        this.cdate = cdate;
        this.id = id;
        this.name = name;
    }

    protected TimeSheetActivitys(Parcel in) {
        display_name = in.readString();
        unit_amount = in.readString();
        cdate = in.readString();
        id = in.readString();
        name = in.readString();
        is_billable = in.readString();
        status = in.readString();
        account_id = in.readParcelable(Account_id.class.getClassLoader());

    }

    public static final Creator<TimeSheetActivitys> CREATOR = new Creator<TimeSheetActivitys>() {
        @Override
        public TimeSheetActivitys createFromParcel(Parcel in) {
            return new TimeSheetActivitys(in);
        }

        @Override
        public TimeSheetActivitys[] newArray(int size) {
            return new TimeSheetActivitys[size];
        }
    };

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Account_id getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Account_id account_id) {
        this.account_id = account_id;
    }

    public String getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(String unit_amount) {
        this.unit_amount = unit_amount;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

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
        parcel.writeString(display_name);
        parcel.writeString(unit_amount);
        parcel.writeString(cdate);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(is_billable);
        parcel.writeString(status);
        parcel.writeParcelable(account_id, i);
    }
}
