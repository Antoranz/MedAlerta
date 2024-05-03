package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.manager.SessionManager;
import java.util.Random;


public class ChangePasswordActivity extends AppCompatActivity {

    SessionManager sessionManager ;
    Button reenvioButton, checKButton;
    EditText codigoText,password1,password2;
    Integer numeroAleatorio = generarNumeroAleatorio();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sessionManager = new SessionManager(this);
        codigoText = findViewById(R.id.editTextCodigo);
        password1 = findViewById(R.id.editTextPassword);
        password2 = findViewById(R.id.editTextPassword2);

        reenvioButton = findViewById(R.id.idReenvio);
        checKButton = findViewById(R.id.idComprobar);

        checKButton.setOnClickListener(v -> {

            if(password1.getText().toString().isEmpty() || password2.getText().toString().isEmpty()) {
                Toast.makeText(this,"Los campos de contraseña no pueden estar vacíos",Toast.LENGTH_LONG).show();
            } else if(!password1.getText().toString().equals(password2.getText().toString())) {
                Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
            } else if(codigoText.getText().toString().equals(numeroAleatorio.toString())) {
                Controller.getInstance().editarPassword(sessionManager.getUserId(),password1.getText().toString(),this,MainActivity.class);
            } else {
                Toast.makeText(this,"Código introducido incorrecto",Toast.LENGTH_LONG).show();
            }


        });

        reenvioButton.setOnClickListener(v -> {

            numeroAleatorio = generarNumeroAleatorio();
            Controller.getInstance().validarPaciente(numeroAleatorio.toString(),sessionManager.getEmail(),this);

        });
        Controller.getInstance().validarPaciente(numeroAleatorio.toString(),sessionManager.getEmail(),this);
    }


    private Integer generarNumeroAleatorio() {
        // Crear un objeto Random
        Random random = new Random();

        Integer numeroAleatorio = random.nextInt(90000) + 10000;

        return numeroAleatorio;
    }

}