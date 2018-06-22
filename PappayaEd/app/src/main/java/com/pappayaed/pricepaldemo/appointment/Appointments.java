package com.pappayaed.pricepaldemo.appointment;

/**
 * Created by yasar on 13/3/18.
 */

public class Appointments {

    private String time, des;

    public Appointments(String time, String des) {
        this.time = time;
        this.des = des;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
