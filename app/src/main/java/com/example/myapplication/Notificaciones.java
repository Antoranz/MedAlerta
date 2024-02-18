package com.example.myapplication;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class Notificaciones {

    private static final String NOTIFICATION_CHANNEL_ID = "nombrecanal2";
    private static final String CHANNEL_NAME = "idcanal2";
    private static final String NOMBRE_CANAL = "nombrecanal1";
    private static final String ID_CANAL = "idcanal1";
    private static NotificationChannel crearCanalNotificaciones(Context context,String nombre_canal, String id_canal){

        NotificationChannel notificationChannel = null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(id_canal, nombre_canal, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(context.getApplicationContext().getResources().getColor(R.color.red));
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationChannel.setVibrationPattern(new long[] {500, 500,500,500,500,500});
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
        }
        return notificationChannel;
    }

    public static void lanzarNotificacion (Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel =null;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            notificationChannel = crearCanalNotificaciones(context,NOMBRE_CANAL,ID_CANAL);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context,ID_CANAL);
        nb.setDefaults(Notification.DEFAULT_ALL);
        nb.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        nb.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        nb.setSmallIcon(android.R.drawable.ic_dialog_info);

        nb.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round));
        nb.setContentTitle("PERRACO LA PASTILLITA");
        nb.setSubText("aviso de alarma");
        nb.setContentTitle("Ereh un crack");

        Intent actividad_destino = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,actividad_destino,PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        nb.setContentIntent(pendingIntent);
        nb.setAutoCancel(true);
        Notification notification = nb.build();

        notificationManager.notify(57,notification);


    }

    public static Notification crearNotificacionSegundoPlano(Context context){
        Notification segundo_plano = null;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder nb = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = crearCanalNotificaciones(context,NOTIFICATION_CHANNEL_ID,CHANNEL_NAME);
            notificationManager.createNotificationChannel(nc);
        }
        nb = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        nb.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        nb.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        nb.setSmallIcon(android.R.drawable.ic_dialog_info);
        nb.setContentTitle("Comprobando si hay alarmas");
        nb.setAutoCancel(true);
        nb.setDefaults(Notification.DEFAULT_ALL);
        nb.setTimeoutAfter(5000);

        segundo_plano = nb.build();

        Log.i("ComprobandoMensajes", "notificacion en segundo plano");

        return segundo_plano;
    }
}
