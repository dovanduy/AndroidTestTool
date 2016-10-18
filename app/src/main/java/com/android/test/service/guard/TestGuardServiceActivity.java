package com.android.test.service.guard;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.test.R;

public class TestGuardServiceActivity extends AppCompatActivity {

    LocalWorkServiceConnection localWorkServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_guard_service);

        localWorkServiceConnection = new LocalWorkServiceConnection();

        Intent localServiceIntent = new Intent();
        localServiceIntent.setAction("com.android.test.service.guard.LocalWorkService");
        localServiceIntent.setPackage(getPackageName());
        startService(localServiceIntent);
        bindService(localServiceIntent, localWorkServiceConnection, Service.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(localWorkServiceConnection);
    }

    /**
     * 连接LocalWorkService的Conntection
     */
    class LocalWorkServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service!=null){
                Log.e("","本地服务开始--非空");
            }else {
                Log.e("","本地服务开始--为空");
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("","本地服务被杀死");
        }

    }

}
