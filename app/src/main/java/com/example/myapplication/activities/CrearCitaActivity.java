package com.example.myapplication.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.myapplication.utils.async.PostDataAsync.postDataAsync;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.R;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CrearCitaActivity extends AppCompatActivity {

    private EditText editTextDate,editTextTime,motivoConsulta, duracion,tipo;
    private Button confirmarCita;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cita);

        sessionManager = new SessionManager(this);

        initIgui();

    }

    private void initIgui() {

        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(v -> openDatePicker());
        editTextTime = findViewById(R.id.editTextTime);
        editTextTime.setOnClickListener(v -> openTimePicker());

        motivoConsulta = findViewById(R.id.motivoConsulta);
        tipo = findViewById(R.id.tipoCita);
        duracion = findViewById(R.id.duracionCita);

        confirmarCita = findViewById(R.id.botonConfirmarCita);

        confirmarCita.setOnClickListener(v -> {

            Executor executor2 = Executors.newSingleThreadExecutor();
            String urlServidor2 = ConfigApi.BASE_URL+"pacientes/crearNotificacionCita/" + sessionManager.getUserId();

            JSONObject postData = new JSONObject();
            try {
                postData.put("tipo", tipo.getText().toString());
                postData.put("motivo", motivoConsulta.getText().toString());
                postData.put("fecha",editTextDate.getText().toString());
                postData.put("hora",editTextTime.getText().toString());
                postData.put("duracion",duracion.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            postDataAsync(urlServidor2, executor2, (PostDataAsync.OnTaskCompleted) result -> {
                runOnUiThread(() -> {
                    if (result != null) {

                        Log.d(TAG, "Consulta solicitada correctamente");

                        Toast.makeText(this, "Consulta solicitada correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Log.d(TAG, "Error al solicitar consulta");
                        Toast.makeText(this, "Error al solicitar la consulta", Toast.LENGTH_SHORT).show();
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

                    // Verificar si la fecha seleccionada es mayor o igual que la fecha actual
                    if (selectedDateCalendar.getTimeInMillis() >= calendar.getTimeInMillis()) {
                        // Formatear la fecha seleccionada en el formato deseado
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String selectedDateFormatted = sdf.format(selectedDateCalendar.getTime());

                        // Actualizar el campo de texto con la fecha seleccionada en el formato deseado
                        editTextDate.setText(selectedDateFormatted);
                    } else {
                        // Mostrar un mensaje de advertencia si la fecha seleccionada es anterior a la fecha actual
                        Toast.makeText(this, "La fecha seleccionada debe ser mayor o igual que la fecha actual", Toast.LENGTH_SHORT).show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }


    private void openTimePicker() {
        // Obtener la hora actual
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Crear un TimePickerDialog y mostrarlo
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Formatear la hora seleccionada en el formato deseado (opcional)
                        String selectedTimeFormatted = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);

                        // Actualizar el campo de texto con la hora seleccionada en el formato deseado
                        editTextTime.setText(selectedTimeFormatted);
                    }
                }, hour, minute, true); // true indica si el formato de 24 horas está habilitado

        // Mostrar el TimePickerDialog
        timePickerDialog.show();
    }

}
