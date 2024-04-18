package com.example.myapplication.data;

public class Doctor {
    private String nombre;
    private String dni;

    public Doctor(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    // Override toString() para que el ArrayAdapter pueda mostrar el nombre en el Spinner
    @Override
    public String toString() {
        return nombre;
    }
}
