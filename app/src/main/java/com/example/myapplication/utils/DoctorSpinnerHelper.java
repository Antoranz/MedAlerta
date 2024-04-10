// DoctorSpinnerHelper.java
package com.example.myapplication.utils;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class DoctorSpinnerHelper {

    public static void setupDoctorSpinner(Context context, Spinner spinner, List<String> listaDoctores) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaDoctores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Seleccionar el primer elemento por defecto
        if (!listaDoctores.isEmpty()) {
            spinner.setSelection(0);
        }
    }

    public static void setupDoctorSpinnerWithListener(Context context, Spinner spinner, List<String> listaDoctores, AdapterView.OnItemSelectedListener listener) {
        setupDoctorSpinner(context, spinner, listaDoctores);
        spinner.setOnItemSelectedListener(listener);
    }

}
