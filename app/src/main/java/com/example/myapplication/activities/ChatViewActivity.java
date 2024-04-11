package com.example.myapplication.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Consulta;
import com.example.myapplication.utils.async.ActualizarMensajesAsync;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.adapters.MensajesListAdapter;
import com.example.myapplication.utils.manager.SessionManager;

import java.util.LinkedList;


public class ChatViewActivity extends AppCompatActivity {

    SessionManager sessionManager;

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

    private void initUI() {

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
