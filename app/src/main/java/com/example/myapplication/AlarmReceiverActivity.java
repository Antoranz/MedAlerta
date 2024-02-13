package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AlarmReceiverActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service1 = new Intent(context, NotificationServiceActivity.class);
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ContextCompat.startForegroundService(context, service1 );
        Log.i("WALKIRIA", " ALARM RECEIVED!!!");


    }
}