package com.pappaya.prms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasar on 10/12/16.
 */
public class TimeSheetDetail implements Parcelable{

    private User_id user_id;

    private String date_from;

    private String total_timesheet;

    private String state;

    private String date_to;

    private String id;

    private Account_ids account_ids;

    private Employee_id employee_id;

    protected TimeSheetDetail(Parcel in) {
        date_from = in.readString();
        total_timesheet = in.readString();
        state = in.readString();
        date_to = in.readString();
        id = in.readString();
    }

    public static final Creator<TimeSheetDetail> CREATOR = new Creator<TimeSheetDetail>() {
        @Override
        public TimeSheetDetail createFromParcel(Parcel in) {
            return new TimeSheetDetail(in);
        }

        @Override
        public TimeSheetDetail[] newArray(int size) {
            return new TimeSheetDetail[size];
        }
    };

    public Employee_id getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Employee_id employee_id) {
        this.employee_id = employee_id;
    }

    public TimeSheetDetail() {

    }

    public TimeSheetDetail(User_id user_id, String date_from, String total_timesheet, String state, String date_to, String id, Account_ids account_ids) {
        this.user_id = user_id;
        this.date_from = date_from;
        this.total_timesheet = total_timesheet;
        this.state = state;
        this.date_to = date_to;
        this.id = id;
        this.account_ids = account_ids;
    }

    public User_id getUser_id() {
        return user_id;
    }

    public void setUser_id(User_id user_id) {
        this.user_id = user_id;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getTotal_timesheet() {
        return total_timesheet;
    }

    public void setTotal_timesheet(String total_timesheet) {
        this.total_timesheet = total_timesheet;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account_ids getAccount_ids() {
        return account_ids;
    }

    public void setAccount_ids(Account_ids account_ids) {
        this.account_ids = account_ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date_from);
        parcel.writeString(total_timesheet);
        parcel.writeString(state);
        parcel.writeString(date_to);
        parcel.writeString(id);
    }
}
