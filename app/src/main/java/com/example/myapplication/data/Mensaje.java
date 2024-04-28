package com.example.myapplication.data;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable {

    private long id_consulta;
    private String mensaje;
    private int propietario;
    private Date fecha;

    public Mensaje(long id_consulta, String mensaje, int propietario, Date fecha) {
        this.id_consulta = id_consulta;
        this.mensaje = mensaje;
        this.propietario = propietario;
        this.fecha = fecha;
    }

    public long getId_consulta() {
        return id_consulta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getPropietario() {
        return propietario;
    }

    public void setPropietario(int propietario) {
        this.propietario = propietario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
