package com.example.myapplication;
/*
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    private EditText nameText,surnameText,dateText,domicilioText,postalAddressText,phoneText;
    private TextView changePassword;
    private Button editButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_editar);

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
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            makeTextToast("Email sent");
                            auth.signOut();
                            Intent intent = new Intent(this, IniciarSesionActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d(TAG, "Error on Email sent.");
                            makeTextToast("Error on Email sent");
                        }
                    });
        });

        DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    nameText.setText(document.getString("Nombre"));
                    surnameText.setText(document.getString("Apellidos"));
                    dateText.setText(document.getString("FechaDeNacimiento"));
                    domicilioText.setText(document.getString("Direccion"));
                    postalAddressText.setText(document.getString("CodigoPostal"));
                    phoneText.setText(document.getString("telefono"));

                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        editButton.setOnClickListener(v -> {

            Map<String, Object> datosPersonales = new HashMap<>();
            datosPersonales.put("Nombre", nameText.getText().toString());
            datosPersonales.put("Apellidos",surnameText.getText().toString());
            datosPersonales.put("FechaDeNacimiento", dateText.getText().toString());
            datosPersonales.put("Direccion", domicilioText.getText().toString());
            datosPersonales.put("CodigoPostal", postalAddressText.getText().toString());
            datosPersonales.put("telefono", phoneText.getText().toString());

            db.collection("users").document(firebaseAuth.getCurrentUser().getEmail())
                    .set(datosPersonales)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Datos personales modificados con éxito");
                            changeActivity();
                            makeTextToast("Datos personales modificados con éxito");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al guardar datos de usuario en la base de datos", e);
                        }
                    });







        });
    }

    private void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}

*/
