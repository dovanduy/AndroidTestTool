package com.android.test.service.foreground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by elvis on 2016/10/13.
 * 前台Service 不容易被杀死
 */

public class ForegroundService extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
        //设置为前台Service
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
