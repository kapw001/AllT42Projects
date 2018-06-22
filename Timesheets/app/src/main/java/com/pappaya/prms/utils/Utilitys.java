package com.pappaya.prms.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.pappaya.prms.model.AddDates;
import com.pappaya.prms.model.TimeSheetActivitys;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Created by yasar on 28/11/16.
 */
public class Utilitys {
    public static String myFormat = "MM/dd/yy"; //In which you need put here
    public static SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private Calendar calendar = Calendar.getInstance();


    public static ProgressDialog progress;

    public static void showProgressDialog(Context context) {
        progress = ProgressDialog.show(context, "Timesheet", "Loading.........", true);
        progress.setCancelable(true);
    }

    public static void hideProgressDialog(Context context) {
        if (progress != null) {
            progress.dismiss();

        }
    }

    public static Date stringToDateUsingFormater(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Convert from String to Date
        Date startDate = new Date();
        try {
            startDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;

    }

    public static String converDate(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date datetxt = null;
        try {
            datetxt = parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        final String formattedDatefrom = formatter.format(datetxt);
        return formattedDatefrom;
    }

    public static String converDateServer(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");
        Date datetxt = null;
        try {
            datetxt = parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDatefrom = formatter.format(datetxt);
        return formattedDatefrom;
    }

    public static String displayDate(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date datetxt = null;
        try {
            datetxt = parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM, yyyy");
        final String formattedDatefrom = formatter.format(datetxt);
        return formattedDatefrom;
    }


    public static String displayDateTest(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("d MMM, yyyy");
        Date datetxt = null;
        try {
            datetxt = parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        final String formattedDatefrom = formatter.format(datetxt);
        return formattedDatefrom;
    }

    public static Date getDateA(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");
        Date f = null;
        try {
            f = parser.parse(value);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static Date getDate(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date f = null;
        try {
            f = parser.parse(value);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return f;
    }


    public static String displayDateAnother(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");
        Date datetxt = null;
        try {
            datetxt = parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM, yyyy");
        final String formattedDatefrom = formatter.format(datetxt);
        return formattedDatefrom;
    }

    public static String convertDate(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("d MMM, yyyy");
        Date datetxt = null;
        try {
            datetxt = parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        final String formattedDatefrom = formatter.format(datetxt);
        return formattedDatefrom;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //blah
        }
    }

    public static String removeLastChar(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }


    public static void callCallender(final EditText editText, Activity mContext) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                editText.setText(sdf.format(myCalendar.getTime()));

            }

        };
        new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));


    }

    public static void callCallender(final TextView editText, Activity mContext) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                editText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void callCallenderReturn(final TextView editText, Activity mContext, final CallValue callValue) {

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

//                editText.setText(sdf.format(myCalendar.getTime()));
                callValue.value(sdf.format(myCalendar.getTime()));

            }


        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                callValue.value(null);
            }
        });

    }

    public static void callCallenderReturn(String setdate, final TextView editText, Activity mContext, final CallValue callValue) {

        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date(setdate));
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

//                editText.setText(sdf.format(myCalendar.getTime()));
                callValue.value(sdf.format(myCalendar.getTime()));

            }


        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                callValue.value(null);
            }
        });

    }

    public static final String DEFAULT_TIME_ZONE = "Asia/Kolkata";

    public static Calendar convertStringToCalendar(String dateString, String timeFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        format.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        try {
            cal.setTime(format.parse(dateString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String convertDateObjectToString(Date date, String timeFormat) {
        String formattedDate = null;
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        format.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        try {
            formattedDate = format.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    private static final String TAG = "Utilitys";

//    public static boolean totalUnit(List<AddDates> datesArrayList, String datetest, String hours) {
//
//
//        List<AddDates> newList = new ArrayList<>();
//        List<AddDates> copy = datesArrayList; //new ArrayList<>();
//        double unit = 0.0;
//        for (int i = 0; i < datesArrayList.size(); i++) {
//            AddDates addDates = datesArrayList.get(i);
//////            AddDates addDates2 = null;
//
//
//            Log.e(TAG, "totalUnit: " + addDates.getDate() + "       " + datetest);
////            for (int j = 0; j < copy.size(); j++) {
////                AddDates addDates1 = copy.get(j);
//            if (addDates.getDate().equalsIgnoreCase(datetest)) {
//
////                    System.out.println("ok");
//                unit += Double.parseDouble(addDates.getUnit_amount());
//
//
//            }
////                addDates2 = addDates;//new AddDates(addDates.getName(),addDates.getAccount_id(), unit + "", addDates.getCdate(), addDates.getId(), addDates.getName());
//
//        }
//
//
//        return unit + Double.parseDouble(hours) <= Double.parseDouble("24");
//
////            newList.add(addDates2);
//
////    }
//
//
////    return false;
//
//    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean totalUnit(List<Object> datesArrayList, String datetest, String hours) {


//        List<Object> newList = new ArrayList<>();
//        List<Object> copy = datesArrayList; //new ArrayList<>();
        double unit = 0.0;
        for (int i = 0; i < datesArrayList.size(); i++) {
            AddDates addDates = (AddDates) datesArrayList.get(i);
////            AddDates addDates2 = null;


            Log.e(TAG, "totalUnit: " + addDates.getDate() + "       " + datetest);
//            for (int j = 0; j < copy.size(); j++) {
//                AddDates addDates1 = copy.get(j);
            if (addDates.getDate().equalsIgnoreCase(datetest)) {

//                    System.out.println("ok");
                unit += Double.parseDouble(addDates.getUnit_amount());


            }
//                addDates2 = addDates;//new AddDates(addDates.getName(),addDates.getAccount_id(), unit + "", addDates.getCdate(), addDates.getId(), addDates.getName());

        }


        return unit + Double.parseDouble(hours) <= Double.parseDouble("24");

//            newList.add(addDates2);

//    }


//    return false;

    }


    public static ArrayList<TimeSheetActivitys> removeDup(ArrayList<TimeSheetActivitys> datesArrayList) {
        ArrayList<TimeSheetActivitys> newList = new ArrayList<>();
        ArrayList<TimeSheetActivitys> newListResult = new ArrayList<>();
        ArrayList<TimeSheetActivitys> copy = datesArrayList;// new ArrayList<>();


        for (int i = 0; i < datesArrayList.size(); i++) {
            TimeSheetActivitys addDates = datesArrayList.get(i);
            TimeSheetActivitys addDates2 = null;
            double unit = 0.0;
            for (int j = 0; j < copy.size(); j++) {
                TimeSheetActivitys addDates1 = copy.get(j);
                if (addDates.getCdate().equalsIgnoreCase(addDates1.getCdate()) && addDates.getAccount_id().getId().equalsIgnoreCase(addDates1.getAccount_id().getId())) {

//                    System.out.println("ok");
                    unit += Double.parseDouble(addDates1.getUnit_amount());

                }
                addDates2 = new TimeSheetActivitys(addDates.getDisplay_name(), addDates.getAccount_id(), unit + "", addDates.getCdate(), addDates.getId(), addDates.getName());

            }

            newList.add(addDates2);

        }

        for (int k = 0; k < newList.size(); k++) {
            TimeSheetActivitys addDates = newList.get(k);
            boolean isFound = false;

            for (int j = 0; j < newListResult.size(); j++) {
                TimeSheetActivitys addDates1 = newListResult.get(j);
                if (addDates.getCdate().equalsIgnoreCase(addDates1.getCdate()) && addDates.getAccount_id().getId().equalsIgnoreCase(addDates1.getAccount_id().getId()))
                    isFound = true;
                break;


            }
            if (!isFound) newListResult.add(addDates);

//            if (!isFound) {

//                    newListResult.add(addDates);
//            }
        }


        return newListResult;
    }

    public interface CallValue {
        void value(String v);

    }

}
