package com.example.myapplication.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CrearCitaFragment extends Fragment {

    private EditText editTextDate, editTextTime, motivoConsulta, duracion, tipo;
    private Button confirmarCita;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_cita, container, false);

        sessionManager = new SessionManager(requireContext());

        initGui(rootView);

        return rootView;
    }

    private void initGui(View rootView) {
        editTextDate = rootView.findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(v -> openDatePicker());
        editTextTime = rootView.findViewById(R.id.editTextTime);
        editTextTime.setOnClickListener(v -> openTimePicker());

        motivoConsulta = rootView.findViewById(R.id.motivoConsulta);
        tipo = rootView.findViewById(R.id.tipoCita);
        duracion = rootView.findViewById(R.id.duracionCita);

        confirmarCita = rootView.findViewById(R.id.botonConfirmarCita);

        confirmarCita.setOnClickListener(v -> {

            Executor executor2 = Executors.newSingleThreadExecutor();
            String urlServidor2 = ConfigApi.BASE_URL + "pacientes/crearNotificacionCita/" + sessionManager.getUserId();

            JSONObject postData = new JSONObject();
            try {
                postData.put("tipo", tipo.getText().toString());
                postData.put("motivo", motivoConsulta.getText().toString());
                postData.put("fecha", editTextDate.getText().toString());
                postData.put("hora", editTextTime.getText().toString());
                postData.put("duracion", duracion.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PostDataAsync.postDataAsync(urlServidor2, executor2, result -> requireActivity().runOnUiThread(() -> {
                if (result != null) {
                    Toast.makeText(requireContext(), "Consulta solicitada correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireContext(), MainActivity.class));
                } else {
                    Toast.makeText(requireContext(), "Error al solicitar la consulta", Toast.LENGTH_SHORT).show();
                }
            }), "POST", postData.toString());
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay);

                    if (selectedDateCalendar.getTimeInMillis() >= calendar.getTimeInMillis()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String selectedDateFormatted = sdf.format(selectedDateCalendar.getTime());
                        editTextDate.setText(selectedDateFormatted);
                    } else {
                        Toast.makeText(requireContext(), "La fecha seleccionada debe ser mayor o igual que la fecha actual", Toast.LENGTH_SHORT).show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    String selectedTimeFormatted = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    editTextTime.setText(selectedTimeFormatted);
                }, hour, minute, true);
        timePickerDialog.show();
    }
}
