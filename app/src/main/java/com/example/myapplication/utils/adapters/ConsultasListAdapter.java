package com.example.myapplication.utils.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.activities.ChatViewActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.Consulta;
import com.example.myapplication.utils.Controller;
import com.example.myapplication.utils.manager.NavigationManager;

import java.util.LinkedList;
import java.util.List;

public class ConsultasListAdapter extends RecyclerView.Adapter<ConsultasListAdapter.ViewHolder> {
    protected LinkedList<Consulta> consultasList;

    protected Context context;
    protected String dni;
    protected View.OnClickListener onClickListener;
    public ConsultasListAdapter(LinkedList<Consulta> list, Context c,String dni_paciente){
        context = c;
        consultasList = list;
        dni = dni_paciente;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ConsultasListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view,context,dni);
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

        public String dni;

        public ImageView image_notificacion;
        public ViewHolder(@NonNull View itemView, Context context,String dni) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tituloTextView);
            image_notificacion = itemView.findViewById(R.id.imageViewMensajesNoLeidos);

            this.dni = dni;

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

            boolean flag = Controller.getInstance().getSaberSiHayMensajesNoLeidos(b.getId(),dni);
            Log.d("FlagValue", "El valor de flag es: " + flag);


            //LA IMAGEN NO FUNCIONA Y ME PETA, EL RESTO EN PRINCIPIO VA BIEN
            if(image_notificacion!=null){

                if (flag){
                    image_notificacion.setVisibility(View.VISIBLE);

                }else{
                    image_notificacion.setVisibility(View.GONE);
                }
            }


            if(b.getTitulo().length()>35){
                titulo.setText(b.getTitulo().substring(0,34)+"...");
            }else{
                titulo.setText(b.getTitulo());
            }
        }
    }
}