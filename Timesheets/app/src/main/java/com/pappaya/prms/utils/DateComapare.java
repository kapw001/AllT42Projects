package com.pappaya.prms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.pappaya.prms.model.TimeSheetActivitys;

/**
 * Created by yasar on 18/12/16.
 */
public class DateComapare implements Comparator<TimeSheetActivitys> {
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int compare(TimeSheetActivitys t, TimeSheetActivitys t1) {
        Date f = null;
        Date s = null;
        try {
            f = parser.parse(t.getCdate());
            s = parser.parse(t1.getCdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return f.compareTo(s);
    }
}
