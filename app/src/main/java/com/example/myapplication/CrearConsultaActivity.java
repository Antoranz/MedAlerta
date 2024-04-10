package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.DoctorSpinnerHelper;
import com.example.myapplication.utils.SessionManager;

import java.util.List;

public class CrearConsultaActivity extends AppCompatActivity {

    SessionManager sessionManager;
    private Button crearConsulta;

    private Spinner spinnerDoctores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_consulta);

        sessionManager = new SessionManager(this);

        initIgui();

    }

    private void initIgui() {

        spinnerDoctores = findViewById(R.id.spinnerDoctores);

        List<String> listaDoctores = Controller.getInstance().getDoctoresParaConsulta(this,sessionManager.getUserId());

        for (String doctor : listaDoctores) {
            Log.d("Nombres de doctores", doctor);
        }

        DoctorSpinnerHelper.setupDoctorSpinner(this, spinnerDoctores, listaDoctores);

        DoctorSpinnerHelper.setupDoctorSpinnerWithListener(this, spinnerDoctores, listaDoctores, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String doctorSeleccionado = listaDoctores.get(position);
                Toast.makeText(CrearConsultaActivity.this, "Doctor seleccionado: " + doctorSeleccionado, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejar el caso en que no se seleccione ningún doctor si es necesario
            }
        });



    }


}
