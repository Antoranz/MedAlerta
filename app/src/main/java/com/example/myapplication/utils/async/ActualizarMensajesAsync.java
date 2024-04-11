package com.example.myapplication.utils.async;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.myapplication.data.Mensaje;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.adapters.MensajesListAdapter;

import java.util.LinkedList;

public class ActualizarMensajesAsync {
    private Handler mHandler;
    private boolean mShouldStop = false;
    private MensajesListAdapter mAdapter;
    private Context context;
    private Long idConsulta;

    public ActualizarMensajesAsync(MensajesListAdapter adapter,Long idConsulta, Context context) {
        mAdapter = adapter;
        mHandler = new Handler(Looper.getMainLooper());
        this.idConsulta = idConsulta;
        this.context = context;
    }

    public void startTask() {
        mHandler.postDelayed(updateRunnable, 2000); // Ejecutar la tarea cada 2 segundos
    }

    public void stopTask() {
        mShouldStop = true;
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mShouldStop) {
                LinkedList<Mensaje> newMensajes = obtenerMensajesDelServidor();
                mHandler.post(() -> mAdapter.setMensajesList(newMensajes));

                // Vuelve a programar esta tarea para que se ejecute nuevamente después de 2 segundos
                mHandler.postDelayed(this, 2000);
            }
        }
    };

    private LinkedList<Mensaje> obtenerMensajesDelServidor() {
        return Controller.getInstance().getAllMensajes(context,idConsulta);
    }
}
