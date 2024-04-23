package com.example.myapplication.services;

import android.content.Context;
import java.util.LinkedList;

import com.example.myapplication.data.Cita;

public interface CitaService {
    public LinkedList<Cita> getAllCitas(Context c, String dni);
}
