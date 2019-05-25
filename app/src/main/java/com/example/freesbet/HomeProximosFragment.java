package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.bases.BooVariable;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.freesbet.bases.BaseActivity.datosUsuarioActualizados;
import static com.example.freesbet.bases.BaseActivity.idUsuario;
import static com.example.freesbet.bases.BaseActivity.nivelUsuario;
import static com.example.freesbet.bases.BaseActivity.nombreUsuario;
import static com.example.freesbet.bases.BaseActivity.photoUrlUsuario;

public class HomeProximosFragment extends Fragment {

    NavigationView navigationView;
    View headerView;
    TextView textViewNombreUsuarioHeaderMenu;
    TextView textViewNivelUsuarioHeaderMenu;
    CircleImageView circleImageViewUsuarioMenu;

    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<EventoLista> eventos = new ArrayList<>();
    RVAdapter adapter;
    TextView textViewNohayEventos;

    //Firestore
    FirebaseFirestore db;
    ListenerRegistration registration;

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rv = getActivity().findViewById(R.id.recyclerView_eventos_populares);
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_proximos,container,false);
        ButterKnife.bind(getActivity());

        db = FirebaseFirestore.getInstance();

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        // inicializar header menu

        headerView = navigationView.getHeaderView(0);
        textViewNombreUsuarioHeaderMenu = headerView.findViewById(R.id.textView_nombreUsuario_headerMenu);
        textViewNivelUsuarioHeaderMenu = headerView.findViewById(R.id.textView_NivelUsuario_headerMenu);
        circleImageViewUsuarioMenu = headerView.findViewById(R.id.circleview_header_perfil_usuario);

        // setear imagen menu cuando se guarda en firebase
        datosUsuarioActualizados.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (datosUsuarioActualizados.isBoo()){
                    Glide.with(AppFreesBet.mContext).load(photoUrlUsuario).into(circleImageViewUsuarioMenu);
                    textViewNombreUsuarioHeaderMenu.setText(nombreUsuario);
                    textViewNivelUsuarioHeaderMenu.setText("Nivel "+Integer.toString(nivelUsuario));
                }
            }
        });
        if (datosUsuarioActualizados.isBoo()){
            Glide.with(AppFreesBet.mContext).load(photoUrlUsuario).into(circleImageViewUsuarioMenu);
            textViewNombreUsuarioHeaderMenu.setText(nombreUsuario);
        }
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

        circleImageViewUsuarioMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irPerfil();
            }
        });

        rv =view.findViewById(R.id.recyclerView_eventos_proximos);
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
        //Query
        Query query = db.collection("eventos")
                .whereEqualTo("finalizado", false).orderBy("fecha", Query.Direction.ASCENDING);
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

    private void irPerfil(){
        Intent in = new Intent(getActivity(),Ajustes.class);
        startActivity(in);
        getActivity().finish();
    }

}
