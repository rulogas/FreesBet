package com.example.freesbet;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.freesbet.bases.Actividad;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.bases.RVAdapterActividad;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ActividadFragment extends Fragment {

    RecyclerView rv;
    ProgressDialog progressDialog;
    private List<Actividad> actividades = new ArrayList<>();
    RVAdapterActividad adapter;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividad,container,false);


        // setear imagen fondo con glide para aumentar rendimiento

        Glide.with(this).load(R.drawable.fondo_login).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });

        rv =view.findViewById(R.id.recyclerView_actividad);
        getActividad();
        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    private void getActividad(){
        MyAsyncTasksGetActividad myAsyncTasksGetActividad = new MyAsyncTasksGetActividad();
        myAsyncTasksGetActividad.execute();
    }

    public class MyAsyncTasksGetActividad extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Cargando actividad");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String current = "";
            try {



                actividades.add(new Actividad(1,"Red Bull España - Nacional", "ganado", "competicion"));
                actividades.add(new Actividad(2,"Chuty vs Walls", "perdido", "liga"));
                actividades.add(new Actividad(-1,"Red Bull España - Nacional - Regional Cádiz", "perdido", "competicion"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));
                actividades.add(new Actividad(-1,"Subida de nivel", "ganado", "bonus"));


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
            adapter = new RVAdapterActividad(actividades,getContext());
            rv.setAdapter(adapter);

        }
    }

}