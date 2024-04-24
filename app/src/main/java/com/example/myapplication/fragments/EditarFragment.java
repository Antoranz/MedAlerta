package com.example.myapplication.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activities.ChangePasswordActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.data.Paciente;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditarFragment extends Fragment {

    private EditText nameText, surnameText, editTextDate, domicilioText, postalAddressText, phoneText;
    private TextView changePassword;
    private Button editButton;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editar, container, false);
        sessionManager = new SessionManager(requireContext());

        initGui(rootView);
        return rootView;
    }
    private void initGui(View view) {
        nameText = view.findViewById(R.id.idName);
        surnameText = view.findViewById(R.id.idSurname);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(v -> openDatePicker());
        domicilioText = view.findViewById(R.id.idDomicilio);
        postalAddressText = view.findViewById(R.id.editTextTextPostalAddress);
        phoneText = view.findViewById(R.id.editTextPhone);
        changePassword = view.findViewById(R.id.changePasswordId);

        editButton = view.findViewById(R.id.editButton);

        changePassword.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToDestination(requireContext(), ChangePasswordActivity.class);
        });

        Paciente paciente = Controller.getInstance().getPaciente(sessionManager.getUserId());
        nameText.setText(paciente.getNombre());
        surnameText.setText(paciente.getApellidos());

        SimpleDateFormat sdfFormateada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String nuevaFechaFormateada = sdfFormateada.format(paciente.getFechaDeNacimiento());
        editTextDate.setText(nuevaFechaFormateada);

        domicilioText.setText(paciente.getDireccion());
        postalAddressText.setText(paciente.getCodigoPostal());
        phoneText.setText(paciente.getTelefono());


        editButton.setOnClickListener(v -> {
            Executor executor2 = Executors.newSingleThreadExecutor();
            String urlServidor2 = ConfigApi.BASE_URL + "pacientes/usuario/editarPaciente/" + sessionManager.getUserId();

            JSONObject postData = new JSONObject();
            try {
                postData.put("Nombre", nameText.getText().toString());
                postData.put("Apellidos", surnameText.getText().toString());
                postData.put("FechaDeNacimiento", editTextDate.getText().toString());
                postData.put("Direccion", domicilioText.getText().toString());
                postData.put("CodigoPostal", postalAddressText.getText().toString());
                postData.put("telefono", phoneText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PostDataAsync.postDataAsync(urlServidor2, executor2, (PostDataAsync.OnTaskCompleted) result -> {
                requireActivity().runOnUiThread(() -> {
                    if (result != null) {
                        makeTextToast("Registro finalizado correctamente");
                        NavigationManager.getInstance().navigateToDestination(requireContext(), MainActivity.class);
                    } else {
                        makeTextToast("Error al registrar");
                    }
                });
            }, "POST", postData.toString());
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String selectedDateFormatted = sdf.format(selectedDateCalendar.getTime());
                    editTextDate.setText(selectedDateFormatted);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void makeTextToast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show();
    }
}