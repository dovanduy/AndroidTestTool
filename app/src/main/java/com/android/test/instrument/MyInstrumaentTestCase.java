package com.android.test.instrument;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.android.test.MainActivity;
import com.android.test.log.LogTool;

/**
 * Created by elvis on 2016/10/17.
 * MyInstrumaentTestCase
 */

public class MyInstrumaentTestCase extends InstrumentationTestCase {

    private static final String TAG = "MyInstrumaentTestCase";
    public static Object mInstrumentLock = new Object();

    public void test1(){
        Intent intent = new Intent();
        intent.setClassName("com.android.test", MainActivity.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getInstrumentation().startActivitySync(intent);

        Log.e("","Instrument模式开始");
        LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"Instrument模式开始");

        synchronized (mInstrumentLock) {
            try {
                mInstrumentLock.wait();
                Log.e("","Instrument模式结束");
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"Instrument模式結束");
            } catch (Throwable e) {
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"ttt"+e.getMessage());
            }
        }
    }


}
