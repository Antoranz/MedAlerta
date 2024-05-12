package com.example.myapplication.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cita implements Serializable {

    private long id;
    private String doctor_dni;
    private String paciente_dni;
    private String nombre_doctor;
    private String apellidos_doctor;
    private Date fecha_hora;
    private int duracion;



    public Cita(long id, String doctor_dni, String paciente_dni, Date fecha_hora, int duracion, String nombre_doctor, String apellidos_doctor) {
        this.id = id;
        this.doctor_dni = doctor_dni;
        this.paciente_dni = paciente_dni;
        this.nombre_doctor = nombre_doctor;
        this.apellidos_doctor = apellidos_doctor;
        this.fecha_hora = fecha_hora;
        this.duracion = duracion;
    }

    public String getNombre_doctor() {
        return nombre_doctor;
    }

    public String getApellidos_doctor() {
        return apellidos_doctor;
    }


    public String writeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha_hora.getTime());
    }
    public long getId() {
        return id;
    }

    public String getDoctor_dni() {
        return doctor_dni;
    }

    public void setDoctor_dni(String doctor_dni) {
        this.doctor_dni = doctor_dni;
    }

    public String getPaciente_dni() {
        return paciente_dni;
    }

    public void setPaciente_dni(String paciente_dni) {
        this.paciente_dni = paciente_dni;
    }

    public Date getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Date fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
