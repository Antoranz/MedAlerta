package com.example.myapplication.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.myapplication.utils.Notificaciones;
import com.example.myapplication.utils.SessionManager;

public class AlarmReceiverActivity extends BroadcastReceiver {

    SessionManager sessionManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        sessionManager = new SessionManager(context);

        if(sessionManager.isLoggedIn()){

            String medicamento = intent.getStringExtra("MEDICAMENTO");
            String dosis = intent.getStringExtra("DOSIS");

            Notificaciones.lanzarNotificacion(context,medicamento,dosis);
        }

        Log.i("WALKIRIA", " ALARM RECEIVED!!!");


    }
}