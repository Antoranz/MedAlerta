package com.example.myapplication.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.data.Doctor;
import com.example.myapplication.utils.Controller;

import com.example.myapplication.utils.manager.NavigationManager;
import com.example.myapplication.utils.manager.SessionManager;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


public class CrearConsultaFragment extends Fragment {

    SessionManager sessionManager;
    private Button crearConsulta;
    private EditText tituloConsulta;
    Spinner spinner;

    LinkedList<Doctor> listaDoctores;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_crear_consulta, container, false);

        sessionManager = new SessionManager(requireContext());

        initGui(rootView);

        return rootView;
    }
    private void initGui(View rootView) {

        spinner = rootView.findViewById(R.id.myspinner);
        crearConsulta = rootView.findViewById(R.id.buttonCrearConsulta);
        tituloConsulta = rootView.findViewById(R.id.editTextTituloConsulta);

        listaDoctores = Controller.getInstance().getDoctoresParaConsulta(requireContext(),sessionManager.getUserId());


        ArrayAdapter<Doctor> adapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listaDoctores);

        spinner.setAdapter(adapter);


            crearConsulta.setOnClickListener(v -> {
                if(listaDoctores.isEmpty()){

                    Toast.makeText(getContext(), "No hay doctores disponibles para crear consultas", Toast.LENGTH_LONG).show();

                }else {

                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    String titulo = tituloConsulta.getText().toString();

                    Doctor doctorSeleccionado = (Doctor) spinner.getSelectedItem();
                    String dniDoctorSeleccionado = doctorSeleccionado.getDni();
                    if(titulo.isEmpty()){
                        Toast.makeText(getContext(),"El título no puede ser vacio",Toast.LENGTH_LONG).show();
                    }else{
                        Controller.getInstance().postCrearConsulta(getContext(),dniDoctorSeleccionado,sessionManager.getUserId().toString(),titulo,timeStamp);
                        NavigationManager.getInstance().navigateToDestination(requireContext(), MainActivity.class);
                    }
                }





            });



        }



    }




