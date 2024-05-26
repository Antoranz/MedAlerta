package com.example.myapplication.utils.async;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Handler;
import android.util.Log;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.manager.NotificacionesManager;
import java.util.LinkedList;

public class NotificarMensajesAsync extends Service {
    private static final String TAG = "NotificarMensajesAsync";
    private String dni;
    private LinkedList<String> mensajesNoLeidosAnteriores = new LinkedList<>();
    private Handler mHandler;
    private Runnable mRunnable;
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("datos_persona", Context.MODE_PRIVATE);
                dni = pref.getString("dni", "");

                LinkedList<String> mensajesNoLeidos = obtenerConsultasSinLeer();

                if (mensajesNoLeidos.size() > mensajesNoLeidosAnteriores.size()) {

                    NotificacionesManager.lanzarNotificacionMensajeNoLeido(getApplicationContext());
                }
                mensajesNoLeidosAnteriores = mensajesNoLeidos;
                mHandler.postDelayed(this, 10000);
            }
        };
        mHandler.postDelayed(mRunnable, 10000);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        dni = intent.getStringExtra("dni");

        SharedPreferences pref = getSharedPreferences("datos_persona", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("dni", dni);
        editor.commit();
        editor.apply();
        return Service.START_STICKY;
    }
    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }
    public void onStop() {
        Log.i(TAG, "onStop()");
    }
    public void onPause() {
        Log.i(TAG, "onPause()");
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate() , service stopped...");
    }
    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }

    private LinkedList<String> obtenerConsultasSinLeer() {
        LinkedList<String> mensajesNoLeidos = Controller.getInstance().obtenerMensajesNoLeidos(dni);
        Log.d(TAG, "Mensajes no leídos obtenidos: " + mensajesNoLeidos);
        return mensajesNoLeidos;
    }
}
