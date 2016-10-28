package com.android.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.test.aidl.TestAIDLActivity;
import com.android.test.data.file.TestFileActivity;
import com.android.test.instrument.TestInstrumentActivity;
import com.android.test.log.TestLogToolActivity;
import com.android.test.service.foreground.ForegroundService;
import com.android.test.service.guard.TestGuardServiceActivity;
import com.android.test.uiautomator.view.TestUIAutomatorActivity;
import com.android.test.view.listview.pulltorefersh.TestPullToRefershListViewActivity;
import com.android.test.view.listview.expandable.TestExpandableListViewActivity;

public class MainActivity extends AppCompatActivity {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }


        View.OnClickListener myOnClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()){
                    case R.id.button_uiautomator:
                        intent.setClass(MainActivity.this, TestUIAutomatorActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_pulltorefersh_listview:
                        intent.setClass(MainActivity.this, TestPullToRefershListViewActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_file:
                        intent.setClass(MainActivity.this, TestFileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_aidl:
                        intent.setClass(MainActivity.this, TestAIDLActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_guard_service:
                        intent.setClass(MainActivity.this, TestGuardServiceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_instrument:
                        intent.setClass(MainActivity.this, TestInstrumentActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_log_tool:
                        intent.setClass(MainActivity.this, TestLogToolActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_expandable_list:
                        intent.setClass(MainActivity.this, TestExpandableListViewActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.but_Foreground_service:
                        Intent serverIntent  = new Intent(MainActivity.this, ForegroundService.class);
                        startService(serverIntent);
                        break;
                }
            }
        };

        //测试 UIAutomator
        Button uiautomatorButton = (Button) findViewById(R.id.button_uiautomator);
        uiautomatorButton.setOnClickListener(myOnClickListener);
        //测试 PullToRefershListView
        Button pullToRefershListViewButton = (Button) findViewById(R.id.but_pulltorefersh_listview);
        pullToRefershListViewButton.setOnClickListener(myOnClickListener);
        //测试 File
        Button fileButton = (Button) findViewById(R.id.but_file);
        fileButton.setOnClickListener(myOnClickListener);
        //测试 aidl
        Button aidlButton = (Button) findViewById(R.id.but_aidl);
        aidlButton.setOnClickListener(myOnClickListener);
        //测试 守护进程
        Button guardButton = (Button) findViewById(R.id.but_guard_service);
        guardButton.setOnClickListener(myOnClickListener);
        //测试 Instrument
        Button instrumentButton = (Button) findViewById(R.id.but_instrument);
        instrumentButton.setOnClickListener(myOnClickListener);
        //测试 LogTool
        Button logToolButton = (Button) findViewById(R.id.but_log_tool);
        logToolButton.setOnClickListener(myOnClickListener);
        //测试 ExpandableListView
        Button expandableListViewButton = (Button) findViewById(R.id.but_expandable_list);
        expandableListViewButton.setOnClickListener(myOnClickListener);
        //测试 ForegroundService
        Button foregroundServiceButton = (Button) findViewById(R.id.but_Foreground_service);
        foregroundServiceButton.setOnClickListener(myOnClickListener);

    }
}
