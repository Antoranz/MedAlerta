package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Cita;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.adapters.CitasListAdapter;
import com.example.myapplication.utils.manager.SessionManager;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CalendarCitasActivity extends AppCompatActivity {
    private List<Cita> citas;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_citas);
        sessionManager = new SessionManager(this);
        citas = Controller.getInstance().getAllCitas(this, sessionManager.getUserId());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // o GridLayoutManager según tus necesidades

        // Crea un adaptador para el RecyclerView y asigna la lista de citas
        CitasListAdapter adapter = new CitasListAdapter( new LinkedList<>(citas),this); // Suponiendo que "citas" es tu lista de citas
        recyclerView.setAdapter(adapter);

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Aquí puedes manejar la lógica cuando el usuario selecciona una fecha
                // Por ejemplo, puedes mostrar una ventana emergente con la información de la cita para esa fecha
                mostrarInformacionDeCita(year, month, dayOfMonth);
            }
        });
    }
    private void mostrarInformacionDeCita(int year, int month, int dayOfMonth) {
        boolean citaEncontrada = false;
        for (Cita cita : citas) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cita.getFecha_hora());

            if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                citaEncontrada = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Detalles de la Cita");
                builder.setMessage("Doctor DNI: " + cita.getDoctor_dni() + "\n"
                        + "Paciente DNI: " + cita.getPaciente_dni() + "\n"
                        + "Duración: " + cita.getDuracion() + " minutos");
                builder.setPositiveButton("Aceptar", null);
                builder.show();
                break;
            }
        }

        if (!citaEncontrada) {
            Toast.makeText(this, "No hay cita para esta fecha", Toast.LENGTH_SHORT).show();
        }
    }




    private Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }
}