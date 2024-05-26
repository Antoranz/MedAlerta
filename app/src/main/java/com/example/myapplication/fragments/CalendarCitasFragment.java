package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class CalendarCitasFragment extends Fragment {

    private List<Cita> citas;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendario_citas, container, false);

        sessionManager = new SessionManager(requireContext());
        citas = Controller.getInstance().getAllCitas(requireContext(), sessionManager.getUserId());

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        CitasListAdapter adapter = new CitasListAdapter(new LinkedList<>(citas), requireContext());
        recyclerView.setAdapter(adapter);

        CalendarView calendarView = rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> mostrarInformacionDeCita(year, month, dayOfMonth));

        return rootView;
    }

    private void mostrarInformacionDeCita(int year, int month, int dayOfMonth) {
        boolean citaEncontrada = false;
        for (Cita cita : citas) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cita.getFecha_hora());

            if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                citaEncontrada = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Detalles de la Cita");
                builder.setMessage("Duración de la cita: " + cita.getDuracion() + " minutos");
                builder.setPositiveButton("Aceptar", null);
                builder.show();
                break;
            }
        }

        if (!citaEncontrada) {
            Toast.makeText(requireContext(), "No hay cita para esta fecha", Toast.LENGTH_SHORT).show();
        }
    }

    private Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }
}
