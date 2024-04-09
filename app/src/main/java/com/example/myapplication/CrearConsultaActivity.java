package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.myapplication.PostDataAsync.postDataAsync;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.Consulta;
import com.example.myapplication.utils.ConsultasListAdapter;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.DoctorSpinnerHelper;
import com.example.myapplication.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
