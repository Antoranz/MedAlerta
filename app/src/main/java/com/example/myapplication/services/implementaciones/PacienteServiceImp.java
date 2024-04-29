package com.example.myapplication.services.implementaciones;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.myapplication.utils.async.GetDataAsync.getDataAsync;
import static com.example.myapplication.utils.async.PostDataAsync.postDataAsync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.activities.ConfirmationActivity;
import com.example.myapplication.activities.IniciarSesionActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.data.Paciente;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.services.PacienteService;
import com.example.myapplication.utils.async.GetDataAsync;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
                                firstObject.getString("dni"),
                                firstObject.getString("Nombre"),
                                firstObject.getString("email"),
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
    @Override
    public void checkPaciente(String dni, String pass,Context context){
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/usuario/checkPaciente";
        SessionManager sessionManager = new SessionManager(context);
        JSONObject postData = new JSONObject();
        try {
            postData.put("dni", dni);
            postData.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
            ((Activity) context).runOnUiThread(() -> {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        if (jsonArray.length() > 0) {

                            JSONObject firstObject = jsonArray.getJSONObject(0);

                            String dniUser = firstObject.getString("dni");
                            String email = firstObject.getString("email");
                            String name = firstObject.getString("Nombre");

                            sessionManager.createSession(dniUser, email,name);
                            NavigationManager.getInstance().navigateToDestination(context,ConfirmationActivity.class);
                        } else {
                            Log.d(TAG, "Error: No data found in the JSON array");
                            Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Log.d(TAG, e.getMessage());
                    }
                }else{
                    Log.d(TAG, "Error deleting account");
                    Toast.makeText(context, "Error interno en el servidor", Toast.LENGTH_LONG).show();
                }
            });

        }, "POST", postData.toString());
    }

    @Override

    public <T> void editarPassword(String dni, String pass, Context context, Class<T> activity) {
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/usuario/editarPassword";

        JSONObject postData = new JSONObject();
        try {
            postData.put("password", pass);
            postData.put("dni", dni);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Realizar la solicitud POST para validar el email del paciente
        postDataAsync(urlServidor, executor, result -> {
            ((Activity) context).runOnUiThread(() -> {
                if (result != null) {
                    Log.d(TAG, "Contraseña cambiada correctamente");
                    Toast.makeText(context, "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show();
                    NavigationManager.getInstance().navigateToDestination(context,activity);
                } else {
                    Log.d(TAG, "Error al cambiar password");
                    Toast.makeText(context, "Error al cambiar password", Toast.LENGTH_LONG).show();
                }
            });
        }, "POST", postData.toString());
    }
    @Override
    public void validarPaciente(String numeroAleatorio, String email, Context context){
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/usuario/validarPaciente";

        JSONObject postData = new JSONObject();
        try {
            postData.put("codigo", numeroAleatorio);
            postData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postDataAsync(urlServidor, executor, result -> {
            ((Activity)context).runOnUiThread(() -> {
                if (result != null) {
                    Log.d(TAG, "Correo enviado");
                    Toast.makeText(context, "Correo enviado a " + email, Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "Error al enviar correo");
                    Toast.makeText(context, "Error al enviar correo", Toast.LENGTH_LONG).show();
                }
            });
        }, "POST", postData.toString());
    }

    @Override
    public void bajaPaciente(Context context) {
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/usuario/bajaPaciente";
        SessionManager sessionManager = new SessionManager(context);
        JSONObject postData = new JSONObject();
        try {
            postData.put("dniPaciente", sessionManager.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
            ((Activity)context).runOnUiThread(() -> {
                if (result != null) {
                    Log.d(TAG, "Account deleted successfully");
                    Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_LONG).show();
                    sessionManager.logout();
                    NavigationManager.getInstance().navigateToDestination(context,IniciarSesionActivity.class);

                }else{
                    Log.d(TAG, "Error deleting account");
                    Toast.makeText(context, "Error deleting account", Toast.LENGTH_LONG).show();
                }

            });

        }, "POST", postData.toString());
    }

    @Override
    public void registrarPaciente(Paciente paciente, Context context) {
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/usuario/registrarPaciente";

        JSONObject postData = new JSONObject();
        try {
            DateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFormateadaBD = formatoBD.format(paciente.getFechaDeNacimiento());

            postData.put("Nombre", paciente.getNombre());
            postData.put("Apellidos",paciente.getApellidos());
            postData.put("FechaDeNacimiento", fechaFormateadaBD);
            postData.put("Direccion", paciente.getDireccion());
            postData.put("CodigoPostal",paciente.getCodigoPostal() );
            postData.put("telefono", paciente.getTelefono());
            postData.put("email",paciente.getEmail());
            postData.put("DNI", paciente.getDni());
            postData.put("password", paciente.getPassword());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
            ((Activity)context).runOnUiThread(() -> {
                if (result != null) {

                    Log.d(TAG, "Registro finalizado correctamente");
                    Toast.makeText(context, "Registro finalizado correctamente", Toast.LENGTH_LONG).show();
                    NavigationManager.getInstance().navigateToDestination(context,IniciarSesionActivity.class);

                }else{
                    Log.d(TAG, "Error al registrar");
                    Toast.makeText(context, "Error al registrar", Toast.LENGTH_LONG).show();
                }
            });
        }, "POST", postData.toString());
    }

}
