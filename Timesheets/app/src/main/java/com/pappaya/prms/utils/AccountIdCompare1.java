package com.pappaya.prms.utils;

import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.TimeSheetActivitys;

import java.util.Comparator;

/**
 * Created by yasar on 18/12/16.
 */
public class AccountIdCompare1 implements Comparator<Object> {

    @Override
    public int compare(Object t, Object t1) {
        AddDates addDates1 = (AddDates) t;
        AddDates addDates2 = (AddDates) t1;
        return (addDates1.getName().compareTo(addDates2.getName()));
    }
}
