package com.example.myapplication.activities;

import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;


import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.Doctor;
import com.example.myapplication.utils.Controller;

import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


public class CrearConsultaActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private Button crearConsulta;

    private EditText tituloConsulta;

    Spinner spinner;

    LinkedList<Doctor> listaDoctores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_consulta);

        sessionManager = new SessionManager(this);


        initIgui();


    }
    private void initIgui() {

        spinner = findViewById(R.id.myspinner);
        crearConsulta = findViewById(R.id.buttonCrearConsulta);
        tituloConsulta = findViewById(R.id.editTextTituloConsulta);

        listaDoctores = Controller.getInstance().getDoctoresParaConsulta(this,sessionManager.getUserId());


        ArrayAdapter<Doctor> adapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listaDoctores);

        spinner.setAdapter(adapter);

        crearConsulta.setOnClickListener(v -> {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            String titulo = tituloConsulta.getText().toString();

            // Obtener el doctor seleccionado del Spinner
            Doctor doctorSeleccionado = (Doctor) spinner.getSelectedItem();
            String dniDoctorSeleccionado = doctorSeleccionado.getDni();

            Controller.getInstance().postCrearConsulta(dniDoctorSeleccionado,sessionManager.getUserId().toString(),titulo,timeStamp);
            NavigationManager.getInstance().navigateToDestination(this,ConsultasActivity.class);
        });


    }



}
