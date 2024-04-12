package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Doctor;
import com.example.myapplication.utils.async.ActualizarMensajesAsync;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.adapters.MensajesListAdapter;
import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


public class ChatViewActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private TextView nombreDoctorchat;

    private TextInputEditText textoAenviar;
    private AppCompatImageButton buttonEnviar;

    LinkedList<Doctor> listaDoctores;
    private String nombreDoctor = "";

    Consulta consulta;
    private MensajesListAdapter adapter;
    private ActualizarMensajesAsync actualizador;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        if (android.os.Build.VERSION.SDK_INT >= 31) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                consulta = (Consulta) extras.getSerializable("data");
            }
        }

        sessionManager = new SessionManager(this);
        initUI();

    }

    @SuppressLint("WrongViewCast")
    private void initUI() {
        nombreDoctorchat = findViewById(R.id.nombreDoctorTextView);
        textoAenviar = findViewById(R.id.textoAEnviar);
        buttonEnviar = findViewById(R.id.botonEnviar);

        buttonEnviar.setOnClickListener(v -> {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            Controller.getInstance().crearMensaje(consulta.getId(),textoAenviar.getText().toString(),1,timeStamp);
            textoAenviar.setText("");
        });

        //Poner nombre del doctor en cada chat
        String dniDoctor = consulta.getDni_doctor();

        listaDoctores = Controller.getInstance().getDoctoresParaConsulta(this,sessionManager.getUserId());
        for (Doctor doctor : listaDoctores) {
            if (doctor.getDni().equals(dniDoctor)) {
                nombreDoctor = doctor.getNombre();
                break;
            }
        }

        nombreDoctorchat.setText(nombreDoctor);

        RecyclerView rv = findViewById(R.id.recyclerViewChat);

        adapter = new MensajesListAdapter(new LinkedList<>(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        adapter.setMensajesList(Controller.getInstance().getAllMensajes(this,consulta.getId()));
        adapter.notifyDataSetChanged();

        actualizador = new ActualizarMensajesAsync(adapter,consulta.getId(),this);
        actualizador.startTask();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (actualizador != null) {
            actualizador.stopTask();
        }
    }
}
