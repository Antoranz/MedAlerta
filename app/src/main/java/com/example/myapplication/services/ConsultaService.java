package com.example.myapplication.services;

import com.example.myapplication.data.Consulta;

import java.util.LinkedList;

public interface ConsultaService {

    public LinkedList<Consulta> getAllConsultas(String dni);
}
