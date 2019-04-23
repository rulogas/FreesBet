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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.freesbet.bases.BaseActivity.datosUsuarioActualizados;
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

    //Firestore
    FirebaseFirestore db;

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

        circleImageViewUsuarioMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irPerfil();
            }
        });


        rv =view.findViewById(R.id.recyclerView_eventos_proximos);
        initializeRecyclerView();
        //getEventos();


        db.collection("eventos")
                .whereEqualTo("finalizado", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Listener EventoLista", "Listen failed.", e);
                            return;
                        }
                        eventos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {

                            Log.d("EventoLista", document.getId() + " => " + document.getData());
                            Map<String, Object> eventoListaDb = document.getData();
                            List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoListaDb.get("apuestas") ;
                            int numeroJugadoresDb = listaApuestasDb.size();
                            EventoLista eventoLista = new EventoLista(document.getId(),(String)eventoListaDb.get("nombre"),
                                    (String)eventoListaDb.get("zona"),
                                    (String)eventoListaDb.get("urlImagen"),
                                    (String)eventoListaDb.get("fecha"),
                                    numeroJugadoresDb);
                            eventos.add(eventoLista);
                        }
                        adapter = new RVAdapter(eventos, getContext());
                        rv.setAdapter(adapter);
                        Log.d("Listener EventoLista", "Eventos actuales en: " + eventos);
                    }
                });


        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(llm);
    }

    private void getEventos() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando eventos");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection("eventos")
                .whereEqualTo("finalizado", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            eventos = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("EventoLista", document.getId() + " => " + document.getData());
                                Map<String, Object> eventoListaDb = document.getData();
                                List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoListaDb.get("apuestas") ;
                                int numeroJugadoresDb = listaApuestasDb.size();
                                EventoLista eventoLista = new EventoLista(document.getId(),(String)eventoListaDb.get("nombre"),
                                        (String)eventoListaDb.get("zona"),
                                        (String)eventoListaDb.get("urlImagen"),
                                        (String)eventoListaDb.get("fecha"),
                                        numeroJugadoresDb);
                                eventos.add(eventoLista);
                            }
                            adapter = new RVAdapter(eventos, getContext());
                            rv.setAdapter(adapter);

                            progressDialog.dismiss();

                        } else {
                            Log.d("EventoLista", "Error getting documents: ", task.getException());
                            Snackbar.make(getView(), "Error al cargar los eventos", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                /*
                // Obtener eventos y añadirlos a lista
                try {
                    // Imei
                    TelephonyManager telephonyManager = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
                    String imei = telephonyManager.getDeviceId();

                    url = new URL( "http://192.168.1.10/api/control/arranque?imei="+imei+"&battery="+getBatteryPercentage(MainActivity.this));

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }
                    // Devolver los datos al método onPostExecute
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(R.id.drawer_layout), "Error. Comprueba tu conexión a internet", Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(Color.RED)
                            .setAction("Reintentar", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getZonas();
                                }
                            })
                            .show();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }*/
                /*
                eventos = new ArrayList<>();
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Papo vs Trueno", "FMS - Argentina", "https://pbs.twimg.com/profile_images/1102595661034385409/wkdgl8ok.png","27/04/2019",500));
                eventos.add(new EventoLista(1,"Azcino vs RC", "FMS - Mexico", "https://pbs.twimg.com/profile_images/1098695785976381441/KbUn_B7T_400x400.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Nitro vs Kaiser", "FMS - Chile", "https://pbs.twimg.com/profile_images/1101153365428457477/xFoTQVHK_400x400.png","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));

*/
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            // Cargar eventos en lista
            adapter = new RVAdapter(eventos, getContext());
            rv.setAdapter(adapter);

            progressDialog.dismiss();

        }
    }

    private void irPerfil(){
        Intent in = new Intent(getActivity(),Ajustes.class);
        startActivity(in);
        getActivity().finish();
    }

}
