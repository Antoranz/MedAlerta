package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.myapplication.PostDataAsync.postDataAsync;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.Consulta;
import com.example.myapplication.utils.ConsultasListAdapter;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
