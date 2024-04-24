package com.example.myapplication.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Paciente {
    private String dni;
    private String email;
    private String Nombre;
    private String Apellidos;
    private String CodigoPostal;
    private Date FechaDeNacimiento;
    private String Direccion;
    private String telefono;
    private String password;


    public Paciente(String dni, String email, String nombre, String apellidos, String codigoPostal, Date fechaDeNacimiento, String direccion, String telefono,String password) {
        this.dni = dni;
        this.email = email;
        Nombre = nombre;
        Apellidos = apellidos;
        CodigoPostal = codigoPostal;
        FechaDeNacimiento = fechaDeNacimiento;
        Direccion = direccion;
        this.telefono = telefono;
        this.password = password;
    }
    public Paciente(String nombre, String apellidos, String codigoPostal, Date fechaDeNacimiento, String direccion, String telefono) {
        Nombre = nombre;
        Apellidos = apellidos;
        CodigoPostal = codigoPostal;
        FechaDeNacimiento = fechaDeNacimiento;
        Direccion = direccion;
        this.telefono = telefono;
    }

    public String writeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy//MM/dd");
        return sdf.format(FechaDeNacimiento.getTime());
    }
    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        CodigoPostal = codigoPostal;
    }

    public Date getFechaDeNacimiento() {
        return FechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        FechaDeNacimiento = fechaDeNacimiento;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setDni(String dni){
        this.dni = dni;
    }
}
