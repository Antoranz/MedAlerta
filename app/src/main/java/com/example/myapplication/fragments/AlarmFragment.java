package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.adapters.ConsultasListAdapter;
import com.example.myapplication.utils.manager.SessionManager;

import java.util.LinkedList;

public class AlarmFragment extends Fragment {

    private SessionManager sessionManager;

    private ConsultasListAdapter adapter;
    private Button crearConsulta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_consultas, container, false);

        sessionManager = new SessionManager(requireContext());

        initGui(rootView);

        return rootView;
    }

    private void initGui(View rootView) {
        RecyclerView rv = rootView.findViewById(R.id.recyclerViewConsultas);

        crearConsulta = rootView.findViewById(R.id.ButtonCrearConsulta);

        crearConsulta.setOnClickListener(v -> {
            replaceFragment(new CrearConsultaFragment());
        });

        adapter = new ConsultasListAdapter(new LinkedList<>(), requireContext(),sessionManager.getUserId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        adapter.setConsultasList(Controller.getInstance().getAllConsultas(requireContext(), sessionManager.getUserId()));
        adapter.notifyDataSetChanged();
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
