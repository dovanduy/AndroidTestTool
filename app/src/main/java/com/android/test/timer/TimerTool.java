package com.android.test.timer;

import android.text.format.Time;

import java.text.SimpleDateFormat;

/**
 * Created by elvis on 2016/10/18.
 */

public class TimerTool {

    public static String getYearMonthDayHour(){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy年MM月dd日HH时 ");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static String getYearMonthDayHourMinuteSecond(){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

}
