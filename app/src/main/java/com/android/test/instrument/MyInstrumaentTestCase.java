package com.android.test.instrument;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.android.test.MainActivity;

/**
 * Created by elvis on 2016/10/17.
 * MyInstrumaentTestCase
 */

public class MyInstrumaentTestCase extends InstrumentationTestCase {

    public static Object mInstrumentLock = new Object();

    public void test1(){
        Intent intent = new Intent();
        intent.setClassName("com.android.test", MainActivity.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getInstrumentation().startActivitySync(intent);

        Log.e("","Instrument模式开始");

        synchronized (mInstrumentLock) {
            try {
                mInstrumentLock.wait();
                Log.e("","Instrument模式结束");
            } catch (Exception e) {
            }
        }
    }


}
