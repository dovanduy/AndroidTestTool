package com.android.test.service.guard;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;



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

    @Override
    public void onCreate() {
        super.onCreate();
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
