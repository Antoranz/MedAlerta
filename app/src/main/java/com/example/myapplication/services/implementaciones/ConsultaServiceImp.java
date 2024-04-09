package com.example.myapplication.services.implementaciones;

import static com.example.myapplication.GetDataAsync.getDataAsync;

import com.example.myapplication.GetDataAsync;
import com.example.myapplication.data.Consulta;
import com.example.myapplication.services.ConsultaService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConsultaServiceImp implements ConsultaService {

    private static ConsultaServiceImp instance;

    public static ConsultaServiceImp getInstance() {

        if(instance == null){
            instance = new ConsultaServiceImp();
        }
        return instance;
    }

    @Override
    public LinkedList<Consulta> getAllConsultas(String dni) {

        LinkedList<Consulta> listaConsultas = new LinkedList<>();

        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = "http://10.0.2.2:3000/pacientes/obtenerConsultasPaciente/" + dni;

        getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
            if (result != null) {
                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonConsulta = jsonArray.getJSONObject(i);

                        long id = jsonConsulta.getLong("id");
                        String dniDoctor = jsonConsulta.getString("dni_doctor");
                        String dniPaciente = jsonConsulta.getString("dni_paciente");
                        String titulo = jsonConsulta.getString("titulo");
                        // Parsear la fecha del String al tipo Date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date ultimaFecha = dateFormat.parse(jsonConsulta.getString("ultima_fecha"));
                        int mensajesTotales = jsonConsulta.getInt("mensajes_totales");
                        int notificacionesDoctor = jsonConsulta.getInt("notificaciones_doctor");
                        int notificacionesPaciente = jsonConsulta.getInt("notificaciones_paciente");


                        Consulta consulta = new Consulta(id, dniDoctor, dniPaciente, titulo, ultimaFecha, mensajesTotales, notificacionesDoctor, notificacionesPaciente);
                        listaConsultas.add(consulta);
                    }

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        return listaConsultas;
    }

    @Override
    public List<String> getDoctoresParaConsulta(String dni) {

        List<String> listaNombresDoctores = new ArrayList<>();

        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = "http://10.0.2.2:3000/pacientes/obtenerDoctoresDelPaciente/" + dni;

        getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonConsulta = jsonArray.getJSONObject(i);
                        String nombre = jsonConsulta.getString("nombre");

                        listaNombresDoctores.add(nombre);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return listaNombresDoctores;
    }



}
