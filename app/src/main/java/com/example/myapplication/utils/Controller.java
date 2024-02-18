package com.example.myapplication.utils;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.myapplication.AlarmReceiverActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Controller {
    static Controller instance;

    public static Controller getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new Controller();
        return instance;
    }

    public void creacionAlarma(int i, Long timestamp, Context ctx) {

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiverActivity.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);

        // Convertir el timestamp a una fecha y hora legibles
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDateTime = sdf.format(date);

        Log.i("ALARMAS", "Alarma creada: ID " + i + ", timestamp " + formattedDateTime);

    }

}