package com.example.myapplication.activities;


import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import com.example.myapplication.utils.Controller;

import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;

import java.util.Random;



public class ConfirmationActivity extends AppCompatActivity {

    SessionManager sessionManager ;
    Button reenvioButton, checKButton;
    EditText codigoText;
    Integer numeroAleatorio = generarNumeroAleatorio();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);

        sessionManager = new SessionManager(this);
        codigoText = findViewById(R.id.editTextNumber);
        reenvioButton = findViewById(R.id.idReenvio);
        checKButton = findViewById(R.id.idComprobar);

        checKButton.setOnClickListener(v -> {
            if(codigoText.getText().toString().equals(numeroAleatorio.toString())){
                sessionManager.verificateEmail();
                NavigationManager.getInstance().navigateToDestination(this,CargandoConfiguracionActivity.class);
            }else{
                Toast.makeText(this,"codigo introducido incorrecto",Toast.LENGTH_LONG).show();
            }
        });

        reenvioButton.setOnClickListener(v -> {

            numeroAleatorio = generarNumeroAleatorio();
            Controller.getInstance().validarPaciente(numeroAleatorio.toString(),sessionManager.getEmail(),this);

        });

        if (sessionManager.isVerificated()) {
            NavigationManager.getInstance().navigateToDestination(this,CargandoConfiguracionActivity.class);

        } else {
            Controller.getInstance().validarPaciente(numeroAleatorio.toString(),sessionManager.getEmail(),this);
        }

    }

    private Integer generarNumeroAleatorio() {

        Random random = new Random();

        Integer numeroAleatorio = random.nextInt(90000) + 10000;

        return numeroAleatorio;
    }

}