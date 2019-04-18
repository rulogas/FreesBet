package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.RVAdapter;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class PerfilFragment extends Fragment {

    View view;

    CircleImageView circleImageViewUsuario;
    TextView textViewNombreUsuario;
    TextView textViewNivelUsuario;

    TextView textViewNumBanca;
    TextView textViewPorcentajeJuego;

    ProgressBar progressBarNivelExperiencia;
    TextView textViewNivelActualExperiencia;
    TextView textViewNivelObjetivoExperiencia;

    ImageView imageViewRacha1;
    ImageView imageViewRacha2;
    ImageView imageViewRacha3;
    ImageView imageViewRacha4;
    ImageView imageViewRacha5;

    TextView textViewTextoEventoFavorito1;
    TextView textViewPorcentajeEventoFavorito1;
    ProgressBar progressBarEventoFavorito1;
    TextView textViewTextoEventoFavorito2;
    TextView textViewPorcentajeEventoFavorito2;
    ProgressBar progressBarEventoFavorito2;
    TextView textViewTextoEventoFavorito3;
    TextView textViewPorcentajeEventoFavorito3;
    ProgressBar progressBarEventoFavorito3;
    TextView textViewTextoEventoFavorito4;
    TextView textViewPorcentajeEventoFavorito4;
    ProgressBar progressBarEventoFavorito4;

    ProgressDialog progressDialog;

    View headerView;



    private static final int PICK_IMAGE = 1;
    Uri imageUri;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil,container,false);

        // setear imagen header con glide para aumentar rendimiento
        ImageView imageViewHeaderperfil = view.findViewById(R.id.imageView_header_perfil);
        Glide.with(this).load(R.drawable.fondo_header).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageViewHeaderperfil.setBackground(drawable);
                }
            }
        });

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        inicializarPerfil();
        getDatosUsuario();

        circleImageViewUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return view;
    }

    private void inicializarPerfil(){
        circleImageViewUsuario = view.findViewById(R.id.circleview_perfil_usuario);
        textViewNombreUsuario = view.findViewById(R.id.textView_nombreUsuario);
        textViewNivelUsuario = view.findViewById(R.id.textView_ajustes_nivel);

        textViewNumBanca = view.findViewById(R.id.textView_numBanca);
        textViewPorcentajeJuego = view.findViewById(R.id.textView_porcentaje_en_juego);
        progressBarNivelExperiencia = view.findViewById(R.id.progressBar_experiencia);
        textViewNivelActualExperiencia = view.findViewById(R.id.textView_nivel_actual);
        textViewNivelObjetivoExperiencia = view.findViewById(R.id.textView_nivel_objetivo);

        imageViewRacha1 = view.findViewById(R.id.imageView_racha1);
        imageViewRacha2 = view.findViewById(R.id.imageView_racha2);
        imageViewRacha3 = view.findViewById(R.id.imageView_racha3);
        imageViewRacha4 = view.findViewById(R.id.imageView_racha4);
        imageViewRacha5 = view.findViewById(R.id.imageView_racha5);

        textViewTextoEventoFavorito1 = view.findViewById(R.id.textView_texto_evento_favorito1);
        textViewPorcentajeEventoFavorito1 = view.findViewById(R.id.textView_porcentaje_evento_favorito1);
        progressBarEventoFavorito1 = view.findViewById(R.id.progressBar_evento_favorito1);
        textViewTextoEventoFavorito2 = view.findViewById(R.id.textView_texto_evento_favorito2);
        textViewPorcentajeEventoFavorito2 = view.findViewById(R.id.textView_porcentaje_evento_favorito2);
        progressBarEventoFavorito2 = view.findViewById(R.id.progressBar_evento_favorito2);
        textViewTextoEventoFavorito3 = view.findViewById(R.id.textView_texto_evento_favorito3);
        textViewPorcentajeEventoFavorito3 = view.findViewById(R.id.textView_porcentaje_evento_favorito3);
        progressBarEventoFavorito3 = view.findViewById(R.id.progressBar_evento_favorito3);
        textViewTextoEventoFavorito4 = view.findViewById(R.id.textView_texto_evento_favorito4);
        textViewPorcentajeEventoFavorito4 = view.findViewById(R.id.textView_porcentaje_evento_favorito4);
        progressBarEventoFavorito4 = view.findViewById(R.id.progressBar_evento_favorito4);
    }

    private void getDatosUsuario(){
        MyAsyncTasksGetDatosUsuario myAsyncTasksGetDatosUsuario = new MyAsyncTasksGetDatosUsuario();
        myAsyncTasksGetDatosUsuario.execute();
    }

    public class MyAsyncTasksGetDatosUsuario extends AsyncTask<String, String, String> {

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

            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            //setear imagen usuario

            //setear nombre usuario y nivel

            //setear banca y porcentaje en juego

            //setear progress experiencia maxima y experiencia actual, nivel actual y siguiente nivel

            // setear imagenes racha dependiendo de campo resultado, limitado a 5

            // setear porcentajes ordenados con zona de evento

        }
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            circleImageViewUsuario.setImageURI(imageUri);

            CircleImageView circleImageView =  headerView.findViewById(R.id.circleview_header_perfil_usuario);
            circleImageView.setImageURI(imageUri);

            // actualizar imagen perfil firebase
        }
    }
}
