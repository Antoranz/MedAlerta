package com.example.myapplication.utils.manager;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.utils.receiver.NotificationButtonReceiver;

public class NotificacionesManager {

    private static final String NOTIFICATION_CHANNEL_ID = "nombrecanal2";
    private static final String CHANNEL_NAME = "idcanal2";

    public static MediaPlayer mp;

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

    public static void lanzarNotificacion (Context context,String medicamento, String dosis){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel =null;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            notificationChannel = crearCanalNotificaciones(context,NOMBRE_CANAL,ID_CANAL);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent stopIntent = new Intent(context, NotificationButtonReceiver.class);
        stopIntent.setAction("ACTION_STOP_MEDIA_PLAYER");
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder nb = new NotificationCompat.Builder(context,ID_CANAL);
        nb.setPriority(NotificationCompat.PRIORITY_HIGH);
        nb.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        nb.setSmallIcon(android.R.drawable.ic_dialog_info);
        nb.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round));
        nb.setContentTitle(medicamento + ", dosis: " + dosis);
        nb.setDeleteIntent(stopPendingIntent);
        nb.addAction(android.R.drawable.ic_media_pause, "Detener", stopPendingIntent);


        mp = MediaPlayer.create(context,R.raw.alarmclock);
        mp.setLooping(true);
        mp.start();

        nb.setContentIntent(stopPendingIntent);
        nb.setAutoCancel(true);
        Notification notification = nb.build();

        notificationManager.notify(57,notification);
    }

    public static void lanzarNotificacionMensajeNoLeido (Context context){
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

        nb.setContentTitle("MENSAJES NO LEIDOS");

        Intent actividad_destino = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,actividad_destino,PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        nb.setContentIntent(pendingIntent);
        nb.setAutoCancel(true);
        Notification notification = nb.build();

        notificationManager.notify(57,notification);
    }

    public static void notificacionComprobandoAlarmas(Context context){


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
        nb.setContentTitle("Configurando las alarmas");
        nb.setSubText("configurando alarmas inicio movil");
        nb.setAutoCancel(true);


        Notification notification = nb.build();

        notificationManager.notify(56,notification);

        // Cancelar la notificación después de 5 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.cancel(56);
            }
        }, 5000); // 5000 milisegundos equivalen a 5 segundos

    }
}
