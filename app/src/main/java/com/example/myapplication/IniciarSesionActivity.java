package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.GetDataAsync.getDataAsync;
import static com.example.myapplication.PostDataAsync.postDataAsync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IniciarSesionActivity extends AppCompatActivity {

    TextView logIn, changePassword;
    EditText email, password;
    Button inicio;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        sessionManager = new SessionManager(this);

        email = findViewById(R.id.idEmail);
        password = findViewById(R.id.idPassword);

        inicio = findViewById(R.id.editButton);
        inicio.setOnClickListener(v -> {
            if(email.getText().toString() != "" && password.getText().toString() != ""){

                Executor executor = Executors.newSingleThreadExecutor();
                String urlServidor = "http://10.0.2.2:3000/pacientes/checkPaciente";

                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", email);
                    postData.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
                    if (result != null) {
                        JSONArray jsonArray = new JSONArray(result);

                        if (jsonArray.length() > 0) {

                            JSONObject firstObject = jsonArray.getJSONObject(0);

                            String dni = firstObject.getString("DNI");
                            String email = firstObject.getString("email");

                            sessionManager.createSession(dni, email);

                            Intent intent = new Intent(this, IniciarSesionActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "Error: No data found in the JSON array");
                            makeTextToast("Credenciales incorrectas");
                        }

                    }else{
                        Log.d(TAG, "Error deleting account");
                        makeTextToast("Error interno en el servidor");
                    }
                }, "POST", postData.toString());

            }else{
                makeTextToast("No puede haber ningun campo vacio");
            }
        });

        /*
        logIn = findViewById(R.id.crearCuentaId);
        logIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrarActivity.class);
            startActivity(intent);
        });
        */

        changePassword = findViewById(R.id.changePasswordId);
        changePassword.setOnClickListener(v -> {
            if(!email.getText().toString().isEmpty()){

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