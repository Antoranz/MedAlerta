package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.GetDataAsync.getDataAsync;
import static com.example.myapplication.PostDataAsync.postDataAsync;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditarActivity extends AppCompatActivity {

    private EditText nameText,surnameText,editTextDate,domicilioText,postalAddressText,phoneText;
    private TextView changePassword;
    private Button editButton;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar);
        sessionManager = new SessionManager(this);

        initIgui();
    }

    private void initIgui() {

        nameText = findViewById(R.id.idName);
        surnameText = findViewById(R.id.idSurname);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(v -> openDatePicker());
        domicilioText = findViewById(R.id.idDomicilio);
        postalAddressText = findViewById(R.id.editTextTextPostalAddress);
        phoneText = findViewById(R.id.editTextPhone);
        changePassword = findViewById(R.id.changePasswordId);

        editButton = findViewById(R.id.editButton);

        changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        });


        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = "http://10.0.2.2:3000/pacientes/obtenerPaciente/" + sessionManager.getUserId();

        getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
            if (result != null) {
                try {

                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {

                        JSONObject firstObject = jsonArray.getJSONObject(0);

                        nameText.setText(firstObject.getString("Nombre"));
                        surnameText.setText(firstObject.getString("Apellidos"));

                        String fechaString = firstObject.getString("FechaDeNacimiento");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                        Date fecha = sdf.parse(fechaString);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fecha);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        Date nuevaFecha = calendar.getTime();
                        SimpleDateFormat sdfFormateada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String nuevaFechaFormateada = sdfFormateada.format(nuevaFecha);

                        editTextDate.setText(nuevaFechaFormateada);

                        domicilioText.setText(firstObject.getString("Direccion"));
                        postalAddressText.setText(firstObject.getString("CodigoPostal"));
                        phoneText.setText(firstObject.getString("telefono"));



                    } else {
                        Log.d(TAG, "Error: No data found in the JSON array");

                    }

                    Log.i("DOCTORES",jsonArray.toString());
                } catch (JSONException e) {
                    Log.e("ObtenerDoctoresTask", "Error al procesar JSON", e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        editButton.setOnClickListener(v -> {

            Executor executor2 = Executors.newSingleThreadExecutor();
            String urlServidor2 = "http://10.0.2.2:3000/pacientes/editarPaciente/" + sessionManager.getUserId();

            JSONObject postData = new JSONObject();
            try {
                postData.put("Nombre", nameText.getText().toString());
                postData.put("Apellidos",surnameText.getText().toString());
                postData.put("FechaDeNacimiento", editTextDate.getText().toString());
                postData.put("Direccion", domicilioText.getText().toString());
                postData.put("CodigoPostal", postalAddressText.getText().toString());
                postData.put("telefono", phoneText.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            postDataAsync(urlServidor2, executor2, (PostDataAsync.OnTaskCompleted) result -> {
                runOnUiThread(() -> {
                    if (result != null) {

                        Log.d(TAG, "Registro finalizado correctamente");
                        makeTextToast("Registro finalizado correctamente");
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Log.d(TAG, "Error al registrar");
                        makeTextToast("Error al registrar");
                    }
                });
            }, "POST", postData.toString());

        });
    }

    private void openDatePicker() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();

        // Crear un DatePickerDialog y mostrarlo
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Crear un objeto Calendar con la fecha seleccionada
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay);

                    // Formatear la fecha seleccionada en el formato deseado
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String selectedDateFormatted = sdf.format(selectedDateCalendar.getTime());

                    // Actualizar el campo de texto con la fecha seleccionada en el formato deseado
                    editTextDate.setText(selectedDateFormatted);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }



    private Integer generarNumeroAleatorio() {
        // Crear un objeto Random
        Random random = new Random();

        // Generar un número aleatorio entre 10000 y 99999 (ambos inclusive)
        Integer numeroAleatorio = random.nextInt(90000) + 10000;

        return numeroAleatorio;
    }

    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}


