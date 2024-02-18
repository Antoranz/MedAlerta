package com.example.myapplication;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationServiceActivity extends Service {


    public NotificationServiceActivity() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Notification n = Notificaciones.crearNotificacionSegundoPlano(this);

        startForeground(66,n);



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


}