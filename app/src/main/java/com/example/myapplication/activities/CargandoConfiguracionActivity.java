package com.example.myapplication.activities;



import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.async.ActualizarMensajesAsync;
import com.example.myapplication.utils.async.GetDataAsync;
import com.example.myapplication.R;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.async.NotificarMensajesAsync;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;

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

                    Intent intentService = new Intent(this, NotificarMensajesAsync.class);
                    intentService.putExtra("dni", sessionManager.getUserId());
                    startService(intentService);

                }else {
                    NavigationManager.getInstance().navigateToDestination(this, ConfirmationActivity.class);
                }

            } else {
               NavigationManager.getInstance().navigateToDestination(this,IniciarSesionActivity.class);
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
            String urlServidor = ConfigApi.BASE_URL+"pacientes/obtenerAlarmas/" + sessionManager.getUserId() ;

            getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
                if (result != null) {
                    int idAlarmaActual=0;

                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        Log.i("DOCTORES",jsonArray.toString());

                        long currentTimeMillis = System.currentTimeMillis();

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
                            calendar.add(Calendar.DAY_OF_MONTH, 1); // Suma un día al día que has recogido de la base de datos
                            String[] horaArray = horaPrimeraToma.split(":");
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaArray[0]));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(horaArray[1]));
                            calendar.set(Calendar.SECOND, Integer.parseInt(horaArray[2]));

                            // Obtén el timestamp

                            long timestamp = calendar.getTimeInMillis();
                            int intervaloHoras = 24 / tomasAlDia;

                            Calendar calInicio = Calendar.getInstance();
                            calInicio.setTime(dateInicio);

                            Calendar calFin = Calendar.getInstance();
                            calFin.setTime(dateFin);

                            long diferenciaEnMillis = calFin.getTimeInMillis() - calInicio.getTimeInMillis();
                            int diasEntreFechas = (int) (diferenciaEnMillis / (1000 * 60 * 60 * 24)) + 1;

                            Log.i("diasEntreFehcas", "diasentrefechas " + diasEntreFechas);


                                for (int k = 0; k < diasEntreFechas; k++) {
                                    int diaActual = calendar.get(Calendar.DAY_OF_MONTH);


                                    for (int j = 0; j < tomasAlDia; j++) {

                                        if(idAlarmaActual <= idAlarma){
                                            // Crea la alarma

                                            if (timestamp > currentTimeMillis) {
                                                Controller.getInstance().creacionAlarma(idAlarma, timestamp, medicamento,dosis, this);
                                            }else {
                                                Log.i("TIEMPO_PASADO", "La alarma "+ idAlarma+ " ha pasado el tiempo actual");
                                            }

                                            calendar.add(Calendar.HOUR_OF_DAY, intervaloHoras);
                                            timestamp = calendar.getTimeInMillis();

                                            idAlarma++;
                                            idAlarmaActual = idAlarma;
                                        }else{
                                            // Crea la alarma
                                            if (timestamp > currentTimeMillis) {
                                                Controller.getInstance().creacionAlarma(idAlarmaActual, timestamp,medicamento,dosis, this);
                                            }else {
                                                Log.i("TIEMPO_PASADO", "La alarma "+ idAlarmaActual+ " ha pasado el tiempo actual");
                                            }

                                            // Avanza al siguiente horario de la toma
                                            calendar.add(Calendar.HOUR_OF_DAY, intervaloHoras);
                                            timestamp = calendar.getTimeInMillis();
                                            idAlarmaActual++;
                                        }
                                    }

                                    if (calendar.get(Calendar.DAY_OF_MONTH) != diaActual) {

                                        timestamp = calendar.getTimeInMillis();
                                    }else{
                                        calendar.add(Calendar.DAY_OF_MONTH, k + 1);
                                        timestamp = calendar.getTimeInMillis();
                                    }


                                }



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
