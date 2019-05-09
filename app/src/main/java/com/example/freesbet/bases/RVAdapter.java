package com.example.freesbet.bases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.freesbet.Apuesta;
import com.example.freesbet.FinalizarEvento;
import com.example.freesbet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventoViewHolder>{

    List<EventoLista> eventos;
    private Context mContext;
    SharedPreferences sharedpreferences;
    DocumentReference docRefEvento;
    FirebaseFirestore db;
    ProgressDialog progressDialog;


    public RVAdapter(List<EventoLista> eventos, Context mContext){
        this.eventos = eventos;
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
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

        if (mContext instanceof FinalizarEvento){
            eventoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCompetidores(eventos.get(i).id);
                }
            });

        }else{
            eventoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, Apuesta.class);
                    intent.putExtra("idEvento",eventos.get(i).id);
                    mContext.startActivity(intent);

                }
            });
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void getCompetidores(String idEvento){
        docRefEvento = db.collection("eventos").document(idEvento);
        docRefEvento.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Map<String, Object> eventoDb = document.getData();
                        List<String> listaCompetidores =(List<String>)eventoDb.get("competidores");
                        mostrarDialogGanador(listaCompetidores, idEvento);
                    }
                }
            }
        });


    }

    private void mostrarDialogGanador(List<String> listaCompetidores , String idEvento){
        CharSequence[] arrayCompetidores = listaCompetidores.toArray(new CharSequence[listaCompetidores.size()]);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle("Selecciona un ganador");
        mBuilder.setSingleChoiceItems(arrayCompetidores, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("Finalizando evento");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String elecciónGanador = listaCompetidores.get(i);
                docRefEvento.update("ganador",elecciónGanador,
                        "finalizado", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            dialogInterface.dismiss();
                            Toast.makeText(mContext,"El evento se ha finalizado con éxito",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
