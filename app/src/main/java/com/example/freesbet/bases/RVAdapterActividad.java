package com.example.freesbet.bases;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.freesbet.Apuesta;
import com.example.freesbet.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVAdapterActividad extends RecyclerView.Adapter<RVAdapterActividad.ActividadViewHolder>{

    static List<Actividad> actividades;
    private Context mContext;
    SharedPreferences sharedpreferences;

    public RVAdapterActividad(List<Actividad> actividades, Context mContext){
        this.actividades = actividades;
        this.mContext = mContext;
    }

    public static class ActividadViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageViewResultado;
        TextView textViewTextoActividad;


        ActividadViewHolder(View itemView) {
            super(itemView);
            circleImageViewResultado = itemView.findViewById(R.id.circleview_actividad_resultado);
            textViewTextoActividad = itemView.findViewById(R.id.textView_actividad_texto);
        }
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    @Override
    public ActividadViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_actividad, viewGroup, false);
        return new ActividadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ActividadViewHolder eventoViewHolder, final int i) {
        if (actividades.get(i).tipo.equalsIgnoreCase("bonus")){
            eventoViewHolder.circleImageViewResultado.setImageResource(R.drawable.ganada);
            eventoViewHolder.textViewTextoActividad.setText("Has ganado 1000 coins por subir de nivel");

        }else{
            if (actividades.get(i).resultado.equalsIgnoreCase("ganado")){
                eventoViewHolder.circleImageViewResultado.setImageResource(R.drawable.ganada);
                eventoViewHolder.textViewTextoActividad.setText("Has ganado tu apuesta en "+actividades.get(i).nombre);
                eventoViewHolder.textViewTextoActividad.setTextColor(Color.CYAN);
            }else{
                eventoViewHolder.circleImageViewResultado.setImageResource(R.drawable.perdida);
                eventoViewHolder.textViewTextoActividad.setText("Has perdido tu apuesta en "+actividades.get(i).nombre);
                eventoViewHolder.textViewTextoActividad.setTextColor(Color.parseColor("#ff5b5b"));
            }

            eventoViewHolder.textViewTextoActividad.setPaintFlags(eventoViewHolder.textViewTextoActividad.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

            eventoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Apuesta.class);
                    intent.putExtra("idEevnto",actividades.get(i).id);
                    mContext.startActivity(intent);

                }
            });
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
