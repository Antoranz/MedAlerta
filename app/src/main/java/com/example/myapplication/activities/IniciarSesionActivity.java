package com.example.myapplication.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


import static com.example.myapplication.utils.async.PostDataAsync.postDataAsync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.R;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

                Executor executor = Executors.newSingleThreadExecutor();
                String urlServidor = ConfigApi.BASE_URL+"pacientes/checkPaciente";
                
                JSONObject postData = new JSONObject();
                try {
                    postData.put("dni", dni.getText().toString());
                    postData.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
                    runOnUiThread(() -> {
                        if (result != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(result);

                                if (jsonArray.length() > 0) {

                                    JSONObject firstObject = jsonArray.getJSONObject(0);

                                    String dni = firstObject.getString("dni");
                                    String email = firstObject.getString("email");
                                    String name = firstObject.getString("Nombre");

                                    sessionManager.createSession(dni, email,name);

                                    Intent intent = new Intent(this, ConfirmationActivity.class);
                                    startActivity(intent);

                                } else {
                                    Log.d(TAG, "Error: No data found in the JSON array");
                                    makeTextToast("Credenciales incorrectas");
                                }
                            }catch (Exception e){
                                Log.d(TAG, e.getMessage());
                            }
                        }else{
                            Log.d(TAG, "Error deleting account");
                            makeTextToast("Error interno en el servidor");
                        }
                    });

                }, "POST", postData.toString());

            }else{
                makeTextToast("No puede haber ningun campo vacio");
            }
        });


        logIn = findViewById(R.id.crearCuentaId);
        logIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrarActivity.class);
            startActivity(intent);
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