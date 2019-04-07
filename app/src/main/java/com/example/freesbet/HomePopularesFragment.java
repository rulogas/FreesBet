package com.example.freesbet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.freesbet.bases.Evento;
import com.example.freesbet.bases.RVAdapter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePopularesFragment extends Fragment {

    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<Evento> eventos = new ArrayList<>();
    RVAdapter adapter;

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rv = getActivity().findViewById(R.id.recyclerView_eventos_populares);
    }*/


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_populares,container,false);
        ButterKnife.bind(getActivity());

        rv =view.findViewById(R.id.recyclerView_eventos_populares);
        getEventos();
        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    private void getEventos() {
        HomePopularesFragment.MyAsyncTasks myAsyncTasks = new HomePopularesFragment.MyAsyncTasks();
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
                eventos.add(new Evento(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new Evento(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1102595661034385409/wkdgl8ok.png","27/04/2019",500));
                eventos.add(new Evento(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1098695785976381441/KbUn_B7T_400x400.jpg","27/04/2019",500));
                eventos.add(new Evento(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1101153365428457477/xFoTQVHK_400x400.png","27/04/2019",500));
                eventos.add(new Evento(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new Evento(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));
                eventos.add(new Evento(1,"Chuty vs Walls", "FMS - España", "https://pbs.twimg.com/profile_images/1077019493883432960/YD_xeCQW.jpg","27/04/2019",500));


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

        }
    }
}
