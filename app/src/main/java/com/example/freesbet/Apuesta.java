package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.CustomAdapterSpinnerCompetidores;
import com.example.freesbet.bases.CustomAdpaterSpinner;
import com.example.freesbet.bases.EventoApuesta;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.widgets.CheckLogout;
import com.example.freesbet.widgets.General;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Apuesta extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    NavigationView navigationView;
    View headerView;
    CircleImageView circleImageViewUsuarioMenu;
    TextView textViewNivelUsuarioHeaderMenu;

    // HEADER
    @BindView(R.id.textView_tipo)
    TextView textViewTipo;
    @BindView(R.id.textView_apuesta_zona)
    TextView textViewApuestaZona;
    @BindView(R.id.textView_vs)
    TextView textViewVS;
    @BindView(R.id.textView_apuesta_fecha)
    TextView textViewFecha;
    @BindView(R.id.textView_apuesta_ayuda)
    TextView textViewAyuda;

    //LIGA
    @BindView(R.id.textView_apuesta_header_competidor_1)
    TextView textViewApuestaHeaderCompetidor1;
    @BindView(R.id.textView_apuesta_header_competidor_2)
    TextView textViewApuestaHeaderCompetidor2;
    @BindView(R.id.cardView_apostar_liga)
    CardView cardViewApostarLiga;
    @BindView(R.id.textView_apuesta_liga_competidor1)
    TextView textViewApuestaLigaCompetidor1;
    @BindView(R.id.textView_apuesta_liga_competidor2)
    TextView textViewApuestaLigaCompetidor2;
    @BindView(R.id.button_cuota1)
    AppCompatButton mButton_cuota1;
    @BindView(R.id.button_cuota2)
    AppCompatButton mButton_cuota2;

    @BindView(R.id.cardView_porcentajes_liga)
    CardView cardViewPorcentajesLiga;
    @BindView(R.id.textView_apuesta_liga_numJugadores)
    TextView textViewApuestaLigaNumJugadores;
    @BindView(R.id.textView_porcentajes_liga_competidor1)
    TextView texViewPorcentajesLigaCompetidor1;
    @BindView(R.id.textView_porcentajes_liga_competidor2)
    TextView texViewPorcentajesLigaCompetidor2;
    @BindView(R.id.textView_porcentajes_liga_competidor1_porcentaje)
    TextView textViewPorcentajesLigaCompetidor1Porcentaje;
    @BindView(R.id.textView_porcentajes_liga_competidor2_porcentaje)
    TextView textViewPorcentajesLigaCompetidor2Porcentaje;
    @BindView(R.id.progressBar_porcenaje1_liga)
    ProgressBar progressBarPorcenaje1Liga;
    @BindView(R.id.progressBar_porcenaje2_liga)
    ProgressBar progressBarPorcenaje2Liga;

    // COMPETICION
    @BindView(R.id.cardView_apostar_competicion)
    CardView mCardview_apostar_competicion;
    @BindView(R.id.textView_bote)
    TextView textViewBote;
    @BindView(R.id.spinner_competidores)
    Spinner mSpinnerCompetidores;
    @BindView(R.id.textView_apuesta_competicion_numJugadores)
    TextView textViewApuestaCompeticionNumJugadores;
    @BindView(R.id.button_jugar_competicion)
    AppCompatButton mButton_jugar_competicion;
    @BindView(R.id.cardView_porcentajes_competicion)

    CardView mCardviewPorcentajesCompeticion;
    @BindView(R.id.grid_porcentajes_competicion)
    GridLayout mGrid_porcentajes_competicion;

    //USUARIOS
    @BindView(R.id.circleview_image1)
    CircleImageView fotoUsuario1Apuesta;
    @BindView(R.id.textView_apuesta_masChulos1)
    TextView nombreUsuario1Apuesta;
    @BindView(R.id.textView_apuesta_masChulos1_coins)
    TextView coinsUsuario1Apuesta;

    @BindView(R.id.circleview_image2)
    CircleImageView fotoUsuario2Apuesta;
    @BindView(R.id.textView_apuesta_masChulos2)
    TextView nombreUsuario2Apuesta;
    @BindView(R.id.textView_apuesta_masChulos2_coins)
    TextView coinsUsuario2Apuesta;

    @BindView(R.id.circleview_image3)
    CircleImageView fotoUsuario3Apuesta;
    @BindView(R.id.textView_apuesta_masChulos3)
    TextView nombreUsuario3Apuesta;
    @BindView(R.id.textView_apuesta_masChulos3_coins)
    TextView coinsUsuario3Apuesta;

    @BindView(R.id.circleview_image4)
    CircleImageView fotoUsuario4Apuesta;
    @BindView(R.id.textView_apuesta_masChulos4)
    TextView nombreUsuario4Apuesta;
    @BindView(R.id.textView_apuesta_masChulos4_coins)
    TextView coinsUsuario4Apuesta;

    @BindView(R.id.relativeLayout_apuesta_hecha_liga_advertencia)
    RelativeLayout frameLayoutLigaApuestaHecha;
    @BindView(R.id.relativeLayout_apuesta_competicion_hecha_advertencia)
    RelativeLayout frameLayoutCompeticionApuestaHecha;

    //FINALIZADO
    @BindView(R.id.cardView_apostar_finalizado)
    CardView mCardviewApostarFinalizado;
    @BindView(R.id.textView_ganador)
    TextView textViewGanador;
    @BindView(R.id.textView_resultado_coins)
    TextView textViewResultadoCoins;

    String selecccionGanador;

    ProgressDialog progressDialog;

    EventoApuesta evento;
    ArrayList<String> competidores = new ArrayList<>();
    ArrayList<String> porcentajes = new ArrayList<>();

    //Firestore
    FirebaseFirestore db;
    ListenerRegistration registration;

    Map<String, Object> eventoDb;

    FirebaseUser user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuesta);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getText(R.string.title_activity_home));
        /*TextView texto;
        texto.setCompoundDrawablesRelative(ContextCompat.getDrawable(Apuesta.this,R.drawable.com_facebook_button_icon),null,null,null);*/

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

        cargarInfoUsuarioMenu();

        circleImageViewUsuarioMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irPerfil();
            }
        });

        db = FirebaseFirestore.getInstance();

        mButton_cuota1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragmentApuestaLiga bsdFragment = new
                        BottomSheetDialogFragmentApuestaLiga();
                Bundle args = new Bundle();
                args.putString("cuota",mButton_cuota1.getText().toString());
                //Enviar puntos de usuario
                args.putString("puntosUsuario","14500");
                bsdFragment.setArguments(args);
                bsdFragment.show(
                        Apuesta.this.getSupportFragmentManager(), "BSDialog");
            }
        });

        mButton_cuota2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragmentApuestaLiga bsdFragment = new
                        BottomSheetDialogFragmentApuestaLiga();
                Bundle args = new Bundle();
                args.putString("cuota",mButton_cuota2.getText().toString());
                //Enviar puntos de usuario
                args.putString("puntosUsuario","14500");
                bsdFragment.setArguments(args);
                bsdFragment.show(
                        Apuesta.this.getSupportFragmentManager(), "BSDialog");
            }
        });

        mButton_jugar_competicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTasksApostarCompeticion myAsyncTasksApostarCompeticion = new MyAsyncTasksApostarCompeticion();
                myAsyncTasksApostarCompeticion.execute();
            }
        });

        cargarEventoApuesta();

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
                Intent intent1= new Intent(Apuesta.this,Home.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.opcion_menu_fms:
                Intent intent2= new Intent(Apuesta.this,Fms.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.opcion_menu_redbull:
                Intent intent3= new Intent(Apuesta.this, Redbull.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.opcion_menu_bdm:
                Intent intent4= new Intent(Apuesta.this,Bdm.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.opcion_supremaciamc:
                Intent intent5= new Intent(Apuesta.this,Supremacia.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.opcion_ivita_amigos:
                Intent intent6= new Intent(Apuesta.this,Invita.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.opcion_ajustes_cuenta:
                Intent intent7= new Intent(Apuesta.this,Ajustes.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.opcion_logout:
                CheckLogout dialogFragment = new CheckLogout();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager,"checkLogout");
                break;
            case R.id.opcion_premios:
                Intent intent8= new Intent(Apuesta.this,Premios.class);
                startActivity(intent8);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cargarEventoApuesta() {
        progressDialog = new ProgressDialog(Apuesta.this);
        progressDialog.setMessage("Cargando evento");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //Query
        String idEvento = getIntent().getStringExtra("idEvento");
        if (idEvento == null){
            Toast.makeText(AppFreesBet.mContext,"No se ha encontrado el evento",Toast.LENGTH_SHORT);
            finish();
        }
        DocumentReference documentReference = db.collection("eventos").document(getIntent().getStringExtra("idEvento"));
        registration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // Cargar objeto Evento
                    Log.d("EVENTO", "Current data: " + snapshot.getData());
                    eventoDb = snapshot.getData();

                    String pattern = "dd-MM-yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String fecha = simpleDateFormat.format((Date) eventoDb.get("fecha"));

                    competidores = (ArrayList<String>) eventoDb.get("competidores");

                    evento = new EventoApuesta(
                            idEvento,
                            (String)eventoDb.get("nombre"),
                            (String)eventoDb.get("zona"),
                            fecha,
                            ((Long)eventoDb.get("numeroApuestas")).intValue(),
                            (String)eventoDb.get("tipo"),
                            competidores,
                            ((Double)eventoDb.get("cuota1")),
                            ((Double)eventoDb.get("cuota2")));

                    // Cargar interfaz
                    textViewFecha.setText(evento.getFecha());
                    textViewTipo.setText(evento.getTipo());
                    textViewApuestaZona.setText(evento.getZona());
                    textViewAyuda.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (evento.getTipo().equalsIgnoreCase("liga")){
                                General dialogFragment = new General();
                                Bundle mensaje = new Bundle();
                                mensaje.putString("titulo","Apuesta en una liga");
                                mensaje.putString("mensaje","Apuesta a un competidor, una cantidad determinada de coins. Si ganas, la cantidad obtenida variará según la cuota de cada competidor. Se sigue las mismas normas que un sistema de apuestas estandar.");
                                dialogFragment.setArguments(mensaje);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                dialogFragment.show(fragmentManager,"general");
                            }
                            else if(evento.getTipo().equalsIgnoreCase("competicion")){
                                General dialogFragment = new General();

                                Bundle mensaje = new Bundle();
                                mensaje.putString("titulo","Apuesta en una competición");
                                mensaje.putString("mensaje", "Apuesta 400 coins. Si aciertas en la elección del competidor recibirás parte del bote acumulado.");
                                dialogFragment.setArguments(mensaje);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                dialogFragment.show(fragmentManager,"general");
                            }
                        }
                    });

                    //comprobar tipo evento mostrar layout liga o competición
                    // LIGA
                    if (evento.getTipo().equalsIgnoreCase("liga")){
                        // Comprobar si el evento ha finalizado
                        if (((Boolean)eventoDb.get("finalizado"))== false){
                            mCardviewApostarFinalizado.setVisibility(View.GONE);


                            // comprobar si el usuario ha apostado en este evento con el id de evento y las apuestas
                            if (comprobarApuestaUsuario()){
                                // mostrar advertencia con datos y desactivar botones
                                /*frameLayoutLigaApuestaHecha.setVisibility(View.VISIBLE);
                                mButton_cuota1.setEnabled(false);
                                mButton_cuota2.setEnabled(false);*/
                            }
                            /* mostrar datos y esconder cards competicion */
                            textViewApuestaHeaderCompetidor1.setVisibility(View.VISIBLE);
                            textViewApuestaHeaderCompetidor1.setText(competidores.get(0));
                            textViewApuestaHeaderCompetidor2.setVisibility(View.VISIBLE);
                            textViewApuestaHeaderCompetidor2.setText(competidores.get(1));
                            mCardview_apostar_competicion.setVisibility(View.GONE);
                            mCardviewPorcentajesCompeticion.setVisibility(View.GONE);
                            cardViewApostarLiga.setVisibility(View.VISIBLE);
                            textViewApuestaLigaCompetidor1.setText(competidores.get(0));
                            textViewApuestaLigaCompetidor2.setText(competidores.get(1));
                            mButton_cuota1.setText(Double.toString(evento.getCuota1()));
                            mButton_cuota2.setText(Double.toString(evento.getCuota2()));
                        }else{
                            cardViewApostarLiga.setVisibility(View.GONE);
                            mCardviewApostarFinalizado.setVisibility(View.VISIBLE);
                            textViewGanador.setText((String)eventoDb.get("ganador"));
                            List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas") ;
                            String eleccionUsuario = "";
                            int coins = 0;
                            int gananciaPotencial = 0;
                            for (Map<String,Object> apuesta : listaApuestasDb){
                                if (((String)apuesta.get("idUsuario")).equalsIgnoreCase(idUsuario)){
                                    eleccionUsuario = (String)apuesta.get("elección");
                                    coins = ((Long) apuesta.get("coins")).intValue();
                                    gananciaPotencial = ((Long) apuesta.get("gananciaPotencial")).intValue();
                                    break;
                                }
                            }
                            if (((String)eventoDb.get("ganador")).equalsIgnoreCase(eleccionUsuario)){
                                textViewResultadoCoins.setText("Has ganado "+String.valueOf(gananciaPotencial)+" coins");
                            }else{
                                textViewResultadoCoins.setText("Has perdido "+String.valueOf(coins)+" coins");
                            }

                        }
                        //card porcentajes liga
                        cardViewPorcentajesLiga.setVisibility(View.VISIBLE);
                        textViewApuestaLigaNumJugadores.setText("Jugadores: "+Integer.toString(evento.getNumeroJugadores()));
                        texViewPorcentajesLigaCompetidor1.setText(competidores.get(0));
                        texViewPorcentajesLigaCompetidor2.setText(competidores.get(1));

                        // función porcentajes
                        ArrayList<Porcentaje> porcentajesApuestasCompetidores = getPorcentajesApuestasCompetidores();
                        if (!porcentajesApuestasCompetidores.isEmpty()){
                            textViewPorcentajesLigaCompetidor1Porcentaje.setText(String.valueOf(porcentajesApuestasCompetidores.get(0).porcentaje)+"%");
                            progressBarPorcenaje1Liga.setProgress(porcentajesApuestasCompetidores.get(0).porcentaje);

                            textViewPorcentajesLigaCompetidor2Porcentaje.setText(String.valueOf(porcentajesApuestasCompetidores.get(1).porcentaje)+"%");
                            progressBarPorcenaje2Liga.setProgress(porcentajesApuestasCompetidores.get(1).porcentaje);
                        }else{
                            textViewPorcentajesLigaCompetidor1Porcentaje.setText("0%");
                            progressBarPorcenaje1Liga.setProgress(0);

                            textViewPorcentajesLigaCompetidor2Porcentaje.setText("0%");
                            progressBarPorcenaje2Liga.setProgress(0);
                        }

                    }
                    //funcion obtener apuestas mas caras nombre, imagen, coins, competidor
                    ArrayList<MasGallo> listaMasGallos = getMasGallos();

                    for (int i=0;i<listaMasGallos.size();i++){
                        switch (i){
                            case 0:
                                cargarImagenUsuario(fotoUsuario1Apuesta,listaMasGallos.get(0).idUsuario);
                                nombreUsuario1Apuesta.setText(listaMasGallos.get(0).nombreUsuario);
                                coinsUsuario1Apuesta.setText(String.valueOf(listaMasGallos.get(0).getCoins()+" coins a "+listaMasGallos.get(0).eleccion));
                                break;
                            case 1:
                                cargarImagenUsuario(fotoUsuario2Apuesta,listaMasGallos.get(1).idUsuario);
                                nombreUsuario2Apuesta.setText(listaMasGallos.get(1).nombreUsuario);
                                coinsUsuario2Apuesta.setText(String.valueOf(listaMasGallos.get(1).getCoins()+" coins a "+listaMasGallos.get(1).eleccion));
                                break;
                            case 2:
                                cargarImagenUsuario(fotoUsuario3Apuesta,listaMasGallos.get(2).idUsuario);
                                nombreUsuario3Apuesta.setText(listaMasGallos.get(2).nombreUsuario);
                                coinsUsuario3Apuesta.setText(String.valueOf(listaMasGallos.get(2).getCoins()+" coins a "+listaMasGallos.get(2).eleccion));
                                break;
                            case 3:
                                cargarImagenUsuario(fotoUsuario4Apuesta,listaMasGallos.get(3).idUsuario);
                                nombreUsuario4Apuesta.setText(listaMasGallos.get(3).nombreUsuario);
                                coinsUsuario4Apuesta.setText(String.valueOf(listaMasGallos.get(3).getCoins()+" coins a "+listaMasGallos.get(3).eleccion));
                                break;

                        }
                    }

                } else {
                    Log.d("EVENTO", "Current data: null");
                    Toast.makeText(AppFreesBet.mContext,"No se ha encontrado el evento",Toast.LENGTH_SHORT);
                    finish();
                }
            }
        });


        /*}else if(evento.getTipo().equalsIgnoreCase("competicion")){
            // COMPETICION
            // comprobar si el usuario ha apostado en este evento con el id de evento y las apuestas
            // mostrar advertencia con datos y desactivar botones
                        *//*frameLayoutCompeticionApuestaHecha.setVisibility(View.VISIBLE);
                        mButton_jugar_competicion.setEnabled(false);*//*
            *//* esconder textos header, esconder cards liga, mostrar datos  *//*
            textViewTipo.setText("Competición");
            textViewApuestaHeaderCompetidor1.setVisibility(View.GONE);
            textViewApuestaHeaderCompetidor2.setVisibility(View.GONE);
            textViewVS.setText(evento.getNombre());
            //card apostar competicion
            mCardview_apostar_competicion.setVisibility(View.VISIBLE);
            textViewApuestaCompeticionNumJugadores.setText(Integer.toString(evento.getNumeroJugadores()));
            textViewBote.setText("20000");

            // spinner competidores
            mSpinnerCompetidores.setOnItemSelectedListener(Apuesta.this);
            CustomAdapterSpinnerCompetidores customAdapterSpinnerCompetidores = new CustomAdapterSpinnerCompetidores(Apuesta.this,competidores);
            mSpinnerCompetidores.setAdapter(customAdapterSpinnerCompetidores);

            // card porcentajes
            mCardviewPorcentajesCompeticion.setVisibility(View.VISIBLE);
            for (int i=0;i<porcentajes.size();i++){
                LinearLayout ll = (LinearLayout)mGrid_porcentajes_competicion.getChildAt(i);
                TextView textoCompeticionPorcentaje =(TextView) ll.getChildAt(0);
                TextView textoCompeticionPorcentajeCompetidor = (TextView)ll.getChildAt(1);
                textoCompeticionPorcentaje.setText(porcentajes.get(i)+"%");
                textoCompeticionPorcentajeCompetidor.setText(competidores.get(i));
            }


        }*/
        // usuarios



        progressDialog.dismiss();
    }

    private boolean comprobarApuestaUsuario(){
        boolean apostado = false;
        user = FirebaseAuth.getInstance().getCurrentUser();
        List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas") ;

        for(Map<String,Object> apuesta: listaApuestasDb){
            if (apuesta.containsKey("idUsuario") && apuesta.containsValue(idUsuario)){
                apostado = true;
                break;
            }
        }
        return apostado;
    }

    private ArrayList<Porcentaje> getPorcentajesApuestasCompetidores(){
        ArrayList<Porcentaje> porcentajesApuestasCompetidores = new ArrayList<>();
        List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas") ;
        if (listaApuestasDb.size()>0){
            for (String competidor : competidores){
                int numeroApuestasCompetidor = 0;
                for(Map<String,Object> apuesta: listaApuestasDb){
                    if (competidor.equalsIgnoreCase((String) apuesta.get("elección"))){
                        numeroApuestasCompetidor++;
                    }
                }
                int porcentaje = (100*numeroApuestasCompetidor)/listaApuestasDb.size();

                porcentajesApuestasCompetidores.add(new Porcentaje(competidor,porcentaje));
            }
        }
        return porcentajesApuestasCompetidores;
    }

    private ArrayList<MasGallo> getMasGallos(){
        ArrayList<MasGallo> listaMasGallos = new ArrayList<>();

        List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas") ;
        for (Map<String,Object> apuesta : listaApuestasDb){
            if (listaMasGallos.size()>=4){
                break;
            }
            listaMasGallos.add(new MasGallo(
                    (String)apuesta.get("idUsuario"),
                    (String)apuesta.get("nombreUsuario"),
                    (String)apuesta.get("elección"),
                    ((Long) apuesta.get("coins")).intValue()));
            listaMasGallos.sort(Comparator.comparing(MasGallo::getCoins).reversed());
        }
        return listaMasGallos;
    }
    private void cargarImagenUsuario(CircleImageView imagenUsuario, String idUsuario){
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child("imagenes/usuarios/"+idUsuario+"/imagen_usuario.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                System.out.println("uri obtenida");
                Glide.with(Apuesta.this).load(uri).into(imagenUsuario);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selecccionGanador = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selecccionGanador = parent.getItemAtPosition(0).toString();
    }

    public class MyAsyncTasksApostarCompeticion extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String current = "";
            try {

                // Firebase Restar 400 puntos y añadir apuesta



            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            /*if (OK){

            }*/
            mButton_jugar_competicion.setEnabled(false);
            frameLayoutCompeticionApuestaHecha.setVisibility(View.VISIBLE);

        }
    }

    private void cargarInfoUsuarioMenu(){
        headerView = navigationView.getHeaderView(0);
        TextView textViewNombreUsuarioHeaderMenu = headerView.findViewById(R.id.textView_nombreUsuario_headerMenu);
        textViewNombreUsuarioHeaderMenu.setText(nombreUsuario);
        circleImageViewUsuarioMenu = headerView.findViewById(R.id.circleview_header_perfil_usuario);
        Glide.with(getApplicationContext()).load(photoUrlUsuario).into(circleImageViewUsuarioMenu);
        textViewNivelUsuarioHeaderMenu = headerView.findViewById(R.id.textView_NivelUsuario_headerMenu);
        textViewNivelUsuarioHeaderMenu.setText("Nivel "+Integer.toString(nivelUsuario));
    }

    private void irPerfil(){
        startActivity(Apuesta.this,Ajustes.class);
        finish();
    }

    public class Porcentaje{
        String nombreCompetidor;
        int porcentaje;

        public Porcentaje(String nombreCompetidor, int porcentaje) {
            this.nombreCompetidor = nombreCompetidor;
            this.porcentaje = porcentaje;
        }
    }

    public class MasGallo{
        String idUsuario;
        String nombreUsuario;
        String eleccion;
        int coins;

        public MasGallo(String idUsuario, String nombreUsuario, String eleccion, int coins) {
            this.idUsuario = idUsuario;
            this.nombreUsuario = nombreUsuario;
            this.eleccion = eleccion;
            this.coins = coins;
        }

        public int getCoins() {
            return coins;
        }

    }
}
