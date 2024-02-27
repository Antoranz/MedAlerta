package com.example.myapplication;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.GetDataAsync.getDataAsync;
import static com.example.myapplication.PostDataAsync.postDataAsync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.SessionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    TextView userText;
    SessionManager sessionManager;

    Button solicitarCita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initIgui();

    }

    private void initIgui() {

        sessionManager = new SessionManager(this);

        String username = sessionManager.getEmail();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        userText = findViewById(R.id.name);
        userText.setText(username);

        solicitarCita = findViewById(R.id.botonCita);

        solicitarCita.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearCitaActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_edit) {

            Intent intent = new Intent(this, EditarActivity.class);
            startActivity(intent);

            finish();
            return true;
        } else if (id == R.id.action_delete) {
            showDeleteConfirmationDialog();
            return true;
        } else if (id == R.id.action_logout) {
            // Acción para cerrar sesión

            sessionManager.logout();

            Intent intent = new Intent(this, IniciarSesionActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void borrarDatos(){

        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = "http://10.0.2.2:3000/pacientes/bajaPaciente";

        JSONObject postData = new JSONObject();
        try {
            postData.put("dniPaciente", sessionManager.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Realizar la solicitud POST para borrar la cuenta de un paciente
        postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
            runOnUiThread(() -> {
                if (result != null) {

                    Log.d(TAG, "Account deleted successfully");
                    makeTextToast("Account deleted successfully");
                    sessionManager.logout();
                    Intent intent = new Intent(this, IniciarSesionActivity.class);
                    startActivity(intent);

                }else{
                    Log.d(TAG, "Error deleting account");
                    makeTextToast("Error deleting account");
                }

            });

        }, "POST", postData.toString());

    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar cuenta")
                .setMessage("¿Estás seguro de que deseas eliminar tu cuenta?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        borrarDatos();

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }

}
