package com.pappaya.prms.utils;

import com.pappaya.prms.model.TimeSheetActivitys;

import java.util.Comparator;

/**
 * Created by yasar on 18/12/16.
 */
public class AccountIdCompare implements Comparator<TimeSheetActivitys> {

    @Override
    public int compare(TimeSheetActivitys timeSheetActivitys, TimeSheetActivitys t1) {
        return timeSheetActivitys.getAccount_id().getName().compareTo(t1.getAccount_id().getName());
    }
}
