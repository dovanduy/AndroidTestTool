package com.android.test.service.foreground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;

import com.android.test.Constant;
import com.android.test.MainActivity;
import com.android.test.R;


/**
 * Created by elvis on 2016/10/13.
 *  前台service，可以保持service不被杀死！
 *  &&只有本地的前台Service才不会被RecentAPP干掉，远程的前台Serive会被RecentAPP干掉
 *  在前台service中启动后台service(或者远程service)
 *
 */
public class ForegroundService extends Service {

    private boolean mReflectFlg = false;

    private static final Class<?>[] mSetForegroundSignature = new Class[] { boolean.class };
    private static final Class<?>[] mStartForegroundSignature = new Class[] { int.class , Notification.class };
    private static final Class<?>[] mStopForegroundSignature = new Class[] { boolean.class };

    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();

        mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE );
        try {
            mStartForeground = ForegroundService.class.getMethod("startForeground" , mStartForegroundSignature);
            mStopForeground = ForegroundService.class.getMethod("stopForeground" , mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground = mStopForeground = null;
        }

        try {
            mSetForeground = getClass().getMethod( "setForeground", mSetForegroundSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException( "OS doesn't have Service.startForeground OR Service.setForeground!");
        }
        Intent intent = new Intent( this, MainActivity. class);
        intent.putExtra( "ficationId", Constant.Notifi_ForegroundService);
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(contentIntent); //设置了此项内容之后，点击通知栏的这个消息，就跳转到MainActivity
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle( "测试前台Service-兼容所有Android版本" );
        builder.setContentText( "此Service不容易被杀死哦！可以在此处来开启远程Service" );
        Notification notification = builder.build();

        startForegroundCompat( Constant.Notifi_ForegroundService, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        //在这里我又启动了其他的两个Service,注意，此处启动的是后台的Service
//        startService( new Intent( this, WifiService. class));
//        startService( new Intent( this, VoiceService. class));
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForegroundCompat( Constant.Notifi_ForegroundService);
    }

    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke( this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Log. w("ApiDemos" , "Unable to invoke method" , e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Log. w("ApiDemos" , "Unable to invoke method" , e);
        }
    }

    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat( int id, Notification notification) {
        if ( mReflectFlg) {
            // If we have the new startForeground API, then use it.
            if ( mStartForeground != null) {
                mStartForegroundArgs[0] = Integer. valueOf(id);
                mStartForegroundArgs[1] = notification;
                invokeMethod( mStartForeground, mStartForegroundArgs);
                return;
            }

            // Fall back on the old API.
            mSetForegroundArgs[0] = Boolean. TRUE;
            invokeMethod( mSetForeground, mSetForegroundArgs);
            mNM.notify(id, notification);
        } else {
                   /*
                   * 还可以使用以下方法，当 sdk大于等于5时，调用 sdk现有的方法startForeground设置前台运行，
                   * 否则调用反射取得的 sdk level 5（对应Android 2.0）以下才有的旧方法setForeground设置前台运行
                   */

            if (VERSION. SDK_INT >= 5) {
                startForeground(id, notification);
            } else {
                // Fall back on the old API.
                mSetForegroundArgs[0] = Boolean. TRUE;
                invokeMethod( mSetForeground, mSetForegroundArgs);
                mNM.notify(id, notification);
            }
        }
    }

    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    void stopForegroundCompat( int id) {
        if ( mReflectFlg) {
            // If we have the new stopForeground API, then use it.
            if ( mStopForeground != null) {
                mStopForegroundArgs[0] = Boolean. TRUE;
                invokeMethod( mStopForeground, mStopForegroundArgs);
                return;
            }

            // Fall back on the old API. Note to cancel BEFORE changing the
            // foreground state, since we could be killed at that point.
            mNM.cancel(id);
            mSetForegroundArgs[0] = Boolean. FALSE;
            invokeMethod( mSetForeground, mSetForegroundArgs);
        } else {
                   /*
                   * 还可以使用以下方法，当 sdk大于等于5时，调用 sdk现有的方法stopForeground停止前台运行， 否则调用反射取得的 sdk
                   * level 5（对应Android 2.0）以下才有的旧方法setForeground停止前台运行
                   */

            if (VERSION. SDK_INT >= 5) {
                stopForeground( true);
            } else {
                // Fall back on the old API. Note to cancel BEFORE changing the
                // foreground state, since we could be killed at that point.
                mNM.cancel(id);
                mSetForegroundArgs[0] = Boolean. FALSE;
                invokeMethod( mSetForeground, mSetForegroundArgs);
            }
        }
    }

}