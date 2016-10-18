package com.android.test.service.guard;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.test.MainActivity;
import com.android.test.R;


/**
 * 本地工作服务Service（被守护进程保护）
 */

public class LocalWorkService extends Service {


    private static LocalWorkBinder binder;//本Service的binder




    /**
     * 构造函数
     */
    public LocalWorkService() {
        super.onCreate();
        binder = new LocalWorkBinder();
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
            builder.setTicker("后台任务开启");
            builder.setContentTitle("LocalWorkService");
            builder.setContentText("后台任务正在运行....");
            notification = builder.build();
            //设置通知默认效果
            notification.flags=Notification.FLAG_SHOW_LIGHTS;
            startForeground(1,notification);
        }
    }

    /**
     * onStartCommand 执行服务代码
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //如果未开启守护，则开启守护
        startGuardServiceIfNotStart();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * onBind  用于进程间通信
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        //如果未开启守护，则开启守护
        startGuardServiceIfNotStart();
        return binder;
    }

    /**
     * Binder
     */
    class LocalWorkBinder extends ILocalWorkController.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return "LocalWorkService";
        }
    }


    //守护进程相关：-------------------------------------------------------------------------------------

    private static GuardConnection connection;//守护进程的connection
    private static IGuardController guardController;//守护进程controller


    /**
     * ServiceConnection
     */
    class GuardConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service!=null){
                Log.e("","守护服务开始");
                guardController = IGuardController.Stub.asInterface(service);
            }else {
                Log.e("","守护服务为空");
                guardController = null;
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("","守护服务被杀死");
            startGuardService();//开启守护服务
        }

    }


    /**
     * 开启守护服务
     */
    void startGuardService(){
        Intent guardServiceIntent = new Intent();
        guardServiceIntent.setAction("com.android.test.service.guard.GuardService");
        guardServiceIntent.setPackage(getPackageName());
        LocalWorkService.this.startService(guardServiceIntent);
        LocalWorkService.this.bindService(guardServiceIntent, connection, Service.BIND_AUTO_CREATE);
    }

    /**
     * 如果守护进程没有开启，则开启
     */
    void startGuardServiceIfNotStart(){
        try {
            if (guardController==null||!guardController.isGuarding()){
                startGuardService();//开启守护进程
            }
        } catch (RemoteException e) {
            startGuardService();//开启守护进程
        }
    }

}
