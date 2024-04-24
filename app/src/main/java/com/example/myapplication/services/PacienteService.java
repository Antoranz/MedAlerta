package com.example.myapplication.services;

import android.content.Context;

import com.example.myapplication.data.Paciente;

public interface PacienteService {
    public Paciente getPaciente(String dni);
    public void updatePaciente(Paciente paciente, Context context);
}
