package com.android.test.service.guard;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.test.Constant;
import com.android.test.MainActivity;
import com.android.test.R;


/**
 * 守护进程Service，防止本应用Service被杀死
 */
public class GuardService extends Service {



    private GuardBinder binder;
    private GuardConnection connection;
    private static boolean needGuard = true;

    /**
     * 构造函数
     */
    public GuardService() {
        super.onCreate();
        binder = new GuardBinder();
        if (connection == null) {
            connection = new GuardConnection();
        }

    }

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
            builder.setTicker("守护进程已开启");
            builder.setContentTitle("GuardService");
            builder.setContentText("守护进程正在运行....");
            notification = builder.build();
            //设置通知默认效果
            notification.flags=Notification.FLAG_SHOW_LIGHTS;
            startForeground(Constant.Notifi_GuardService,notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground( true);
    }

    /**
     * onBind
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        needGuard = true;
        return binder;
    }


    /**
     * Binder
     */
    class GuardBinder extends IGuardController.Stub {

        @Override
        public void pauseGuard() throws RemoteException {
            needGuard = false;
        }

        @Override
        public void continueGuard() throws RemoteException {
            needGuard = true;
        }

        @Override
        public boolean isGuarding() throws RemoteException {
            return needGuard;
        }
    }

    /**
     * ServiceConnection
     */
    class GuardConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service!=null){
                Log.e("","本地服务开始");
            }else {
                Log.e("","本地服务为空");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("","本地服务被杀死");
            if (needGuard){//如果需要守护
                startLocalService();//开启本地服务
            }else {}
        }

    }

    /**
     * 开启本地服务
     */
    void startLocalService(){
        Intent localServiceIntent = new Intent();
        localServiceIntent.setAction("com.android.test.service.guard.LocalWorkService");
        localServiceIntent.setPackage(getPackageName());
        GuardService.this.startService(localServiceIntent);
        GuardService.this.bindService(localServiceIntent, connection, Service.BIND_AUTO_CREATE);
    }
}
