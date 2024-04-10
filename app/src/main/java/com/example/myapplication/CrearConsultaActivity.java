package com.example.myapplication;

import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;


import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.Controller;

import com.example.myapplication.utils.SessionManager;


import java.util.LinkedList;


public class CrearConsultaActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private Button crearConsulta;

    Spinner spinner;

    LinkedList<String> listaDoctores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_consulta);

        sessionManager = new SessionManager(this);


        initIgui();


    }



    private void initIgui() {

        spinner = findViewById(R.id.myspinner);

        listaDoctores = Controller.getInstance().getDoctoresParaConsulta(this,sessionManager.getUserId());


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listaDoctores);


        spinner.setAdapter(adapter);


    }



}
