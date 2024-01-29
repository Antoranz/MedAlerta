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

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditarActivity extends AppCompatActivity {

    private EditText nameText,surnameText,dateText,domicilioText,postalAddressText,phoneText;
    private TextView changePassword;
    private Button editButton;
    SessionManager sessionManager;
    Integer numeroAleatorio = generarNumeroAleatorio();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar);
        sessionManager = new SessionManager(this);

        initIgui();
    }

    private void initIgui() {

        nameText = findViewById(R.id.idName);
        surnameText = findViewById(R.id.idSurname);
        dateText = findViewById(R.id.editTextDate);
        domicilioText = findViewById(R.id.idDomicilio);
        postalAddressText = findViewById(R.id.editTextTextPostalAddress);
        phoneText = findViewById(R.id.editTextPhone);
        changePassword = findViewById(R.id.changePasswordId);

        editButton = findViewById(R.id.editButton);


        changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        });


        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = "http://10.0.2.2:3000/pacientes/obtenerPaciente/" + sessionManager.getUserId();

        getDataAsync(urlServidor, executor, (GetDataAsync.OnTaskCompleted) result -> {
            if (result != null) {
                try {

                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {


                        JSONObject firstObject = jsonArray.getJSONObject(0);

                        nameText.setText(firstObject.getString("Nombre"));
                        surnameText.setText(firstObject.getString("Apellidos"));
                        dateText.setText(firstObject.getString("FechaDeNacimiento"));
                        domicilioText.setText(firstObject.getString("Direccion"));
                        postalAddressText.setText(firstObject.getString("CodigoPostal"));
                        phoneText.setText(firstObject.getString("telefono"));



                    } else {
                        Log.d(TAG, "Error: No data found in the JSON array");

                    }

                    Log.i("DOCTORES",jsonArray.toString());
                } catch (JSONException e) {
                    Log.e("ObtenerDoctoresTask", "Error al procesar JSON", e);
                }
            }
        });



        editButton.setOnClickListener(v -> {

            Executor executor2 = Executors.newSingleThreadExecutor();
            String urlServidor2 = "http://10.0.2.2:3000/pacientes/editarPaciente/" + sessionManager.getUserId();

            JSONObject postData = new JSONObject();
            try {
                postData.put("Nombre", nameText.getText().toString());
                postData.put("Apellidos",surnameText.getText().toString());
                postData.put("FechaDeNacimiento", dateText.getText().toString());
                postData.put("Direccion", domicilioText.getText().toString());
                postData.put("CodigoPostal", postalAddressText.getText().toString());
                postData.put("telefono", phoneText.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            postDataAsync(urlServidor2, executor2, (PostDataAsync.OnTaskCompleted) result -> {
                runOnUiThread(() -> {
                    if (result != null) {

                        Log.d(TAG, "Registro finalizado correctamente");
                        makeTextToast("Registro finalizado correctamente");
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Log.d(TAG, "Error al registrar");
                        makeTextToast("Error al registrar");
                    }
                });
            }, "POST", postData.toString());

        });
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


