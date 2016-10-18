package com.android.test.uiautomator;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.test.R;

public class TestUIAutomatorActivity extends AppCompatActivity {


    View.OnClickListener onClickListener;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_uiautomator);


        //Handler
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(TestUIAutomatorActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
            }
        };

        //OnClick
        onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.but_start:
                        UIAutomatorTool.startCollection(handler);
                        break;
                    case R.id.but_stop:
                        UIAutomatorTool.stopCollection();
                        break;
                    case R.id.but_get_state:
                        TextView stateTextView = (TextView) findViewById(R.id.textView_state);
                        if (UIAutomatorTool.isRun()){
                            stateTextView.setText("运行中");
                        }else {
                            stateTextView.setText("未运行");
                        }
                        break;
                }
            }
        };




        //视图
        Button startbutton = (Button) findViewById(R.id.but_start);
        Button stopButton = (Button) findViewById(R.id.but_stop);
        Button getStateButton = (Button) findViewById(R.id.but_get_state);
        startbutton.setOnClickListener(onClickListener);
        stopButton.setOnClickListener(onClickListener);
        getStateButton.setOnClickListener(onClickListener);
    }




}
