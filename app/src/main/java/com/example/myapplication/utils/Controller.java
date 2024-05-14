package com.example.myapplication.utils;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.activities.IniciarSesionActivity;
import com.example.myapplication.activities.RegistrarActivity;
import com.example.myapplication.fragments.CrearCitaFragment;
import com.example.myapplication.utils.receiver.AlarmReceiver;
import com.example.myapplication.data.Cita;
import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Doctor;
import com.example.myapplication.data.Mensaje;
import com.example.myapplication.data.Paciente;
import com.example.myapplication.services.CitaService;
import com.example.myapplication.services.ConsultaService;
import com.example.myapplication.services.MensajeService;
import com.example.myapplication.services.PacienteService;
import com.example.myapplication.services.implementaciones.CitaServiceImp;
import com.example.myapplication.services.implementaciones.ConsultaServiceImp;
import com.example.myapplication.services.implementaciones.MensajeServiceImp;
import com.example.myapplication.services.implementaciones.PacienteServiceImp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class Controller {
    static Controller instance;

    public static Controller getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new Controller();
        return instance;
    }

    public LinkedList<Consulta> getAllConsultas(Context c,String dni) {
        ConsultaService service = ConsultaServiceImp.getInstance();

        return service.getAllConsultas(dni);
    }
    public LinkedList<Cita> getAllCitas(Context c, String dni) {
        CitaService service = CitaServiceImp.getInstance();

        return service.getAllCitas(c,dni);
    }

    public LinkedList<Mensaje> getAllMensajes(Context c, long id_consulta) {
        MensajeService service = MensajeServiceImp.getInstance();

        return service.getAllMensajes(id_consulta);
    }

    public LinkedList<Doctor> getDoctoresParaConsulta(Context context, String id_paciente) {

        ConsultaService service = ConsultaServiceImp.getInstance();

        return service.getDoctoresParaConsulta(id_paciente);
    }

    public void crearMensaje(long id, String mensaje, long propietario, String fecha) {

        MensajeService service = MensajeServiceImp.getInstance();

        service.crearMensaje(id,mensaje,propietario,fecha);
    }

    public void postCrearConsulta(Context context,String dni_doctor,String dni_paciente, String titulo, String fecha) {

        ConsultaService service = ConsultaServiceImp.getInstance();

        service.postCrearConsulta(context,dni_doctor,dni_paciente, titulo, fecha);
    }

    public LinkedList<String> obtenerMensajesNoLeidos(String dni) {

        MensajeService service = MensajeServiceImp.getInstance();

        return service.obtenerMensajesNoLeidos(dni);
    }

    public String obtenerDniRepetido(String dni) {

        PacienteService service = PacienteServiceImp.getInstance();

        return service.obtenerDniRepetido(dni);
    }

    public String obtenerEmailRepetido(String email) {

        PacienteService service = PacienteServiceImp.getInstance();

        return service.obtenerEmailRepetido(email);
    }

    public void ponerMensajesComoLeidos(String userId, long id) {

        MensajeService service = MensajeServiceImp.getInstance();

        service.ponerMensajesComoLeidos(userId,id);

    }

    public boolean getSaberSiHayMensajesNoLeidos(long id, String dni) {

        ConsultaService service = ConsultaServiceImp.getInstance();

        return service.getSaberSiHayMensajesNoLeidos(id, dni);
    }




    public void creacionAlarma(int i, Long timestamp, String medicamento, String dosis, Context ctx) {

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);

        alarmIntent.putExtra("MEDICAMENTO",medicamento);
        alarmIntent.putExtra("DOSIS", dosis);

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);

        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDateTime = sdf.format(date);

        Log.i("ALARMAS", "Alarma creada: ID " + i + ", timestamp " + formattedDateTime);

    }
    public Paciente getPaciente(String dni){
        PacienteService service = PacienteServiceImp.getInstance();
        return service.getPaciente(dni);
    }
    public void updatePaciente(Paciente paciente, Context context){
        PacienteService service = PacienteServiceImp.getInstance();
        service.updatePaciente(paciente,context);
    }


    public void checkPaciente(String dni, String pass, Context context) {
        PacienteService service = PacienteServiceImp.getInstance();
        service.checkPaciente(dni,pass,context);
    }

    public <T> void editarPassword(String dni, String pass, Context context, Class<T> activity){
        PacienteService service = PacienteServiceImp.getInstance();
        service.editarPassword(dni,pass,context,activity);
    }

    public void validarPaciente(String numeroAleatorio, String email, Context context){
        PacienteService service = PacienteServiceImp.getInstance();
        service.validarPaciente(numeroAleatorio,email,context);
    }

    public void bajaPaciente(Context context){
        PacienteService service = PacienteServiceImp.getInstance();
        service.bajaPaciente(context);
    }

    public void registrarPaciente(Paciente paciente, Context context) {
        PacienteService service = PacienteServiceImp.getInstance();
        service.registrarPaciente(paciente,context);
    }

    public void crearNotificacionCita(Context context, String dniDoctorSeleccionado, String tipo, String motivo, String date, String time) {
        CitaService service = CitaServiceImp.getInstance();
        service.crearNotificacionCita(context,dniDoctorSeleccionado,tipo,motivo,date,time);
    }
}