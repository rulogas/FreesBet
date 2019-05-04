package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.AdapterGridPremios;
import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.Premio;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.widgets.CheckLogout;
import com.example.freesbet.widgets.DialogFormEnviarPremio;
import com.example.freesbet.widgets.General;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Premios extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    NavigationView navigationView;
    View headerView;
    CircleImageView circleImageViewUsuarioMenu;
    TextView textViewNivelUsuarioHeaderMenu;

    private List<Premio> premios;
    ProgressDialog progressDialog;
    AdapterGridPremios adapterGridPremios;
    @BindView(R.id.gridView_premios)
    GridView gridViewPremios;

    FirebaseFirestore db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premios);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getText(R.string.title_activity_premios));

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

        getPremios();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            startActivity(Premios.this,Home.class);
            finish();
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
                Intent intent= new Intent(Premios.this,Home.class);
                startActivity(intent);
                finish();
                break;
            case R.id.opcion_menu_fms:
                Intent intent2= new Intent(Premios.this,Fms.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.opcion_menu_redbull:
                Intent intent3= new Intent(Premios.this, Redbull.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.opcion_menu_bdm:
                Intent intent4= new Intent(Premios.this,Bdm.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.opcion_supremaciamc:
                Intent intent5= new Intent(Premios.this,Supremacia.class);
                startActivity(intent5);
                finish();
                break;

            case R.id.opcion_ivita_amigos:
                Intent intent6= new Intent(Premios.this,Invita.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.opcion_ajustes_cuenta:
                Intent intent7= new Intent(Premios.this,Ajustes.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.opcion_logout:
                CheckLogout dialogFragment = new CheckLogout();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager,"checkLogout");
                break;
            case R.id.opcion_premios:
                drawer.closeDrawer(GravityCompat.START);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPremios(){

        progressDialog = new ProgressDialog(Premios.this);
        progressDialog.setMessage("Cargando Premios");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Query query = db.collection("premios");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Listener Premios", "Listen failed.", e);
                    return;
                }
                if (value != null && !value.isEmpty()){
                    premios = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        Map<String, Object> premioDb = document.getData();
                        premios.add(new Premio(
                                document.getId(),
                                (String)premioDb.get("nombre"),
                                (String)premioDb.get("urlImagen"),
                                ((Long)premioDb.get("costeCoins")).intValue())
                        );
                    }
                    premios.sort(Comparator.comparing(Premio::getCosteCoins).reversed());

                    adapterGridPremios = new AdapterGridPremios(Premios.this, premios);
                    gridViewPremios.setAdapter(adapterGridPremios);
                    gridViewPremios.setOnItemClickListener(Premios.this);

                    progressDialog.dismiss();

                    Log.d("Listener Premios", "Eventos actuales en: " + premios);
                }
                else{

                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Premio item = (Premio) parent.getItemAtPosition(position);

        if(coinsUsuario >= item.getCosteCoins()){
            DialogFormEnviarPremio dialogFragment = new DialogFormEnviarPremio();
            Bundle bundle = new Bundle();
            bundle.putString("idPremio",item.getId());
            bundle.putString("nombrePremio",item.getNombre());
            bundle.putInt("costeCoins",item.getCosteCoins());
            dialogFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            dialogFragment.show(fragmentManager,"formEnviarPremio");
        }else{
            //error
            showSnackBarLong("No tienes suficientes coins para ese premio", getWindow().getDecorView().findViewById(android.R.id.content));
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
        startActivity(Premios.this,Ajustes.class);
        finish();
    }
}