package com.pappaya.prms.activitys;

import com.pappaya.prms.model.TimeSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by yasar on 24/12/16.
 */
public class DateComapareTimesheet implements Comparator<TimeSheet> {
    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int compare(TimeSheet timeSheet, TimeSheet t1) {
        Date f = null;
        Date s = null;
        try {
            f = parser.parse(timeSheet.getTimeSheetDetailslist().get(0).getDate_from());
            s = parser.parse(t1.getTimeSheetDetailslist().get(0).getDate_from());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return f.compareTo(s);
    }
}
