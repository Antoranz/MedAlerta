package com.example.myapplication.services.implementaciones;

import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.data.Cita;
import com.example.myapplication.services.CitaService;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.async.GetDataAsync;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CitaServiceImp implements CitaService{

    private static CitaServiceImp instance;

    public static CitaServiceImp getInstance() {

        if(instance == null){
            instance = new CitaServiceImp();
        }
        return instance;
    }
    public LinkedList<Cita> getAllCitas(Context c, String dni){
        LinkedList<Cita> listaCitas = new LinkedList<>();

        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/obtenerCitasPaciente/" + dni;

        CountDownLatch latch = new CountDownLatch(1);

        getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonCita = jsonArray.getJSONObject(i);

                        long id = jsonCita.getLong("id");
                        String dniDoctor = jsonCita.getString("doctor_dni");
                        String dniPaciente = jsonCita.getString("paciente_dni");
                        String nombreDoctor = jsonCita.getString("nombre_doctor");
                        String apellidosDoctor = jsonCita.getString("apellidos_doctor");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date fechaHora = dateFormat.parse(jsonCita.getString("fecha_hora"));
                        int duracion = jsonCita.getInt("duracion");


                        Cita cita = new Cita(id, dniDoctor, dniPaciente, fechaHora, duracion , nombreDoctor, apellidosDoctor);
                        listaCitas.add(cita);
                    }

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                latch.countDown();
            }
        });

        try {

            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return listaCitas;
    }

    @Override
    public void crearNotificacionCita(Context context, String dniDoctorSeleccionado, String tipo, String motivo, String date, String time) {
        Executor executor2 = Executors.newSingleThreadExecutor();
        SessionManager sessionManager = new SessionManager(context);
        String urlServidor2 = ConfigApi.BASE_URL + "pacientes/crearNotificacionCita/" + sessionManager.getUserId();

        JSONObject postData = new JSONObject();
        try {
            postData.put("doctor_dni", dniDoctorSeleccionado);
            postData.put("tipo", tipo);
            postData.put("motivo", motivo);
            postData.put("fecha", date);
            postData.put("hora", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostDataAsync.postDataAsync(urlServidor2, executor2, result -> ((Activity) context).runOnUiThread(() -> {
            if (result != null) {
                Toast.makeText(context, "Consulta solicitada correctamente", Toast.LENGTH_SHORT).show();
                NavigationManager.getInstance().navigateToDestination(context,MainActivity.class);
            } else {
                Toast.makeText(context, "Error al solicitar la consulta", Toast.LENGTH_SHORT).show();
            }
        }), "POST", postData.toString());
    }
}
