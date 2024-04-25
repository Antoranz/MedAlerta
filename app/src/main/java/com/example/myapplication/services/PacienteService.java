package com.example.myapplication.services;

import android.content.Context;

import com.example.myapplication.data.Paciente;

public interface PacienteService {
    public Paciente getPaciente(String dni);
    public void updatePaciente(Paciente paciente, Context context);
    public void checkPaciente(String dni, String pass,Context context);
    public void editarPassword(String dni, String pass, Context context);
    public void validarPaciente(String numeroAleatorio, String email, Context context);

    public void bajaPaciente(Context context);

    public void registrarPaciente(Paciente paciente, Context context);
}
