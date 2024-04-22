package com.example.myapplication.services.implementaciones;

import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;

import android.content.Context;

import com.example.myapplication.data.Cita;
import com.example.myapplication.data.Consulta;
import com.example.myapplication.services.CitaService;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.async.GetDataAsync;

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
                        // Parsear la fecha del String al tipo Date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date fechaHora = dateFormat.parse(jsonCita.getString("fecha_hora"));
                        int duracion = jsonCita.getInt("duracion");


                        Cita cita = new Cita(id, dniDoctor, dniPaciente, fechaHora, duracion);
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
}
