package com.example.myapplication.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InicioMovilReceiver extends BroadcastReceiver {

    SessionManager sessionManager;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("MedAlerta", "Dispositivo Iniciadooooo");

        sessionManager = new SessionManager(context);

        if(sessionManager.isLoggedIn()){
            Notificaciones.notificacionComprobandoAlarmas(context);
        }


    }
}
