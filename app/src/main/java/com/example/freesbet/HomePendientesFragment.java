package com.example.freesbet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

import butterknife.ButterKnife;

import static com.example.freesbet.bases.BaseActivity.idUsuario;

public class HomePendientesFragment extends Fragment {

    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<EventoLista> eventos = new ArrayList<>();
    RVAdapter adapter;
    TextView textViewNohayEventos;
    TextView textViewNivelUsuarioHeaderMenu;
    View headerView;
    NavigationView navigationView;

    //Firestore
    FirebaseFirestore db;
    ListenerRegistration registration;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_pendientes,container,false);
        ButterKnife.bind(getActivity());

        db = FirebaseFirestore.getInstance();

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        rv =view.findViewById(R.id.recyclerView_eventos_pendientes);
        textViewNohayEventos = view.findViewById(R.id.textView_noHayEventos);

        headerView = navigationView.getHeaderView(0);
        textViewNivelUsuarioHeaderMenu = headerView.findViewById(R.id.textView_NivelUsuario_headerMenu);
        // cargarNivel
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("usuarios").document(idUsuario);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Log.d("Usuario", "DocumentSnapshot data: " + documentSnapshot.getData());
                    Map<String, Object> user = documentSnapshot.getData();
                    int nivel =((Long) user.get("nivel")).intValue();
                    textViewNivelUsuarioHeaderMenu.setText("Nivel "+Integer.toString(nivel));
                } else {
                    Log.d("Usuario", "No such document");
                }
            }
        });

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
                        List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoListaDb.get("apuestas") ;
                        boolean pendiente = false;
                        if (!listaApuestasDb.isEmpty()){
                            for (Map<String,Object> apuesta : listaApuestasDb){
                                if (((String)apuesta.get("idUsuario")).equalsIgnoreCase(idUsuario)){
                                    pendiente = true;
                                }
                            }
                            if (pendiente){
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
                        }
                    }
                    if (!eventos.isEmpty()){
                        adapter = new RVAdapter(eventos, getActivity());
                        rv.setAdapter(adapter);
                        Log.d("Listener EventoLista", "Eventos actuales en: " + eventos);
                    }else{
                        textViewNohayEventos.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                }
                else{
                    textViewNohayEventos.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }
        });
    }

}
