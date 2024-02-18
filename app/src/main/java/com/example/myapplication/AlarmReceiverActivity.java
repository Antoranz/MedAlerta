package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.utils.SessionManager;

public class AlarmReceiverActivity extends BroadcastReceiver {

    SessionManager sessionManager;
    @Override
    public void onReceive(Context context, Intent intent) {

        sessionManager = new SessionManager(context);

        if(sessionManager.isLoggedIn()){
            Notificaciones.lanzarNotificacion(context);
        }

        Log.i("WALKIRIA", " ALARM RECEIVED!!!");


    }
}