package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.CustomAdpaterSpinner;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.widgets.CheckLogout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Fms extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    View headerView;
    CircleImageView circleImageViewUsuarioMenu;
    TextView textViewNivelUsuarioHeaderMenu;


    @BindView(R.id.spinner_fms)
    Spinner mSpinerPaises;
    private String [] mNombresSpinner;
    private String [] mUrlsImagenesSpinner;
    CustomAdpaterSpinner adpaterSpinner;

    @BindView(R.id.recyclerView_eventos_fms)
    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<EventoLista> eventos = new ArrayList<>();
    RVAdapter adapter;
    @BindView(R.id.textView_noHayEventos)
    TextView textViewNohayEventos;

    //Firestore
    FirebaseFirestore db;
    ListenerRegistration registration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fms);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getText(R.string.title_activity_fms));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        MenuItem ligas= menu.findItem(R.id.ligas);
        SpannableString stringLigas = new SpannableString(ligas.getTitle());
        stringLigas.setSpan(new TextAppearanceSpan(this, R.style.ColorOpcionesMenu), 0, stringLigas.length(), 0);
        ligas.setTitle(stringLigas);

        MenuItem competiciones= menu.findItem(R.id.competiciones);
        SpannableString stringCompeticiones = new SpannableString(competiciones.getTitle());
        stringCompeticiones.setSpan(new TextAppearanceSpan(this, R.style.ColorOpcionesMenu), 0, stringCompeticiones.length(), 0);
        competiciones.setTitle(stringCompeticiones);

        navigationView.setNavigationItemSelectedListener(this);

        // setear info usuario
        cargarInfoUsuarioMenu();

        circleImageViewUsuarioMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irPerfil();
            }
        });


        inicializarSpinner();

        db = FirebaseFirestore.getInstance();


        initializeRecyclerView();

        mSpinerPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (registration != null){
                    registration.remove();
                }
                getEventos(mNombresSpinner[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        switch (id){

            case R.id.opcion_todos:
                Intent intent1= new Intent(Fms.this,Home.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.opcion_menu_fms:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.opcion_menu_redbull:
                Intent intent3= new Intent(Fms.this, Redbull.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.opcion_menu_bdm:
                Intent intent4= new Intent(Fms.this,Bdm.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.opcion_supremaciamc:
                Intent intent5= new Intent(Fms.this,Supremacia.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.opcion_ivita_amigos:
                Intent intent6= new Intent(Fms.this,Invita.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.opcion_ajustes_cuenta:
                Intent intent7= new Intent(Fms.this,Ajustes.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.opcion_logout:
                CheckLogout dialogFragment = new CheckLogout();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager,"checkLogout");
                break;
            case R.id.opcion_premios:
                Intent intent8= new Intent(Fms.this,Premios.class);
                startActivity(intent8);
                finish();
                break;
            // this is done, now let us go and intialise the home page.
            // after this lets start copying the above.
            // FOLLOW MEEEEE>>>
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void inicializarSpinner(){
        mNombresSpinner = new String[]{"Todos","España", "Argentina", "México", "Chile"};
        mUrlsImagenesSpinner = new String[]{"https://www.materialui.co/materialIcons/places/all_inclusive_white_192x192.png",
                "http://flags.fmcdn.net/data/flags/w580/es.png",
                "https://cdn.pixabay.com/photo/2013/07/13/14/14/argentina-162229_960_720.png",
                "http://flags.fmcdn.net/data/flags/w580/mx.png",
                "http://flags.fmcdn.net/data/flags/w580/cl.png"};

        adpaterSpinner = new CustomAdpaterSpinner(this,mNombresSpinner,mUrlsImagenesSpinner);
        mSpinerPaises.setAdapter(adpaterSpinner);

    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(Fms.this);
        rv.setLayoutManager(llm);
    }


    private void getEventos(String filtroZona) {
        //Query
        Query query = null;
        if (filtroZona.equalsIgnoreCase("todos")){
            query = db.collection("eventos")
                    .whereEqualTo("finalizado", false)
                    .whereEqualTo("evento","FMS")
                    .orderBy("fecha", Query.Direction.ASCENDING);
        }else{
            query = db.collection("eventos")
                    .whereEqualTo("finalizado", false)
                    .whereEqualTo("evento","FMS")
                    .whereEqualTo("zona","FMS - "+filtroZona)
                    .orderBy("fecha", Query.Direction.ASCENDING);
        }
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
                    adapter = new RVAdapter(eventos, Fms.this);
                    rv.setAdapter(adapter);
                    Log.d("Listener EventoLista", "Eventos actuales en: " + eventos);
                }
                else{
                    textViewNohayEventos.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }
        });
    }



    private void cargarInfoUsuarioMenu(){
        headerView = navigationView.getHeaderView(0);
        TextView textViewNombreUsuarioHeaderMenu = headerView.findViewById(R.id.textView_nombreUsuario_headerMenu);
        textViewNombreUsuarioHeaderMenu.setText(nombreUsuario);
        circleImageViewUsuarioMenu = headerView.findViewById(R.id.circleview_header_perfil_usuario);
        Glide.with(getApplicationContext()).load(photoUrlUsuario).into(circleImageViewUsuarioMenu);
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
    }

    private void irPerfil(){
        startActivity(Fms.this,Ajustes.class);
        finish();
    }

}
