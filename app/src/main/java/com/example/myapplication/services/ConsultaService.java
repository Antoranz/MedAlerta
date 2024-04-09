package com.example.myapplication.services;

import com.example.myapplication.data.Consulta;

import java.util.LinkedList;
import java.util.List;

public interface ConsultaService {

    LinkedList<Consulta> getAllConsultas(String dni);

    List<String> getDoctoresParaConsulta(String idPaciente);

}
