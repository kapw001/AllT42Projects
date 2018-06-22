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
public class DateComapare2 implements Comparator<AddDates> {
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int compare(AddDates t, AddDates t1) {
        Date f = null;
        Date s = null;
        try {
            f = parser.parse(t.getDate());
            s = parser.parse(t1.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return f.compareTo(s);
    }
}
