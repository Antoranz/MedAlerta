package com.example.myapplication.activities;

import android.annotation.SuppressLint;

import android.os.Bundle;

import android.widget.EditText;
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

import com.example.myapplication.utils.manager.SessionManager;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


public class ChatViewActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private TextView nombreDoctorchat;

    private EditText textoAenviar;
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

    @SuppressLint({"WrongViewCast", "NotifyDataSetChanged"})
    private void initUI() {
        nombreDoctorchat = findViewById(R.id.nombreDoctorTextView);
        textoAenviar = findViewById(R.id.textoAEnviar);
        buttonEnviar = findViewById(R.id.botonEnviar);
        RecyclerView rv = findViewById(R.id.recyclerViewChat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());


        buttonEnviar.setOnClickListener(v -> {
            if(!textoAenviar.getText().toString().isEmpty()){
                Controller.getInstance().crearMensaje(consulta.getId(),textoAenviar.getText().toString(),1,timeStamp);
                textoAenviar.setText("");
                adapter.setMensajesList(Controller.getInstance().getAllMensajes(this, consulta.getId()));
                adapter.notifyDataSetChanged();
                rv.postDelayed(() -> rv.smoothScrollToPosition(adapter.getItemCount() - 1), 500);
            }
        });
        rv.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                rv.postDelayed(() -> rv.smoothScrollToPosition(adapter.getItemCount() - 1), 500);
            }
        });
        Controller.getInstance().ponerMensajesComoLeidos(sessionManager.getUserId(),consulta.getId());
        adapter = new MensajesListAdapter(new LinkedList<>(),this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        rv.post(() -> {
            rv.scrollToPosition(adapter.getItemCount() - 1);
        });

        String dniDoctor = consulta.getDni_doctor();

        listaDoctores = Controller.getInstance().getDoctoresParaConsulta(this,sessionManager.getUserId());
        for (Doctor doctor : listaDoctores) {
            if (doctor.getDni().equals(dniDoctor)) {
                nombreDoctor = doctor.getNombre();
                break;
            }
        }
        nombreDoctorchat.setText(nombreDoctor);
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
