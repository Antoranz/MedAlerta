package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InicioMovilReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("MedAlerta", "Dispositivo Iniciadooooo");

        Notificaciones.lanzarNotificacion(context);

    }
}
