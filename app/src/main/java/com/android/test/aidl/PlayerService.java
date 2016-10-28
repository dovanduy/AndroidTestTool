package com.android.test.aidl;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.test.Constant;
import com.android.test.MainActivity;
import com.android.test.R;

/**
 * Created by elvis on 2016/10/13.
 */

public class PlayerService extends Service{



    //属性：

    //AIDL接口对象--进程间通讯
    IPlayController.Stub binder=new IPlayController.Stub() {
        private static final String TAG = "IControlPlayer";
        @Override
        public String getState() throws RemoteException {
            return "getState()";
        }

        @Override
        public Music getMusicInfo() throws RemoteException {
            return null;
        }

        @Override
        public int getProgress() throws RemoteException {
            Log.e(TAG,"getProgress()");
            return 0;
        }

        @Override
        public String play() throws RemoteException {
            return "play()";
        }

        @Override
        public String pause() throws RemoteException {
            return "pause()";
        }

        @Override
        public String stop() throws RemoteException {
            return "stop()";
        }

        @Override
        public String playLast() throws RemoteException {
            return "playLast()";
        }

        @Override
        public String playNext() throws RemoteException {
            return "playNext()";
        }

        @Override
        public int getVolume() throws RemoteException {
            Log.e(TAG,"getVolume()");
            return 0;
        }

        @Override
        public int setVolume(int volume) throws RemoteException {
            Log.e(TAG,"setVolume()");
            return 0;
        }
    };

    /**
     * 构造函数
      */
    public PlayerService() {

    }


    /**
     * onCreate
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

        //前台Service
        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            builder = new Notification.Builder(this);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setTicker("PlayerService Start");
            builder.setContentTitle("AndroidTestTool 测试AIDL");
            builder.setContentText("后台测试Service正在运行....");
            notification = builder.build();
            //设置通知默认效果
            notification.flags=Notification.FLAG_SHOW_LIGHTS;
            startForeground(Constant.Notifi_PlayerService,notification);
        }









    }

    /**
     * onBind
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){

        }
        return START_STICKY;//Service被非人为kill，重启Service
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);//结束前台
    }
}
