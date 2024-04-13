package com.example.myapplication.utils.async;

import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificarMensajesAsync extends Service {

    private String dni;
    private Handler handler = new Handler();

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            sendRequestToServer();
            // Ejecutar este runnable nuevamente después de 30 segundos
            handler.postDelayed(this, 30 * 1000);
        }
    };

    public NotificarMensajesAsync(String dni) {
        this.dni=dni;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(runnableCode);
        // Si el sistema mata el servicio, reiniciar
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Detener el handler cuando se destruye el servicio
        handler.removeCallbacks(runnableCode);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendRequestToServer() {

        Controller.getInstance().obtenerMensajesNoLeidos(dni);

    }
}
