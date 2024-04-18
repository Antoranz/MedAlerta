package com.example.myapplication.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Mensaje;

import java.util.LinkedList;
import java.util.List;

public class MensajesListAdapter extends RecyclerView.Adapter<MensajesListAdapter.ViewHolder> {
    protected LinkedList<Mensaje> mensajesList;

    protected Context context;
    protected View.OnClickListener onClickListener;
    public MensajesListAdapter(LinkedList<Mensaje> list, Context c){
        context = c;
        mensajesList = list;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MensajesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje, parent, false);

            //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.gravity = Gravity.END;
            //view.setLayoutParams(layoutParams);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje0, parent, false);
        }
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view,context,viewType);
    }
    public void setMensajesList(List<Mensaje> data) {
        mensajesList.clear();
        mensajesList.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        int propietario = mensajesList.get(position).getPropietario();

        return propietario;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(mensajesList.get(position));
    }


    @Override
    public int getItemCount() {
        return mensajesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView mensaje;
        public Context context;
        public ViewHolder(@NonNull View itemView, Context context , int viewType) {
            super(itemView);

            mensaje = itemView.findViewById(R.id.textViewMessage);

            view = itemView;
            this.context=context;

        }

        public void bind(Mensaje b){
            mensaje.setText(b.getMensaje());
        }
    }
}