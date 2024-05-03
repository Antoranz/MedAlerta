package com.example.myapplication.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Paciente;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.R;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;
public class IniciarSesionActivity extends AppCompatActivity {

    TextView logIn, changePassword;
    EditText dni, password;
    Button inicio;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        sessionManager = new SessionManager(this);

        dni = findViewById(R.id.idDNI);
        password = findViewById(R.id.idPassword);

        inicio = findViewById(R.id.editButton);
        inicio.setOnClickListener(v -> {
            if(dni.getText().toString() != "" && password.getText().toString() != ""){
                Controller.getInstance().checkPaciente(dni.getText().toString(), password.getText().toString(), this);
            }else{
                makeTextToast("No puede haber ningun campo vacio");
            }
        });
        logIn = findViewById(R.id.crearCuentaId);
        logIn.setOnClickListener(v -> {
            NavigationManager.getInstance().navigateToDestination(this,RegistrarActivity.class);
        });

        changePassword = findViewById(R.id.changePasswordId);
        changePassword.setOnClickListener(v -> {
            if(!dni.getText().toString().isEmpty()){
                Paciente paciente = Controller.getInstance().getPaciente(dni.getText().toString());

                if(paciente != null){

                    Log.d("paciente en cambiar contraseña", "Paciente es: " + paciente.getDni());

                    NavigationManager.getInstance().navigateToDestinationWithData(this, ForgottenPasswordActivity.class,paciente);


                }else{
                    makeTextToast("Credenciales incorrectas");
                }


            }else{
                makeTextToast("Añade un DNI en el campo DNI para enviarte un correo");
            }

        });
    }
    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}