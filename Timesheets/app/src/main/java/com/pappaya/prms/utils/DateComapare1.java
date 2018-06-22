package com.pappaya.prms.utils;

import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.TimeSheetActivitys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by yasar on 18/12/16.
 */
public class DateComapare1 implements Comparator<Object> {
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int compare(Object t, Object t1) {
        Date f = null;
        Date s = null;
        try {
            AddDates addDates1 = (AddDates) t;
            AddDates addDates2 = (AddDates) t1;
            f = parser.parse(addDates1.getDate());
            s = parser.parse(addDates2.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (f != null && s != null) {
            return f.compareTo(s);
        }
        return 0;
    }
}
