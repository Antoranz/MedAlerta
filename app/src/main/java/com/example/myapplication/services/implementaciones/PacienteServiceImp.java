package com.example.myapplication.services.implementaciones;

import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.data.Paciente;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.services.PacienteService;
import com.example.myapplication.utils.async.GetDataAsync;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.utils.manager.NavigationManager;

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

    @Override
    public void updatePaciente(Paciente paciente,Context context) {
        Executor executor2 = Executors.newSingleThreadExecutor();
        String urlServidor2 = ConfigApi.BASE_URL + "pacientes/usuario/editarPaciente/" + paciente.getDni();

        JSONObject postData = new JSONObject();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            String fechaFormateada = sdf.format(paciente.getFechaDeNacimiento());
            postData.put("Nombre", paciente.getNombre());
            postData.put("Apellidos", paciente.getApellidos());
            postData.put("FechaDeNacimiento", fechaFormateada);
            postData.put("Direccion", paciente.getDireccion());
            postData.put("CodigoPostal", paciente.getCodigoPostal());
            postData.put("telefono", paciente.getTelefono());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Context applicationContext = context.getApplicationContext(); // Almacenar una referencia al contexto

        PostDataAsync.postDataAsync(urlServidor2, executor2, (PostDataAsync.OnTaskCompleted) result -> {

            if (context != null) {
                ((Activity) context).runOnUiThread(() -> {
                    if(result!=null){
                        Toast.makeText(context, "Registro finalizado correctamente", Toast.LENGTH_LONG).show();
                        NavigationManager.getInstance().navigateToDestination(context, MainActivity.class);
                    }else{
                        Toast.makeText(context, "Error al modificar", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.e("PacienteServiceImp", "El contexto es nulo");
            }
        }, "POST", postData.toString());


    }
}
