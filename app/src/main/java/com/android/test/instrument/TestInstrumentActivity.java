package com.android.test.instrument;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.test.Constant;
import com.android.test.R;
import com.android.test.shell.utest.UMVirtualTerminal;
import com.android.test.shell.utest.VTCommandResult;

import java.io.IOException;

public class TestInstrumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_instrument);

        View.OnClickListener myOnClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.but_instrument_start:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String cmd = "am instrument -w -e class com.android.test.instrument.MyInstrumaentTestCase com.android.test/android.test.InstrumentationTestRunner";
                                UMVirtualTerminal umVirtualTerminal = null;
                                try {
                                    umVirtualTerminal = new UMVirtualTerminal();
                                    umVirtualTerminal.runCommand(cmd);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (umVirtualTerminal!=null){
                                        umVirtualTerminal.shutdown();
                                    }
                                }
                            }
                        }).start();
                        break;
                    case R.id.but_instrument_end:
                        synchronized (MyInstrumaentTestCase.mInstrumentLock) {
                            MyInstrumaentTestCase.mInstrumentLock.notifyAll();
                        }
                        break;
                }

            }
        };

        Button startButton = (Button) findViewById(R.id.but_instrument_start);
        startButton.setOnClickListener(myOnClickListener);
        Button endButton = (Button) findViewById(R.id.but_instrument_end);
        endButton.setOnClickListener(myOnClickListener);

    }
}
