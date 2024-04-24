package com.example.myapplication.services.implementaciones;

import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;

import com.example.myapplication.data.Paciente;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.services.PacienteService;
import com.example.myapplication.utils.async.GetDataAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PacienteServiceImp implements PacienteService {
    private static PacienteServiceImp instance;

    public static PacienteServiceImp getInstance() {

        if (instance == null) {
            instance = new PacienteServiceImp();
        }
        return instance;
    }
    @Override
    public Paciente getPaciente(String dni) {
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL + "pacientes/obtenerPaciente/" + dni;
        final Paciente[] paciente = {null};
        CountDownLatch latch = new CountDownLatch(1);
        getDataAsync(urlServidor, executor, result -> {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {
                        JSONObject firstObject = jsonArray.getJSONObject(0);
                        String fechaString = firstObject.getString("FechaDeNacimiento");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                        Date fecha = sdf.parse(fechaString);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fecha);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        Date nuevaFecha = calendar.getTime();
                        paciente[0] = new Paciente(
                                firstObject.getString("Nombre"),
                                firstObject.getString("Apellidos"),
                                firstObject.getString("CodigoPostal"),
                                nuevaFecha,
                                firstObject.getString("Direccion"),
                                firstObject.getString("telefono"));
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
        return paciente[0];
    }
}
