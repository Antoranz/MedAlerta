package com.example.myapplication.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.data.Doctor;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.manager.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

public class CrearCitaFragment extends Fragment {

    private EditText editTextDate, editTextTime, motivoConsulta, tipo;
    private Button confirmarCita;

    Spinner spinner;

    LinkedList<Doctor> listaDoctores;
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

        confirmarCita = rootView.findViewById(R.id.botonConfirmarCita);

        spinner = rootView.findViewById(R.id.myspinnerCitas);

        listaDoctores = Controller.getInstance().getDoctoresParaConsulta(requireContext(),sessionManager.getUserId());


        ArrayAdapter<Doctor> adapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listaDoctores);

        spinner.setAdapter(adapter);

        confirmarCita.setOnClickListener(v -> {

            if(listaDoctores.isEmpty()){

                Toast.makeText(getContext(), "No hay doctores disponibles para crear citas", Toast.LENGTH_LONG).show();

            }else {
                Doctor doctorSeleccionado = (Doctor) spinner.getSelectedItem();
                String dniDoctorSeleccionado = doctorSeleccionado.getDni();
                if (dniDoctorSeleccionado.isEmpty() || tipo.getText().toString().isEmpty() || motivoConsulta.getText().toString().isEmpty() || editTextDate.getText().toString().isEmpty() || editTextTime.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                } else {
                    Controller.getInstance().crearNotificacionCita(getContext(), dniDoctorSeleccionado, tipo.getText().toString(), motivoConsulta.getText().toString(), editTextDate.getText().toString(), editTextTime.getText().toString());
                }
            }
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
