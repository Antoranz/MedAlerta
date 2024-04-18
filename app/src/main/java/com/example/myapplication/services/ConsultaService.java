package com.example.myapplication.services;

import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Doctor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface ConsultaService {

    public LinkedList<Consulta> getAllConsultas(String dni);

    public LinkedList<Doctor> getDoctoresParaConsulta(String idPaciente);

    void postCrearConsulta(String dni_doctor,String dni_paciente, String titulo, String fecha);
}
