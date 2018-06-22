package com.pappayaed.pricepaldemo.approval;

import com.google.gson.annotations.Expose;

/**
 * Created by yasar on 9/3/18.
 */

public class Approvals {

    @Expose(serialize = false)
    private String purpose;
    @Expose(serialize = false)
    private String description;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }


    @Expose
    private boolean isVisible = false;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

}
