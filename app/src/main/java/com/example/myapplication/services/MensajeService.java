package com.example.myapplication.services;

import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Mensaje;

import java.util.LinkedList;

public interface MensajeService {

    public LinkedList<Mensaje> getAllMensajes(long id_consulta);
}
