package com.example.myapplication.utils;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.activities.AlarmReceiverActivity;
import com.example.myapplication.activities.ChatViewActivity;
import com.example.myapplication.data.Cita;
import com.example.myapplication.data.Consulta;
import com.example.myapplication.data.Doctor;
import com.example.myapplication.data.Mensaje;
import com.example.myapplication.services.CitaService;
import com.example.myapplication.services.ConsultaService;
import com.example.myapplication.services.MensajeService;
import com.example.myapplication.services.implementaciones.CitaServiceImp;
import com.example.myapplication.services.implementaciones.ConsultaServiceImp;
import com.example.myapplication.services.implementaciones.MensajeServiceImp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

    public void postCrearConsulta(String dni_doctor,String dni_paciente, String titulo, String fecha) {

        ConsultaService service = ConsultaServiceImp.getInstance();

        service.postCrearConsulta(dni_doctor,dni_paciente, titulo, fecha);
    }

    public LinkedList<String> obtenerMensajesNoLeidos(String dni) {

        MensajeService service = MensajeServiceImp.getInstance();

        return service.obtenerMensajesNoLeidos(dni);
    }

    public void ponerMensajesComoLeidos(String userId, long id) {

        MensajeService service = MensajeServiceImp.getInstance();

        service.ponerMensajesComoLeidos(userId,id);

    }




    public void creacionAlarma(int i, Long timestamp, String medicamento, String dosis, Context ctx) {

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiverActivity.class);

        alarmIntent.putExtra("MEDICAMENTO",medicamento);
        alarmIntent.putExtra("DOSIS", dosis);

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);

        // Convertir el timestamp a una fecha y hora legibles
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDateTime = sdf.format(date);

        Log.i("ALARMAS", "Alarma creada: ID " + i + ", timestamp " + formattedDateTime);

    }



}