package com.example.freesbet;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinalizarEvento extends AppCompatActivity {

    FirebaseFirestore db;

    ProgressDialog progressDialog;

    @BindView(R.id.recyclerView_eventos_finalizar)
    RecyclerView rv;
    private List<EventoLista> eventos = new ArrayList<>();
    RVAdapter adapter;
    @BindView(R.id.textView_noHayEventos)
    TextView textViewNohayEventos;

    ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_evento);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Finalizar evento");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        db = FirebaseFirestore.getInstance();

        initializeRecyclerView();

        getEventos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(FinalizarEvento.this);

        rv.setLayoutManager(llm);
    }

    private void getEventos(){
        progressDialog = new ProgressDialog(FinalizarEvento.this);
        progressDialog.setMessage("Cargando eventos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //Query
        Query query = db.collection("eventos")
                .whereEqualTo("finalizado", false);
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
                    adapter = new RVAdapter(eventos, FinalizarEvento.this);
                    rv.setAdapter(adapter);
                    Log.d("Listener EventoLista", "Eventos actuales en: " + eventos);
                    progressDialog.dismiss();
                }
                else{
                    textViewNohayEventos.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }
        });
    }
}
