package com.example.myapplication.services;

import android.app.Activity;
import android.content.Context;

import com.example.myapplication.data.Paciente;

public interface PacienteService {
    public Paciente getPaciente(String dni);
    public void updatePaciente(Paciente paciente, Context context);
    public void checkPaciente(String dni, String pass,Context context);
    public <T> void editarPassword(String dni, String pass, Context context, Class<T> activity);
    public void validarPaciente(String numeroAleatorio, String email, Context context);

    public void bajaPaciente(Context context);

    public void registrarPaciente(Paciente paciente, Context context);
}
