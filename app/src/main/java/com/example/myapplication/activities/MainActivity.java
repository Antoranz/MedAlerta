package com.example.myapplication.activities;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.utils.async.PostDataAsync.postDataAsync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.fragments.CalendarCitasFragment;
import com.example.myapplication.fragments.ConsultasFragment;
import com.example.myapplication.fragments.CrearCitaFragment;
import com.example.myapplication.fragments.EditarFragment;
import com.example.myapplication.fragments.LogoFragment;
import com.example.myapplication.services.ConfigApi;
import com.example.myapplication.utils.async.PostDataAsync;
import com.example.myapplication.R;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    TextView userText;
    SessionManager sessionManager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initIgui();

    }

    private void initIgui() {

        sessionManager = new SessionManager(this);

        String username = sessionManager.getName();
        userText = findViewById(R.id.name);
        userText.setText(username);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        replaceFragment(new LogoFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.consultas) {
                replaceFragment(new ConsultasFragment());
                return true;
            } else if (item.getItemId() == R.id.calendario) {
                replaceFragment(new CalendarCitasFragment());
                return true;
            } else if (item.getItemId() == R.id.anadir_cita) {
                replaceFragment(new CrearCitaFragment());
                return true;
            } else {
                return false;
            }
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
            replaceFragment(new EditarFragment());
            return true;
        } else if (id == R.id.action_delete) {
            showDeleteConfirmationDialog();
            return true;
        } else if (id == R.id.action_logout) {
            sessionManager.logout();
            NavigationManager.getInstance().navigateToDestination(this,IniciarSesionActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void borrarDatos(){

        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = ConfigApi.BASE_URL+"pacientes/usuario/bajaPaciente";

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
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }

}
