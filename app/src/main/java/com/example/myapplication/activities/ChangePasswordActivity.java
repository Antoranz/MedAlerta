package com.example.myapplication.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.utils.async.PostDataAsync.postDataAsync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.manager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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
            //TODO
            if(!password1.getText().toString().equals(password2.getText().toString())){
                Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
            }else if(codigoText.getText().toString().equals(numeroAleatorio.toString())){

                Executor executor = Executors.newSingleThreadExecutor();
                String urlServidor = ConfigApi.BASE_URL+"pacientes/editarPassword";

                JSONObject postData = new JSONObject();
                try {
                    postData.put("password", password1.getText().toString());
                    postData.put("email", sessionManager.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Realizar la solicitud POST para validar el email del paciente
                postDataAsync(urlServidor, executor, result -> {
                    runOnUiThread(() -> {
                        if (result != null) {

                            Log.d(TAG, "Contraseña cambiada correctamente");
                            Toast.makeText(this, "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "Error al cambiar password");
                            Toast.makeText(this, "Error al cambiar password", Toast.LENGTH_LONG).show();
                        }
                    });
                }, "POST", postData.toString());




            }else{
                Toast.makeText(this,"codigo introducido incorrecto",Toast.LENGTH_LONG).show();
            }

        });

        reenvioButton.setOnClickListener(v -> {

            numeroAleatorio = generarNumeroAleatorio();
            Executor executor = Executors.newSingleThreadExecutor();
            String urlServidor = ConfigApi.BASE_URL+"pacientes/validarPaciente";

            JSONObject postData = new JSONObject();
            try {
                postData.put("codigo", numeroAleatorio.toString());
                postData.put("email", sessionManager.getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Realizar la solicitud POST para validar el email del paciente
            postDataAsync(urlServidor, executor, result -> {
                runOnUiThread(() -> {
                    if (result != null) {
                        Log.d(TAG, "Correo enviado");
                        Toast.makeText(this, "Correo enviado a " + sessionManager.getEmail(), Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "Error al enviar correo");
                        Toast.makeText(this, "Error al enviar correo", Toast.LENGTH_LONG).show();
                    }
                });
            }, "POST", postData.toString());

        });



            Executor executor = Executors.newSingleThreadExecutor();
            String urlServidor = ConfigApi.BASE_URL+"pacientes/validarPaciente";

            JSONObject postData = new JSONObject();
            try {
                postData.put("codigo", numeroAleatorio.toString());
                postData.put("email", sessionManager.getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Realizar la solicitud POST para validar el email del paciente
            postDataAsync(urlServidor, executor, result -> {
                runOnUiThread(() -> {
                    if (result != null) {
                        Log.d(TAG, "Correo enviado");
                        Toast.makeText(this, "Correo enviado a " + sessionManager.getEmail(), Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "Error al enviar correo");
                        Toast.makeText(this, "Error al enviar correo", Toast.LENGTH_LONG).show();
                    }
                });
            }, "POST", postData.toString());



    }


    private Integer generarNumeroAleatorio() {
        // Crear un objeto Random
        Random random = new Random();

        // Generar un número aleatorio entre 10000 y 99999 (ambos inclusive)
        Integer numeroAleatorio = random.nextInt(90000) + 10000;

        return numeroAleatorio;
    }

    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}