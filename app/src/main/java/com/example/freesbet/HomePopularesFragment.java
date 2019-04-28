package com.example.freesbet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class HomePopularesFragment extends Fragment {

    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<EventoLista> eventos = new ArrayList<>();
    RVAdapter adapter;
    TextView textViewNohayEventos;


    //Firestore
    FirebaseFirestore db;
    ListenerRegistration registration;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_populares,container,false);
        ButterKnife.bind(getActivity());

        db = FirebaseFirestore.getInstance();

        rv =view.findViewById(R.id.recyclerView_eventos_populares);
        textViewNohayEventos = view.findViewById(R.id.textView_noHayEventos);

        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeRecyclerView();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando eventos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Query query = db.collection("eventos")
                .whereEqualTo("finalizado", false).orderBy("numeroApuestas", Query.Direction.DESCENDING);
        registration =query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Listener EventoLista", "Listen failed.", e);
                    return;
                }
                if (value != null && !value.isEmpty()){
                    textViewNohayEventos.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    eventos.clear();
                    for (QueryDocumentSnapshot document : value) {

                        Log.d("EventoLista", document.getId() + " => " + document.getData());
                        Map<String, Object> eventoListaDb = document.getData();
                            /*List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoListaDb.get("apuestas") ;
                            int numeroJugadoresDb = listaApuestasDb.size();*/
                        String pattern = "dd-MM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                        String fecha = simpleDateFormat.format((Date) eventoListaDb.get("fecha"));

                        EventoLista eventoLista = new EventoLista(document.getId(),(String)eventoListaDb.get("nombre"),
                                (String)eventoListaDb.get("zona"),
                                (String)eventoListaDb.get("urlImagen"),
                                fecha,
                                ((Long)eventoListaDb.get("numeroApuestas")).intValue());
                        eventos.add(eventoLista);
                    }
                    adapter = new RVAdapter(eventos, getActivity());
                    rv.setAdapter(adapter);
                    Log.d("Listener EventoLista", "Eventos actuales en: " + eventos);
                }
                else{
                    textViewNohayEventos.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }
        });
        progressDialog.dismiss();
    }

}
