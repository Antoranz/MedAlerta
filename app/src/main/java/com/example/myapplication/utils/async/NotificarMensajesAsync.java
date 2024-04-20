package com.example.myapplication.utils.async;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.myapplication.data.Mensaje;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.adapters.MensajesListAdapter;
import com.example.myapplication.utils.manager.NotificacionesManager;

import java.util.LinkedList;

public class NotificarMensajesAsync {
    private Handler mHandler;
    private boolean mShouldStop = false;
    private Context context;
    private String dni;


    public NotificarMensajesAsync(String dni, Context context) {
        mHandler = new Handler(Looper.getMainLooper());
        this.dni = dni;
        this.context = context;
    }

    public void startTask() {
        mHandler.postDelayed(updateRunnable, 10000); // Ejecutar la tarea cada 10 segundos

    }

    public void stopTask() {
        mShouldStop = true;
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mShouldStop) {


                obtenerConsultasSinLeer();
                NotificacionesManager.lanzarNotificacionMensajeNoLeido(context);


                mHandler.postDelayed(updateRunnable, 10000); // Ejecutar la tarea cada 10 segundos

            }
        }
    };

    private LinkedList<String> obtenerConsultasSinLeer() {
        LinkedList<String> mensajesNoLeidos = Controller.getInstance().obtenerMensajesNoLeidos(dni);
        Log.d("TAG", "Mensajes no leídos obtenidos: " + mensajesNoLeidos);
        return mensajesNoLeidos;
    }
}
