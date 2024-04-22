package com.example.myapplication.utils.async;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.example.myapplication.R;
 // Reemplaza MainActivity con la actividad principal de tu aplicación
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.manager.NotificacionesManager;
import java.util.LinkedList;

public class NotificarMensajesAsync extends Service {
    private static final String TAG = "NotificarMensajesAsync";
    private String dni;

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

                if(!obtenerConsultasSinLeer().isEmpty()){
                    NotificacionesManager.lanzarNotificacionMensajeNoLeido(getApplicationContext());
                }
                mHandler.postDelayed(this, 10000); // Ejecutar cada 10 segundos
            }
        };
        mHandler.postDelayed(mRunnable, 10000); // Ejecutar por primera vez después de 10 segundos
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // Obtener el DNI del intent
        dni = intent.getStringExtra("dni");


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
