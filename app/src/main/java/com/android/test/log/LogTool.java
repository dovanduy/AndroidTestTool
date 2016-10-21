package com.android.test.log;

import android.util.Log;

import com.android.test.Constant;
import com.android.test.tools.TimerTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by elvis on 2016/10/18.
 * 带本地打印的日志输出
 */

public class LogTool {

    public static boolean isLocal = true;//是否要进行本地保存

    private static Lock mLock = new ReentrantLock();

    //LOG 等级
    public enum LEVEL{
        Level_i,
        Level_d,
        Level_e
    }

    public static void makeLog(LEVEL level, String tag,String msg){
        switch (level){
            case Level_i:
                Log.i(tag,msg);
                break;
            case Level_d:
                Log.d(tag,msg);
                break;
            case Level_e:
                Log.e(tag,msg);
                break;
        }
        if (isLocal){
            Writer writer = null;
            try{
                mLock.lock();

                String logFileName = Constant.LOG_PATH+File.separator+"Log_"+ TimerTool.getYearMonthDayHour()+".txt";
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFileName,true), "UTF-8"));
                writer.write(TimerTool.getYearMonthDayHourMinuteSecond()+"("+tag+":)\n");
                writer.write(msg+"\n");
            }catch (Exception e){
            }finally {
                if (writer!=null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mLock.unlock();
            }
        }
    }




}
