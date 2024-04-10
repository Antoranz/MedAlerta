package com.example.myapplication.services.implementaciones;

import static com.example.myapplication.utils.GetDataAsync.getDataAsync;

import com.example.myapplication.data.Mensaje;

import com.example.myapplication.services.MensajeService;

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


public class MensajeServiceImp implements MensajeService {

    private static MensajeServiceImp instance;

    public static MensajeServiceImp getInstance() {

        if(instance == null){
            instance = new MensajeServiceImp();
        }
        return instance;
    }

    @Override
    public LinkedList<Mensaje> getAllMensajes(long idConsulta) {
        LinkedList<Mensaje> listaMensajes = new LinkedList<>();

        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = "http://10.0.2.2:3000/pacientes/obtenerMensajesConsulta/" + idConsulta;

        CountDownLatch latch = new CountDownLatch(1);

        getDataAsync(urlServidor, executor, result -> {
            if (result != null) {
                try {

                    JSONArray jsonArray = new JSONArray(result);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonMensaje = jsonArray.getJSONObject(i);

                        long idMensaje = jsonMensaje.getLong("id_consulta");
                        String mensaje = jsonMensaje.getString("mensaje");
                        int propietario = jsonMensaje.getInt("propietario");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date fecha = dateFormat.parse(jsonMensaje.getString("fecha"));


                        Mensaje mensajeObj = new Mensaje(idConsulta, mensaje, propietario, fecha);
                        listaMensajes.add(mensajeObj);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }


            }

            latch.countDown();
        });

        try {
            // Esperamos a que el CountDownLatch se reduzca a cero, lo que indica que getDataAsync() ha terminado
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return listaMensajes;
    }

}
