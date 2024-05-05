package com.example.myapplication.activities;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.myapplication.fragments.AlarmFragment;
import com.example.myapplication.fragments.CalendarCitasFragment;
import com.example.myapplication.fragments.ConsultasFragment;
import com.example.myapplication.fragments.CrearCitaFragment;
import com.example.myapplication.fragments.EditarFragment;
import com.example.myapplication.fragments.LogoFragment;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.R;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;



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
            } else if (item.getItemId() == R.id.ver_alarmas) {
                replaceFragment(new AlarmFragment());
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

        Controller.getInstance().bajaPaciente(this);

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

}
