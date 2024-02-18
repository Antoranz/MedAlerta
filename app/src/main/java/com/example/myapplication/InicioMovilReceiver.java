package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.utils.SessionManager;

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
