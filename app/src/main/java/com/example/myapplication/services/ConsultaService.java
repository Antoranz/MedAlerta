package com.example.myapplication.services;

import com.example.myapplication.data.Consulta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface ConsultaService {

    public LinkedList<Consulta> getAllConsultas(String dni);

    public LinkedList<String> getDoctoresParaConsulta(String idPaciente);

    void postCrearConsulta(String nombre, String titulo, String fecha);
}
