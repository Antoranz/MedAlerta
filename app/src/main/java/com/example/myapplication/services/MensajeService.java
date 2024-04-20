package com.example.myapplication.services;

import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Mensaje;

import java.util.LinkedList;

public interface MensajeService {

    public LinkedList<Mensaje> getAllMensajes(long id_consulta);
    public void crearMensaje(long id, String mensaje,long propietario,String fecha);
    public LinkedList<String> obtenerMensajesNoLeidos(String dni);
    public void ponerMensajesComoLeidos(String dni,long id);
}
