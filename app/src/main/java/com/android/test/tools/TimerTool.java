package com.android.test.tools;

import java.text.SimpleDateFormat;

/**
 * Created by elvis on 2016/10/18.
 */

public class TimerTool {

    public static String getYearMonthDay(){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy年MM月dd日 ");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

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

    /**
     * 将秒格式化为（时分秒）的格式
     *
     * @param second 秒
     * @return 格式化后的字符串
     */
    public static String formatSecond(Double second) {
        try{
            String html = "0秒";
            if (second != null) {
                Double s = second;
                String format;
                Object[] array;
                Integer hours = (int) (s / (60 * 60));
                Integer minutes = (int) (s / 60 - hours * 60);
                Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
                if (hours > 0) {
                    format = "%1$,d时%2$,d分%3$,d秒";
                    array = new Object[]{hours, minutes, seconds};
                } else if (minutes > 0) {
                    format = "%1$,d分%2$,d秒";
                    array = new Object[]{minutes, seconds};
                } else {
                    format = "%1$,d秒";
                    array = new Object[]{seconds};
                }
                html = String.format(format, array);
            }

            return html;
        } catch (Exception e ){
            return "0秒";
        }
    }

}
