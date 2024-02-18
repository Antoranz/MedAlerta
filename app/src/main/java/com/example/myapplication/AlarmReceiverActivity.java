package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AlarmReceiverActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("WALKIRIA", " ALARM RECEIVED!!!");

        Notificaciones.lanzarNotificacion(context);
        //Intent intent_service = new Intent(context,NotificationServiceActivity.class);

       // if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        //    context.startForegroundService(intent_service);
        //}else{
        //    context.startService(intent_service);
        //}



    }
}