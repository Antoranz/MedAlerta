package com.example.myapplication.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Cita;

import java.util.LinkedList;
import java.util.List;

public class CitasListAdapter extends RecyclerView.Adapter<CitasListAdapter.ViewHolder> {
    protected LinkedList<Cita> citasList;

    protected Context context;
    protected View.OnClickListener onClickListener;
    public CitasListAdapter(LinkedList<Cita> list, Context c){
        context = c;
        citasList = list;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CitasListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cita_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new CitasListAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CitasListAdapter.ViewHolder holder, int position) {
        holder.bind(citasList.get(position));
    }

    @Override
    public int getItemCount() {
        return citasList.size();
    }

    public void setCitasList(List<Cita> data) {
        citasList = (LinkedList<Cita>) data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public TextView titulo;
        public TextView fecha;
        public Context context;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textViewTitulo);
            fecha = itemView.findViewById(R.id.textViewFecha);

            view = itemView;
            this.context=context;

            itemView.setOnClickListener(this);

        }
        public void bind(Cita c){
            String t = "Cita Programadada  el doctor: "+c.getNombre_doctor() + " " + c.getApellidos_doctor();
            titulo.setText(t);
            fecha.setText(c.writeDate());
        }

        @Override
        public void onClick(View view) {
            //nada
        }
    }
}