package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.freesbet.bases.BaseActivity.nombreUsuario;
import static com.example.freesbet.bases.BaseActivity.photoUrlUsuario;

public class HomeProximosFragment extends Fragment {

    NavigationView navigationView;
    View headerView;
    TextView textViewNombreUsuarioHeaderMenu;
    CircleImageView circleImageViewUsuarioMenu;

    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<EventoLista> eventos = new ArrayList<>();
    RVAdapter adapter;

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rv = getActivity().findViewById(R.id.recyclerView_eventos_populares);
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_proximos,container,false);
        ButterKnife.bind(getActivity());

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        // setear info usuario

        headerView = navigationView.getHeaderView(0);
        textViewNombreUsuarioHeaderMenu = headerView.findViewById(R.id.textView_nombreUsuario_headerMenu);
        circleImageViewUsuarioMenu = headerView.findViewById(R.id.circleview_header_perfil_usuario);

        circleImageViewUsuarioMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irPerfil();
            }
        });

        //cargarInfoUsuarioMenu();

        rv =view.findViewById(R.id.recyclerView_eventos_proximos);
        getEventos();
        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(llm);
    }

    private void getEventos() {
        HomeProximosFragment.MyAsyncTasks myAsyncTasks = new HomeProximosFragment.MyAsyncTasks();
        myAsyncTasks.execute();
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
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
                eventos.add(new EventoLista(1,"Papo vs Trueno", "FMS - Argentina", "https://pbs.twimg.com/profile_images/1102595661034385409/wkdgl8ok.png","27/04/2019",500));
                eventos.add(new EventoLista(1,"Azcino vs RC", "FMS - Mexico", "https://pbs.twimg.com/profile_images/1098695785976381441/KbUn_B7T_400x400.jpg","27/04/2019",500));
                eventos.add(new EventoLista(1,"Nitro vs Kaiser", "FMS - Chile", "https://pbs.twimg.com/profile_images/1101153365428457477/xFoTQVHK_400x400.png","27/04/2019",500));
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
            adapter = new RVAdapter(eventos, getContext());
            rv.setAdapter(adapter);

            textViewNombreUsuarioHeaderMenu.setText(nombreUsuario);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Glide.with(AppFreesBet.mContext).load(photoUrlUsuario).into(circleImageViewUsuarioMenu);
                }
            }, 5000);
            progressDialog.dismiss();

        }
    }

    /*private void cargarInfoUsuarioMenu(){

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando eventos");
        progressDialog.setCancelable(false);
        progressDialog.show();



        // Esperar un segundo para cargar imagen de menu


    }*/

    private void irPerfil(){
        Intent in = new Intent(getActivity(),Ajustes.class);
        startActivity(in);
        getActivity().finish();
    }

}
