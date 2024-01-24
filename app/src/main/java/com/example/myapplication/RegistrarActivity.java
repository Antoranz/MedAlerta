package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;
import java.util.Map;
/*
public class RegistrarActivity extends AppCompatActivity  {

    private EditText emailText,passwordText,repitPasswordText,nameText,surnameText,dateText,domicilioText,postalAddressText,phoneText;
    private Button registerButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
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

        registerButton = findViewById(R.id.idRegisterButton);

        registerButton.setOnClickListener(v -> {
           if(!passwordText.getText().toString().equals(repitPasswordText.getText().toString())){
               makeTextToast("Las contraseñas no coinciden");
           }else if(passwordText.getText().toString().isEmpty() || emailText.getText().toString().isEmpty()){
               makeTextToast("Ningún campo debe estar vacío");
           }else if(!emailText.getText().toString().contains("@")){
               makeTextToast("Email no válido");
           }else{


               firebaseAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                       .addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {


                               Map<String, Object> datosPersonales = new HashMap<>();
                               datosPersonales.put("Nombre", nameText.getText().toString());
                               datosPersonales.put("Apellidos",surnameText.getText().toString());
                               datosPersonales.put("FechaDeNacimiento", dateText.getText().toString());
                               datosPersonales.put("Direccion", domicilioText.getText().toString());
                               datosPersonales.put("CodigoPostal", postalAddressText.getText().toString());
                               datosPersonales.put("telefono", phoneText.getText().toString());

                               db.collection("users").document(emailText.getText().toString())
                                       .set(datosPersonales)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               Log.d(TAG, "Usuario guardado en la base de datos correctamente");
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Log.w(TAG, "Error al guardar datos de usuario en la base de datos", e);
                                           }
                                       });
                               makeTextToast("Registro completado correctamente");
                               FirebaseUser user = firebaseAuth.getCurrentUser();
                               user.sendEmailVerification()
                                       .addOnCompleteListener(task1 -> {
                                           if (task1.isSuccessful()) {
                                               Log.d(TAG, "Email sent.");
                                               makeTextToast("Email sent.");
                                           }
                                       });
                               Intent intent = new Intent(this, ConfirmationActivity.class);
                               startActivity(intent);
                           }
                           else {
                               makeTextToast("Ha habido un error en el registro");
                               Exception exception = task.getException();
                               if (exception != null) {
                                   Log.d("TAG", exception.getMessage());
                               }
                           }
                       });



           };
        });
    }
    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}
*/