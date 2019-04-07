package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
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
import android.widget.Toast;

import com.example.freesbet.bases.CustomAdpaterSpinner;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.widgets.CheckLogout;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Supremacia extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.spinner_supremacia)
    Spinner mSpinerPaises;
    private String [] mNombresSpinner;
    private String [] mUrlsImagenesSpinner;
    CustomAdpaterSpinner adpaterSpinner;

    @BindView(R.id.recyclerView_eventos_supremacia)
    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<EventoLista> eventos = new ArrayList<>();
    RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supremacia);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getText(R.string.title_activity_supremacia));

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

        inicializarSpinner();

        getEventos();
        initializeRecyclerView();

        mSpinerPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),mNombresSpinner[position],Toast.LENGTH_LONG).show();

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
                Intent intent1= new Intent(Supremacia.this,Home.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.opcion_menu_fms:
                Intent intent2= new Intent(Supremacia.this,Fms.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.opcion_menu_redbull:
                Intent intent3= new Intent(Supremacia.this, Redbull.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.opcion_menu_bdm:
                Intent intent4= new Intent(Supremacia.this,Bdm.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.opcion_supremaciamc:
                Intent intent5= new Intent(Supremacia.this,Supremacia.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.opcion_ivita_amigos:
                Intent intent6= new Intent(Supremacia.this,Invita.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.opcion_ajustes_cuenta:
                Intent intent7= new Intent(Supremacia.this,Ajustes.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.opcion_logout:
                CheckLogout dialogFragment = new CheckLogout();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager,"checkLogout");
                break;
            case R.id.opcion_premios:
                Intent intent8= new Intent(Supremacia.this,Premios.class);
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
        mNombresSpinner = new String[]{"Todos","España", "Argentina", "Mexico", "Chile"};
        mUrlsImagenesSpinner = new String[]{"https://www.materialui.co/materialIcons/places/all_inclusive_black_192x192.png",
                "http://flags.fmcdn.net/data/flags/w580/es.png",
                "https://cdn.pixabay.com/photo/2013/07/13/14/14/argentina-162229_960_720.png",
                "http://flags.fmcdn.net/data/flags/w580/mx.png",
                "http://flags.fmcdn.net/data/flags/w580/cl.png"};

        adpaterSpinner = new CustomAdpaterSpinner(this,mNombresSpinner,mUrlsImagenesSpinner);
        mSpinerPaises.setAdapter(adpaterSpinner);

    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(Supremacia.this);
        rv.setLayoutManager(llm);
    }

    private void getEventos() {
        Supremacia.MyAsyncTasks myAsyncTasks = new Supremacia.MyAsyncTasks();
        myAsyncTasks.execute();
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Supremacia.this);
            progressDialog.setMessage("Cargando eventos");
            progressDialog.setCancelable(false);
            progressDialog.show();
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

                eventos = new ArrayList<>();
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1102595661034385409/wkdgl8ok.png","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1098695785976381441/KbUn_B7T_400x400.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1101153365428457477/xFoTQVHK_400x400.png","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));


            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            // Cargar eventos en lista
            adapter = new RVAdapter(eventos, Supremacia.this);
            rv.setAdapter(adapter);

        }
    }
}
