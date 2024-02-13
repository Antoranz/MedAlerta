package com.example.myapplication;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class NotificationServiceActivity extends IntentService {

    private NotificationManager notificationManager;
    private static int NOTIFICATION_ID = 1;

    public NotificationServiceActivity(String name) {
        super(name);
    }

    public NotificationServiceActivity() {
        super("SERVICE");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if ("ACTION_START_FOREGROUND_SERVICE".equals(action)) {
                handleForegroundService();
            }
        }
    }

    private void handleForegroundService() {
        Context context = getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, MainActivity.class);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = getString(R.string.app_name);
            String id = NOTIFICATION_CHANNEL_ID;
            String title = NOTIFICATION_CHANNEL_ID;
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }

            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            builder = new NotificationCompat.Builder(context, id)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("ALARMAAAAAAAAAAAAAAAAAA")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            Notification notification = builder.build();
            notificationManager.notify(NOTIFICATION_ID, notification);

            // Inicia el servicio en primer plano utilizando startForeground
            startForeground(NOTIFICATION_ID, notification);
        }
    }
}
