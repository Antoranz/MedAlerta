package com.example.myapplication.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.utils.manager.NotificacionesManager;

public class NotificationButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("ACTION_STOP_MEDIA_PLAYER".equals(action)) {

            if (NotificacionesManager.mp != null && NotificacionesManager.mp.isPlaying()) {
                NotificacionesManager.mp.stop();
                NotificacionesManager.mp.release();
                NotificacionesManager.mp = null;
            }
        }
    }
}
