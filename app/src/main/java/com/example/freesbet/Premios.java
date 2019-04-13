package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.freesbet.bases.AdapterGridPremios;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.Premio;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.widgets.CheckLogout;
import com.example.freesbet.widgets.DialogFormEnviarPremio;
import com.example.freesbet.widgets.General;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Premios extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private List<Premio> premios;
    ProgressDialog progressDialog;
    AdapterGridPremios adapterGridPremios;
    @BindView(R.id.gridView_premios)
    GridView gridViewPremios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

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

        getPremios();


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
        if (id == R.id.action_settings) {
            return true;
        }

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
        MyAsyncTasksGetPremios myAsyncTasksGetPremios = new MyAsyncTasksGetPremios();
        myAsyncTasksGetPremios.execute();
    }

    public class MyAsyncTasksGetPremios extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Premios.this);
            progressDialog.setMessage("Cargando Premios");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String current = "";

                // obtener premios ordenados por coins

            premios = new ArrayList<>();

            premios.add(new Premio("1","Xiaomi Pocophone F1","https://images.playfulbet.com/system/uploads/prize/prize_image/325/41QXUJaM_jL.jpg",11500000));
            premios.add(new Premio("2","PS4 Slim","https://images.playfulbet.com/system/uploads/prize/prize_image/225/prize_ps4slim.png",9900000));
            premios.add(new Premio("3","BenQ XL2411Z 144Hz","https://images.playfulbet.com/system/uploads/prize/prize_image/168/prz_monitor_benq.png",7400000));
            premios.add(new Premio("4","Xiaomi MI A2","https://images.playfulbet.com/system/uploads/prize/prize_image/324/Xiaomi-mi-A2-black-560x560.jpg",6000000));
            premios.add(new Premio("5","Apple Airpods","https://images.playfulbet.com/system/uploads/prize/prize_image/299/51o9usvz11L._SL1000_.jpg",4300000));
            premios.add(new Premio("7","Sudadera Oficial Red Bull Batalla de los Gallos","https://images.redbullshop.com/is/image/RedBullSalzburg/RB-product-detail/BDG18014_9A_1/Batalla-College-Jacket.jpg",3500000));
            premios.add(new Premio("6","Gorra Oficial Urban Roosters","https://i2.wp.com/blog.urbanroosters.com/wp-content/uploads/2018/04/87-large_default.jpg",10000000));
            premios.add(new Premio("8","Entradas - Proxima joranda FMS España","http://www.tedxupvalencia.com/wp-content/uploads/2015/10/ticket.png",500000));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            // Cargar premios en grid
            adapterGridPremios = new AdapterGridPremios(Premios.this, premios);
            gridViewPremios.setAdapter(adapterGridPremios);
            gridViewPremios.setOnItemClickListener(Premios.this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /*if(coins >= coste){

        }else{
            //error
        }*/
        Premio item = (Premio) parent.getItemAtPosition(position);
        DialogFormEnviarPremio dialogFragment = new DialogFormEnviarPremio();
        FragmentManager fragmentManager = getSupportFragmentManager();

        dialogFragment.show(fragmentManager,"formEnviarPremio");
    }
}