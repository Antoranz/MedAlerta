package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.utils.adapters.ConsultasListAdapter;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.manager.SessionManager;

import java.util.LinkedList;

public class ConsultasActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private ConsultasListAdapter adapter;
    private Button crearConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        sessionManager = new SessionManager(this);

        initIgui();

    }

    private void initIgui() {



        RecyclerView rv = findViewById(R.id.recyclerViewConsultas);

        crearConsulta = findViewById(R.id.ButtonCrearConsulta);

        crearConsulta.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearConsultaActivity.class);
            startActivity(intent);
        });


        adapter = new ConsultasListAdapter(new LinkedList<>(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        adapter.setConsultasList(Controller.getInstance().getAllConsultas(this,sessionManager.getUserId()));
        adapter.notifyDataSetChanged();


    }


}
