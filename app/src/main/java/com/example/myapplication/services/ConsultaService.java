package com.example.myapplication.services;

import android.content.Context;

import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Doctor;

import java.util.LinkedList;

public interface ConsultaService {

    public LinkedList<Consulta> getAllConsultas(String dni);

    public LinkedList<Doctor> getDoctoresParaConsulta(String idPaciente);

    void postCrearConsulta(Context context, String dni_doctor, String dni_paciente, String titulo, String fecha);

    boolean getSaberSiHayMensajesNoLeidos(long id,String dni);
}
