package com.example.freesbet.bases;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.Apuesta;
import com.example.freesbet.R;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventoViewHolder>{

    List<EventoLista> eventos;
    private Context mContext;
    SharedPreferences sharedpreferences;

    public RVAdapter(List<EventoLista> eventos, Context mContext){
        this.eventos = eventos;
        this.mContext = mContext;
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView batalla;
        TextView nombre;
        ImageView imagen;
        TextView fecha;
        TextView numeroJugadores;


        EventoViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            batalla = itemView.findViewById(R.id.textView_batalla);
            nombre = itemView.findViewById(R.id.textView_nombre);
            imagen = itemView.findViewById(R.id.foto);
            fecha = itemView.findViewById(R.id.textView_fecha);
            numeroJugadores = itemView.findViewById(R.id.textView_numeroJugadores);
        }
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    @Override
    public EventoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_evento, viewGroup, false);
        return new EventoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventoViewHolder eventoViewHolder, final int i) {
        eventoViewHolder.batalla.setText(eventos.get(i).nombre);
        eventoViewHolder.nombre.setText(eventos.get(i).zona);
        Glide.with(mContext)
                .load(eventos.get(i).urlImagen)
                .into(eventoViewHolder.imagen);
        eventoViewHolder.fecha.setText(eventos.get(i).fecha);
        eventoViewHolder.numeroJugadores.setText(Integer.toString(eventos.get(i).numeroJugadores));


        eventoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Apuesta.class);
                intent.putExtra("idEvento",eventos.get(i).id);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
