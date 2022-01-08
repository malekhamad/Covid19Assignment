package com.example.covid19.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private static final String TAG = "DataHelper";

    public static String changeFormat(String oldDate){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(oldDate);
        } catch (ParseException e) {
            Log.i(TAG, "onError: "+e.getMessage());
        }
        return  outputFormat.format(date);
    }

}
