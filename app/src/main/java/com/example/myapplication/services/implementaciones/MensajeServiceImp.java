package com.example.myapplication.services.implementaciones;

import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;
import static com.example.myapplication.utils.async.PostDataAsync.postDataAsync;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Mensaje;

import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.services.MensajeService;
import com.example.myapplication.utils.async.GetDataAsync;
import com.example.myapplication.utils.async.PostDataAsync;

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


public class MensajeServiceImp extends AppCompatActivity implements MensajeService {

    private static MensajeServiceImp instance;

    public static MensajeServiceImp getInstance() {

        if (instance == null) {
            instance = new MensajeServiceImp();
        }
        return instance;
    }

    @Override
    public LinkedList<Mensaje> getAllMensajes(long idConsulta) {
        LinkedList<Mensaje> listaMensajes = new LinkedList<>();

        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/consulta/obtenerMensajesConsulta/" + idConsulta;

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

    @Override
    public void crearMensaje(long id, String mensaje, long propietario, String fecha) {
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/consulta/crearMensaje";

        JSONObject postData = new JSONObject();
        try {

            postData.put("id_consulta", id);
            postData.put("mensaje", mensaje);
            postData.put("propietario", propietario);
            postData.put("fecha", fecha);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
            runOnUiThread(() -> {
            });
        }, "POST", postData.toString());


    }

    @Override
    public LinkedList<String> obtenerMensajesNoLeidos(String dni) {

        LinkedList<String> listaMensajes = new LinkedList<>();
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/consulta/obtenerMensajesNoLeidos/" + dni;

        CountDownLatch latch = new CountDownLatch(1);

        getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonConsulta = jsonArray.getJSONObject(i);

                        long id_consulta = jsonConsulta.getLong("id_consulta");
                        String titulo = jsonConsulta.getString("titulo");
                        String mensaje = jsonConsulta.getString("mensaje");




                        listaMensajes.add(titulo);

                    }

                } catch (JSONException e) {
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

        return listaMensajes;

    }

    @Override
    public void ponerMensajesComoLeidos(String dni, long id) {
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/consulta/ponerMensajesComoLeidosConsulta" + id;

        JSONObject postData = new JSONObject();
        try {

            postData.put("dni_paciente", dni);
            postData.put("id_consulta", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
            runOnUiThread(() -> {
            });
        }, "POST", postData.toString());
    }

}


