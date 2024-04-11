package com.example.myapplication.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.utils.manager.NotificacionesManager;
import com.example.myapplication.utils.manager.SessionManager;

public class InicioMovilReceiver extends BroadcastReceiver {

    SessionManager sessionManager;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("MedAlerta", "Dispositivo Iniciadooooo");

        sessionManager = new SessionManager(context);

        if(sessionManager.isLoggedIn()){
            NotificacionesManager.notificacionComprobandoAlarmas(context);
        }


    }
}
