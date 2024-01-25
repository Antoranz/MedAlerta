package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.PostDataAsync.postDataAsync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegistrarActivity extends AppCompatActivity  {

    private EditText emailText,passwordText,repitPasswordText,nameText,surnameText,dateText,domicilioText,postalAddressText,phoneText,dniText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrar);

        initIgui();
    }

    private void initIgui() {
        emailText = findViewById(R.id.idEmail);
        passwordText = findViewById(R.id.idPassword);
        repitPasswordText = findViewById(R.id.idRepitPassword);
        nameText = findViewById(R.id.idName);
        surnameText = findViewById(R.id.idSurname);
        dateText = findViewById(R.id.editTextDate);
        domicilioText = findViewById(R.id.idDomicilio);
        postalAddressText = findViewById(R.id.editTextTextPostalAddress);
        phoneText = findViewById(R.id.editTextPhone);
        dniText = findViewById(R.id.idDNI);

        registerButton = findViewById(R.id.idRegisterButton);

        registerButton.setOnClickListener(v -> {
           if(!passwordText.getText().toString().equals(repitPasswordText.getText().toString())){
               makeTextToast("Las contraseñas no coinciden");
           }else if(passwordText.getText().toString().isEmpty() || emailText.getText().toString().isEmpty()){
               makeTextToast("Ningún campo debe estar vacío");
           }else if(!emailText.getText().toString().contains("@")){
               makeTextToast("Email no válido");

           } else if(!esDniValido(dniText.getText().toString())){
               makeTextToast("DNI inválido");
           }
           else{

               Executor executor = Executors.newSingleThreadExecutor();
               String urlServidor = "http://10.0.2.2:3000/pacientes/registrarPaciente";

               JSONObject postData = new JSONObject();
               try {
                   postData.put("Nombre", nameText.getText().toString());
                   postData.put("Apellidos",surnameText.getText().toString());
                   postData.put("FechaDeNacimiento", dateText.getText().toString());
                   postData.put("Direccion", domicilioText.getText().toString());
                   postData.put("CodigoPostal", postalAddressText.getText().toString());
                   postData.put("telefono", phoneText.getText().toString());
                   postData.put("email", emailText.getText().toString());
                   postData.put("DNI", dniText.getText().toString());
                   postData.put("password", passwordText.getText().toString());

               } catch (JSONException e) {
                   e.printStackTrace();
               }

               postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
                   if (result != null) {

                       Log.d(TAG, "Registro finalizado correctamente");
                       makeTextToast("Registro finalizado correctamente");
                       Intent intent = new Intent(this, IniciarSesionActivity.class);
                       startActivity(intent);

                   }else{
                       Log.d(TAG, "Error al registrar");
                       makeTextToast("Error al registrar");
                   }
               }, "POST", postData.toString());

           }
        });
    }

    public static boolean esDniValido(String dni) {

        if (dni.length() != 9) {
            return false;
        }

        String numero = dni.substring(0, 8);
        char letra = dni.charAt(8);

        try {

            int numeroInt = Integer.parseInt(numero);

            char letraEsperada = calcularLetraDni(numeroInt);

            return letra == letraEsperada;
        } catch (NumberFormatException e) {

            return false;
        }
    }

    private static char calcularLetraDni(int numero) {

        char[] letras = "TRWAGMYFPDXBNJZSQVHLCKE".toCharArray();
        int indiceLetra = numero % 23;
        return letras[indiceLetra];
    }
    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}
