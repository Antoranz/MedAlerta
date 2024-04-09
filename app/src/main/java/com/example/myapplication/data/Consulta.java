package com.example.myapplication.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Consulta implements Serializable {
    private long id;
    private String dni_doctor;
    private String dni_paciente;

    private String titulo;
    private Date ultima_fecha;
    private int mensajes_totales;
    private int notificaciones_doctor;
    private int notificaciones_paciente;


    public Consulta(long id, String dni_doctor, String dni_paciente,String titulo, Date ultima_fecha, int mensajes_totales, int notificaciones_doctor, int notificaciones_paciente) {
        this.id = id;
        this.dni_doctor = dni_doctor;
        this.dni_paciente = dni_paciente;
        this.titulo = titulo;
        this.ultima_fecha = ultima_fecha;
        this.mensajes_totales = mensajes_totales;
        this.notificaciones_doctor = notificaciones_doctor;
        this.notificaciones_paciente = notificaciones_paciente;
    }


    public String writeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(ultima_fecha.getTime());
    }

    public long getId() {
        return id;
    }

    public String getDni_doctor() {
        return dni_doctor;
    }

    public void setDni_doctor(String dni_doctor) {
        this.dni_doctor = dni_doctor;
    }

    public String getDni_paciente() {
        return dni_paciente;
    }

    public void setDni_paciente(String dni_paciente) {
        this.dni_paciente = dni_paciente;
    }

    public Date getUltima_fecha() {
        return ultima_fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUltima_fecha(Date ultima_fecha) {
        this.ultima_fecha = ultima_fecha;
    }

    public int getMensajes_totales() {
        return mensajes_totales;
    }

    public void setMensajes_totales(int mensajes_totales) {
        this.mensajes_totales = mensajes_totales;
    }

    public int getNotificaciones_doctor() {
        return notificaciones_doctor;
    }

    public void setNotificaciones_doctor(int notificaciones_doctor) {
        this.notificaciones_doctor = notificaciones_doctor;
    }

    public int getNotificaciones_paciente() {
        return notificaciones_paciente;
    }

    public void setNotificaciones_paciente(int notificaciones_paciente) {
        this.notificaciones_paciente = notificaciones_paciente;
    }
}

