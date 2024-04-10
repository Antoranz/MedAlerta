package com.example.myapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatViewActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.Consulta;

import java.util.LinkedList;
import java.util.List;

public class ConsultasListAdapter extends RecyclerView.Adapter<ConsultasListAdapter.ViewHolder> {
    protected LinkedList<Consulta> consultasList;

    protected Context context;
    protected View.OnClickListener onClickListener;
    public ConsultasListAdapter(LinkedList<Consulta> list, Context c){
        context = c;
        consultasList = list;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ConsultasListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultasListAdapter.ViewHolder holder, int position) {
        holder.bind(consultasList.get(position));
    }

    @Override
    public int getItemCount() {
        return consultasList.size();
    }

    public void setConsultasList(List<Consulta> data) {
        consultasList = (LinkedList<Consulta>) data;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public TextView titulo;
        public Context context;
        public Consulta e;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tituloTextView);

            view = itemView;
            this.context=context;

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {

            NavigationManager.getInstance().navigateToDestinationWithData(context, ChatViewActivity.class, e);

        }
        public void bind(Consulta b){
            e=b;
            if(b.getTitulo().length()>35){
                titulo.setText(b.getTitulo().substring(0,34)+"...");
            }else{
                titulo.setText(b.getTitulo());
            }
        }
    }
}