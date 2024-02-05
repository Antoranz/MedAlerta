package com.example.myapplication;



import static com.example.myapplication.GetDataAsync.getDataAsync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CargandoConfiguracionActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private int progressStatus = 0;
    SessionManager sessionManager;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargando_configuraciones);

        sessionManager = new SessionManager(this);

           if (sessionManager.isLoggedIn()) {

                if(sessionManager.isVerificated()){

                    initAlarms();




                }else {
                    Intent intent = new Intent(this, ConfirmationActivity.class);
                    startActivity(intent);
                }



            } else {
                Intent intent = new Intent(this, IniciarSesionActivity.class);
                startActivity(intent);
            }








    }

    public void initAlarms(){

        progressBar = findViewById(R.id.progressBar);

        // Creamos un hilo para simular la carga
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;

                // Actualizamos el progreso de la barra en el hilo principal
                handler.post(new Runnable() {
                    public void run() {

                        progressBar.setProgress(progressStatus);

                    }

                });

                try {
                    // Simulamos un tiempo de espera
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            Executor executor = Executors.newSingleThreadExecutor();
            String urlServidor = "http://10.0.2.2:3000/pacientes/obtenerAlarmas/" + sessionManager.getUserId() ;

            getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        Log.i("DOCTORES",jsonArray.toString());


                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e("ObtenerDoctoresTask", "Error al procesar JSON", e);
                    }
                }
            });


        }).start();


    }

}
