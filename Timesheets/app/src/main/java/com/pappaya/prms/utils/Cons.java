package com.pappaya.prms.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yasar on 24/12/16.
 */
public class Cons {

    public static DateFormat simpleDateFormat = new SimpleDateFormat();
    //    public static String url = "http://192.168.43.94:8069/mobile/";
    public static String url = "https://SIT.pappaya.co.uk/mobile/";
//    public static String url = "http://192.168.43.209:8069/mobile/";

    public static String getS(String format, String values) throws ParseException {
        simpleDateFormat.format(format);
        Date date = simpleDateFormat.parse(values);
        String getDate = simpleDateFormat.format(date);
        return getDate;
    }


}
