package com.example.myapplication.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

                //TODO falta por implementar el cambio de contraseña

                Intent intent = new Intent(this, IniciarSesionActivity.class);
                startActivity(intent);
            }else{
                makeTextToast("Añade un correo en el campo para cambiar la contraseña");
            }

        });
    }
    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}