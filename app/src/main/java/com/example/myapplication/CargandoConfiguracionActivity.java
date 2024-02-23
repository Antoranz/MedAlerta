package com.example.myapplication;



import static com.example.myapplication.GetDataAsync.getDataAsync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            int idAlarma = jsonObject.getInt("id_alarma");
                            int idTratamiento = jsonObject.getInt("id_tratamiento");
                            String medicamento = jsonObject.getString("medicamento");
                            String dosis = jsonObject.getString("dosis");
                            String horaPrimeraToma = jsonObject.getString("hora_primera_toma");
                            int tomasAlDia = jsonObject.getInt("tomas_al_dia");
                            String fechaInicio = jsonObject.getString("fecha_inicio");
                            String fechaFin = jsonObject.getString("fecha_fin");

                            // Parsea la fecha y la hora
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                            Date dateInicio = sdf.parse(fechaInicio);
                            Date dateFin = sdf.parse(fechaFin);

                            // Crea una instancia de Calendar y establece la hora de la primera toma
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(dateInicio);
                            String[] horaArray = horaPrimeraToma.split(":");
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaArray[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(horaArray[1]));
                            calendar.set(Calendar.SECOND, Integer.parseInt(horaArray[2]));

                            // Obtén el timestamp

                            long timestamp = calendar.getTimeInMillis();
                            Log.i("TIEMPO", String.valueOf(timestamp));

                            // Crea la alarma
                            Controller.getInstance().creacionAlarma(idAlarma, timestamp, this);
                        }

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e("ObtenerDoctoresTask", "Error al procesar JSON", e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        }).start();


    }

}
