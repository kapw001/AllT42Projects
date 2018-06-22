package com.pappaya.prms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yasar on 25/11/16.
 */
public class AddDates implements Parcelable {

    private String date, unit_amount, name, line_id, account_id, project;
    boolean is_timesheet = true;
    private String status, is_billable;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_billable() {
        return is_billable;
    }

    public void setIs_billable(String is_billable) {
        this.is_billable = is_billable;
    }

    public AddDates(String date, String unit_amount, String name, String line_id, String account_id, String project, String status, String is_billable) {
        this.date = date;
        this.unit_amount = unit_amount;
        this.name = name;
        this.line_id = line_id;
        this.account_id = account_id;
        this.project = project;
        this.status = status;
        this.is_billable = is_billable;
    }

    public AddDates(String date, String unit_amount, String name, String line_id, String account_id, String project) {
        this.date = date;
        this.unit_amount = unit_amount;
        this.name = name;
        this.line_id = line_id;
        this.account_id = account_id;
        this.project = project;

    }

    public AddDates(String date, String unit_amount, String name, String line_id, String account_id, String project, String is_billable) {
        this.date = date;
        this.unit_amount = unit_amount;
        this.name = name;
        this.line_id = line_id;
        this.account_id = account_id;
        this.project = project;
        this.is_billable = is_billable;

    }

    protected AddDates(Parcel in) {
        date = in.readString();
        unit_amount = in.readString();
        name = in.readString();
        line_id = in.readString();
        account_id = in.readString();
        project = in.readString();
        status = in.readString();
        is_billable = in.readString();
        is_timesheet = in.readByte() != 0;
    }

    public static final Creator<AddDates> CREATOR = new Creator<AddDates>() {
        @Override
        public AddDates createFromParcel(Parcel in) {
            return new AddDates(in);
        }

        @Override
        public AddDates[] newArray(int size) {
            return new AddDates[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(String unit_amount) {
        this.unit_amount = unit_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine_id() {
        return line_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    public int getAccount_id() {
        return Integer.parseInt(account_id);
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public boolean is_timesheet() {
        return is_timesheet;
    }

    public void setIs_timesheet(boolean is_timesheet) {
        this.is_timesheet = is_timesheet;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(unit_amount);
        parcel.writeString(name);
        parcel.writeString(line_id);
        parcel.writeString(account_id);
        parcel.writeString(project);
        parcel.writeString(status);
        parcel.writeString(is_billable);
        parcel.writeByte((byte) (is_timesheet ? 1 : 0));
    }

    @Override
    public String toString() {
        return "{" +
                "date='" + date + '\'' +
                ", unit_amount='" + unit_amount + '\'' +
                ", name='" + name + '\'' +
                ", line_id='" + line_id + '\'' +
                ", account_id='" + account_id + '\'' +
                ", is_timesheet=" + is_timesheet +
                '}';
    }
}
