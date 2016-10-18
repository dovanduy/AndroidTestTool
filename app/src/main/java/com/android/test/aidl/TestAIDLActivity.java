package com.android.test.aidl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.test.R;

public class TestAIDLActivity extends AppCompatActivity {

    private static final String TAG = "TestAIDLActivity";

    //Service接口对象
    IPlayController serviceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aidl);

        //开启服务-bindService
        Intent loaclServiceIntent = new Intent();
        loaclServiceIntent.setAction("com.android.test.aidl.PlayerService");//你定义的service的action
        loaclServiceIntent.setPackage(getPackageName());

        this.bindService(loaclServiceIntent, serviceCon, Service.BIND_AUTO_CREATE);

        //设置监听器--
        View.OnClickListener myOnClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (serviceController!=null){
                    try {
                        switch (v.getId()){
                            case R.id.but_getState:
                                String result = serviceController.getState();
                                if (result!=null){
                                    Log.e(TAG,result);
                                }
                                break;
                            case R.id.but_getMusicInfo:
                                serviceController.getMusicInfo();
                                break;
                            case R.id.but_getProgress:
                                serviceController.getProgress();
                                break;
                            case R.id.but_play:
                                serviceController.play();
                                break;
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG,"serviceController为空！");
                }

            }
        };


        //测试 aidl
        Button getStateButton = (Button) findViewById(R.id.but_getState);
        getStateButton.setOnClickListener(myOnClickListener);
    }


    private ServiceConnection serviceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected");
            serviceController = IPlayController.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService( serviceCon);
    }
}
