package com.example.myapplication.data;

import java.io.Serializable;

public class Doctor implements Serializable {
    private String nombre;

    private String apellidos;
    private String dni;

    public Doctor(String nombre,String apellidos, String dni) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

    // Override toString() para que el ArrayAdapter pueda mostrar el nombre en el Spinner
    @Override
    public String toString() {
        return nombre +" "+apellidos;
    }
}
