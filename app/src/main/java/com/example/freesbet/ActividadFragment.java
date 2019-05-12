package com.example.freesbet;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.freesbet.bases.Actividad;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.bases.RVAdapterActividad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static com.example.freesbet.bases.BaseActivity.idUsuario;

public class ActividadFragment extends Fragment {

    RecyclerView rv;
    TextView textViewNoHayEventos;
    ProgressDialog progressDialog;

    RVAdapterActividad adapter;
    FirebaseFirestore db;

    List<Actividad> actividades;

    ListenerRegistration registration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividad,container,false);


        // setear imagen fondo con glide para aumentar rendimiento

        Glide.with(this).load(R.drawable.fondo_login).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });
        textViewNoHayEventos = view.findViewById(R.id.textView_noHayEventos);
        rv =view.findViewById(R.id.recyclerView_actividad);
        getActividad();
        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    private void getActividad(){

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando actividad");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Query query = db.collection("eventos");

        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.isEmpty() || queryDocumentSnapshots == null) {
                    rv.setVisibility(View.GONE);
                    textViewNoHayEventos.setVisibility(View.VISIBLE);
                    textViewNoHayEventos.setText("No hay eventos");
                } else {
                    actividades = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> eventoDb = document.getData();
                        List<Map<String, Object>> listaApuestasDb = (List<Map<String, Object>>) eventoDb.get("apuestas");
                        for (Map<String, Object> apuesta : listaApuestasDb) {
                            if (((String) apuesta.get("idUsuario")).equalsIgnoreCase(idUsuario)) {
                                if ((boolean)eventoDb.get("finalizado")){
                                    if (((String) apuesta.get("elección")).equalsIgnoreCase((String) eventoDb.get("ganador"))) {
                                        actividades.add(new Actividad(
                                                document.getId(),
                                                (String)eventoDb.get("nombre"),
                                                "ganado",
                                                (String)eventoDb.get("tipo"),
                                                (Date)eventoDb.get("fechaFinalizacion")));
                                    } else {
                                        actividades.add(new Actividad(
                                                document.getId(),
                                                (String)eventoDb.get("nombre"),
                                                "perdido",
                                                (String)eventoDb.get("tipo"),
                                                (Date)eventoDb.get("fechaFinalizacion")));
                                    }
                                    if (apuesta.containsKey("coinsNivel")){
                                        actividades.add(new Actividad("-1","Subida de nivel", "ganado", "bonus", (Date)apuesta.get("fechaApuesta")));
                                    }
                                }else{
                                    if (apuesta.containsKey("coinsNivel")){
                                        actividades.add(new Actividad("-1","Subida de nivel", "ganado", "bonus", (Date)apuesta.get("fechaApuesta")));
                                    }
                                }
                            }
                        }
                    }
                    if (!actividades.isEmpty()){
                        actividades.sort(Comparator.comparing(Actividad::getFecha).reversed());
                        adapter = new RVAdapterActividad(actividades,getContext());
                        rv.setAdapter(adapter);

                        progressDialog.dismiss();

                    }else{
                        rv.setVisibility(View.GONE);
                        textViewNoHayEventos.setVisibility(View.VISIBLE);
                        textViewNoHayEventos.setText("No hay actividad en tu perfil");
                        progressDialog.dismiss();
                    }


                }
            }
        });

    }

}