package com.pappaya.prms.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by yasar on 10/12/16.
 */
public class TimeSheet implements Parcelable {

    private TimeSheetActivitys timeSheetActivitys;

    private TimeSheetDetail timeSheetDetail;

    private ArrayList<TimeSheetActivitys> timeSheetActivityseslist;

    private ArrayList<TimeSheetDetail> timeSheetDetailslist;

    public TimeSheet() {
    }

    public TimeSheet(Parcel in) {
    }

    public static final Creator<TimeSheet> CREATOR = new Creator<TimeSheet>() {
        @Override
        public TimeSheet createFromParcel(Parcel in) {
            return new TimeSheet(in);
        }

        @Override
        public TimeSheet[] newArray(int size) {
            return new TimeSheet[size];
        }
    };

    public TimeSheetActivitys getTimeSheetActivitys() {
        return timeSheetActivitys;
    }

    public void setTimeSheetActivitys(TimeSheetActivitys timeSheetActivitys) {
        this.timeSheetActivitys = timeSheetActivitys;
    }

    public TimeSheetDetail getTimeSheetDetail() {
        return timeSheetDetail;
    }

    public void setTimeSheetDetail(TimeSheetDetail timeSheetDetail) {
        this.timeSheetDetail = timeSheetDetail;
    }

    public ArrayList<TimeSheetActivitys> getTimeSheetActivityseslist() {
        return timeSheetActivityseslist;
    }

    public void setTimeSheetActivityseslist(ArrayList<TimeSheetActivitys> timeSheetActivityseslist) {
        this.timeSheetActivityseslist = timeSheetActivityseslist;
    }

    public ArrayList<TimeSheetDetail> getTimeSheetDetailslist() {
        return timeSheetDetailslist;
    }

    public void setTimeSheetDetailslist(ArrayList<TimeSheetDetail> timeSheetDetailslist) {
        this.timeSheetDetailslist = timeSheetDetailslist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
