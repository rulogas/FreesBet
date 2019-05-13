package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.freesbet.bases.BooVariable;
import com.example.freesbet.widgets.ProgressBarAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.freesbet.bases.BaseActivity.guardarImagenUsuario;
import static com.example.freesbet.bases.BaseActivity.datosUsuarioActualizados;
import static com.example.freesbet.bases.BaseActivity.idUsuario;
import static com.example.freesbet.bases.BaseActivity.nombreUsuario;
import static com.example.freesbet.bases.BaseActivity.photoUrlUsuario;


public class PerfilFragment extends Fragment {

    NavigationView navigationView;
    View headerView;
    CircleImageView circleImageViewMenuUsuario;

    View view;

    CircleImageView circleImageViewUsuario;
    ImageView imageViewEditar;
    TextView textViewNombreUsuario;
    TextView textViewNivelUsuario;

    TextView textViewNumBanca;
    TextView textViewPorcentajeJuego;

    ProgressBar progressBarNivelExperiencia;
    TextView textViewNivelActualExperiencia;
    TextView textViewNivelObjetivoExperiencia;

    TextView textViewRacha;
    ImageView imageViewRacha1;
    ImageView imageViewRacha2;
    ImageView imageViewRacha3;
    ImageView imageViewRacha4;
    ImageView imageViewRacha5;

    TextView textViewEventosFavoritos;
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

    private static final int PICK_IMAGE = 1;
    Uri imageUri;

    FirebaseFirestore db;

    // getRacha
    int indice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // setear imagen header con glide para aumentar rendimiento
        ImageView imageViewHeaderperfil = view.findViewById(R.id.imageView_header_perfil);
        Glide.with(this).load(R.drawable.fondo_login).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageViewHeaderperfil.setBackground(drawable);
                }
            }
        });

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

        inicializarPerfil();
        getDatosUsuario();


        circleImageViewUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        datosUsuarioActualizados.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (datosUsuarioActualizados.isBoo()) {
                    Glide.with(AppFreesBet.mContext).load(photoUrlUsuario).into(circleImageViewUsuario);

                    Glide.with(AppFreesBet.mContext).load(photoUrlUsuario).into(circleImageViewMenuUsuario);

                    progressDialog.dismiss();
                }
            }
        });

        return view;
    }

    private void inicializarPerfil() {
        navigationView = getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        circleImageViewMenuUsuario = headerView.findViewById(R.id.circleview_header_perfil_usuario);
        circleImageViewUsuario = view.findViewById(R.id.circleview_perfil_usuario);
        imageViewEditar = view.findViewById(R.id.imageView_editar);
        textViewNombreUsuario = view.findViewById(R.id.textView_nombreUsuario);
        textViewNivelUsuario = view.findViewById(R.id.textView_ajustes_nivel);

        textViewNumBanca = view.findViewById(R.id.textView_numBanca);
        textViewPorcentajeJuego = view.findViewById(R.id.textView_porcentaje_en_juego);
        progressBarNivelExperiencia = view.findViewById(R.id.progressBar_experiencia);
        textViewNivelActualExperiencia = view.findViewById(R.id.textView_nivel_actual);
        textViewNivelObjetivoExperiencia = view.findViewById(R.id.textView_nivel_objetivo);

        textViewRacha = view.findViewById(R.id.textView_racha);
        imageViewRacha1 = view.findViewById(R.id.imageView_racha1);
        imageViewRacha2 = view.findViewById(R.id.imageView_racha2);
        imageViewRacha3 = view.findViewById(R.id.imageView_racha3);
        imageViewRacha4 = view.findViewById(R.id.imageView_racha4);
        imageViewRacha5 = view.findViewById(R.id.imageView_racha5);

        textViewEventosFavoritos = view.findViewById(R.id.textView_eventos_favoritos);
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

    private void getDatosUsuario() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando eventos");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Glide.with(getContext()).load(photoUrlUsuario).into(circleImageViewUsuario);

        //setear nombre usuario
        textViewNombreUsuario.setText(nombreUsuario);

        DocumentReference docRefUsuario = db.collection("usuarios").document(idUsuario);
        docRefUsuario.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Log.d("Usuario", "DocumentSnapshot data: " + documentSnapshot.getData());
                    Map<String, Object> usuarioDb = documentSnapshot.getData();

                    // setear nivel
                    int nivel = ((Long) usuarioDb.get("nivel")).intValue();
                    textViewNivelUsuario.setText("Nivel " + nivel);

                    //setear banca y numeroApuestas en juego
                    int coins = ((Long) usuarioDb.get("coins")).intValue();

                    List<Map<String, Object>> listaActividadesDb = (List<Map<String, Object>>) usuarioDb.get("actividades");
                    getBancaYPorcentajeEnJuego(listaActividadesDb, coins);

                    //setear progress experiencia maxima y experiencia actual, nivel actual y siguiente nivel
                    int experiencia = ((Long) usuarioDb.get("experiencia")).intValue();
                    int experienciaSiguienteNivel = ((Long) usuarioDb.get("experienciaSiguienteNivel")).intValue();
                    progressBarNivelExperiencia.setMax(experienciaSiguienteNivel);
                    ProgressBarAnimation anim = new ProgressBarAnimation(progressBarNivelExperiencia, 0, experiencia);
                    anim.setDuration(1000);
                    progressBarNivelExperiencia.startAnimation(anim);
                    textViewNivelActualExperiencia.setText(String.valueOf(nivel));
                    textViewNivelObjetivoExperiencia.setText(String.valueOf(nivel + 1));

                    // setear imagenes racha dependiendo de campo resultado, limitado a 5
                    getRacha();

                    // setear porcentajes ordenados con zona de evento
                    getEventosFavoritos();



                } else {
                    Log.d("Usuario", "No such document");
                }
            }
        });


    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            /*progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Guardando imagen");
            progressDialog.setCancelable(false);
            progressDialog.show();*/

            datosUsuarioActualizados.setBoo(false);
            guardarImagenUsuario(imageUri);


            // actualizar imagen perfil firebase
        }
    }

    private void getBancaYPorcentajeEnJuego(List<Map<String, Object>> listaActividadesDb, int coins) {

        Query query = db.collection("eventos")
                .whereEqualTo("finalizado", false);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.isEmpty() || querySnapshot == null) {
                        textViewNumBanca.setText("No hay eventos en los que apostar");
                    } else {
                        int coinsSumados = 0;
                        int banca = coins;
                        int porcentaje = 0;
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Map<String, Object> eventoDb = document.getData();
                            List<Map<String, Object>> listaApuestasDb = (List<Map<String, Object>>) eventoDb.get("apuestas");
                            for (Map<String, Object> apuesta : listaApuestasDb) {
                                if (((String) apuesta.get("idUsuario")).equalsIgnoreCase(idUsuario)) {
                                    coinsSumados = coinsSumados + ((Long) apuesta.get("coins")).intValue();
                                }
                            }
                        }
                        if (coinsSumados != 0) {
                            banca = coinsSumados + banca;
                            porcentaje = (100 * coinsSumados) / banca;
                        }
                        textViewNumBanca.setText(String.valueOf(banca));
                        textViewPorcentajeJuego.setText("(" + porcentaje + "% en juego)");
                    }
                }
            }
        });
    }

    private void getRacha() {

        Query query = db.collection("eventos")
                .whereEqualTo("finalizado", true);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.isEmpty() || querySnapshot == null) {
                        textViewRacha.setText("No hay eventos finalizados");
                    } else {
                        List<String> listaResultados = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Map<String, Object> eventoDb = document.getData();
                            List<Map<String, Object>> listaApuestasDb = (List<Map<String, Object>>) eventoDb.get("apuestas");
                            for (Map<String, Object> apuesta : listaApuestasDb) {
                                if (((String) apuesta.get("idUsuario")).equalsIgnoreCase(idUsuario)) {
                                    if (((String) apuesta.get("elección")).equalsIgnoreCase((String) eventoDb.get("ganador"))) {
                                        listaResultados.add("ganado");
                                    } else {
                                        listaResultados.add("perdido");
                                    }
                                }
                            }
                        }

                        if (listaResultados.isEmpty()){
                            textViewRacha.setText("Racha: No has apostado todavía o no ha finalizado ningún evento de los que has apostado.");
                        }else{
                            textViewRacha.setText("Racha:");
                            for (int i=0;i<listaResultados.size();i++){
                                switch (i){
                                    case 0:
                                        if (listaResultados.get(i).equalsIgnoreCase("ganado")){
                                            Glide.with(getContext()).load(R.drawable.ganada).into(imageViewRacha1);
                                        }else{
                                            Glide.with(getContext()).load(R.drawable.perdida).into(imageViewRacha1);
                                        }
                                        break;
                                    case 1:
                                        if (listaResultados.get(i).equalsIgnoreCase("ganado")){
                                            Glide.with(getContext()).load(R.drawable.ganada).into(imageViewRacha2);
                                        }else{
                                            Glide.with(getContext()).load(R.drawable.perdida).into(imageViewRacha2);
                                        }
                                        break;
                                    case 2:
                                        if (listaResultados.get(i).equalsIgnoreCase("ganado")){
                                            Glide.with(getContext()).load(R.drawable.ganada).into(imageViewRacha3);
                                        }else{
                                            Glide.with(getContext()).load(R.drawable.perdida).into(imageViewRacha3);
                                        }
                                        break;
                                    case 3:
                                        if (listaResultados.get(i).equalsIgnoreCase("ganado")){
                                            Glide.with(getContext()).load(R.drawable.ganada).into(imageViewRacha4);
                                        }else{
                                            Glide.with(getContext()).load(R.drawable.perdida).into(imageViewRacha4);
                                        }
                                        break;
                                    case 4:
                                        if (listaResultados.get(i).equalsIgnoreCase("ganado")){
                                            Glide.with(getContext()).load(R.drawable.ganada).into(imageViewRacha5);
                                        }else{
                                            Glide.with(getContext()).load(R.drawable.perdida).into(imageViewRacha5);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void getEventosFavoritos(){
        Query query = db.collection("eventos");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.isEmpty() || querySnapshot == null) {
                        textViewEventosFavoritos.setText("No hay eventos hay eventos");
                    } else {
                        List<PorcentajeFavorito> listaPorcentajesFavoritos = new ArrayList<>();
                        listaPorcentajesFavoritos.add(new PorcentajeFavorito("FMS",0));
                        listaPorcentajesFavoritos.add(new PorcentajeFavorito("Red Bull",0));
                        listaPorcentajesFavoritos.add(new PorcentajeFavorito("BDM",0));
                        listaPorcentajesFavoritos.add(new PorcentajeFavorito("Supremacía MC",0));
                        int numeroTotalApuestas = 0;
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Map<String, Object> eventoDb = document.getData();
                            List<Map<String, Object>> listaApuestasDb = (List<Map<String, Object>>) eventoDb.get("apuestas");
                            for (Map<String, Object> apuesta : listaApuestasDb) {
                                if (((String) apuesta.get("idUsuario")).equalsIgnoreCase(idUsuario)) {
                                    for (PorcentajeFavorito porcentajeFavorito : listaPorcentajesFavoritos){
                                        if (((String)eventoDb.get("evento")).equalsIgnoreCase(porcentajeFavorito.evento)){
                                            porcentajeFavorito.setNumeroApuestas(porcentajeFavorito.numeroApuestas +1);
                                            break;
                                        }
                                    }
                                    numeroTotalApuestas++;
                                }
                            }
                        }
                        if (numeroTotalApuestas != 0){
                            textViewPorcentajeEventoFavorito1.setText(String.valueOf((100 * listaPorcentajesFavoritos.get(0).numeroApuestas)/numeroTotalApuestas)+"%");
                            progressBarEventoFavorito1.setMax(numeroTotalApuestas*100);
                            ProgressBarAnimation anim = new ProgressBarAnimation(progressBarEventoFavorito1, 0, listaPorcentajesFavoritos.get(0).numeroApuestas*100);
                            anim.setDuration(1000);
                            progressBarEventoFavorito1.startAnimation(anim);

                            //progressBarEventoFavorito1.setProgress(listaPorcentajesFavoritos.get(0).numeroApuestas);

                            textViewPorcentajeEventoFavorito2.setText(String.valueOf((100 * listaPorcentajesFavoritos.get(1).numeroApuestas)/numeroTotalApuestas)+"%");
                            progressBarEventoFavorito2.setMax(numeroTotalApuestas*100);
                            ProgressBarAnimation anim2 = new ProgressBarAnimation(progressBarEventoFavorito2, 0, listaPorcentajesFavoritos.get(1).numeroApuestas*100);
                            anim2.setDuration(1000);
                            progressBarEventoFavorito2.startAnimation(anim2);

                            //progressBarEventoFavorito2.setProgress(listaPorcentajesFavoritos.get(1).numeroApuestas);

                            textViewPorcentajeEventoFavorito3.setText(String.valueOf((100 * listaPorcentajesFavoritos.get(2).numeroApuestas)/numeroTotalApuestas)+"%");
                            progressBarEventoFavorito3.setMax(numeroTotalApuestas*100);
                            ProgressBarAnimation anim3 = new ProgressBarAnimation(progressBarEventoFavorito3, 0, listaPorcentajesFavoritos.get(2).numeroApuestas*100);
                            anim3.setDuration(1000);
                            progressBarEventoFavorito3.startAnimation(anim3);
                            //progressBarEventoFavorito3.setProgress(listaPorcentajesFavoritos.get(2).numeroApuestas);

                            textViewPorcentajeEventoFavorito4.setText(String.valueOf((100 * listaPorcentajesFavoritos.get(3).numeroApuestas)/numeroTotalApuestas)+"%");
                            progressBarEventoFavorito4.setMax(numeroTotalApuestas*100);
                            ProgressBarAnimation anim4 = new ProgressBarAnimation(progressBarEventoFavorito4, 0, listaPorcentajesFavoritos.get(3).numeroApuestas*100);
                            anim4.setDuration(1000);
                            progressBarEventoFavorito4.startAnimation(anim4);
                            //progressBarEventoFavorito4.setProgress(listaPorcentajesFavoritos.get(3).numeroApuestas);
                            progressDialog.dismiss();
                        }
                    }
                }
            }
        });
    }

    public class PorcentajeFavorito{
        String evento;
        int numeroApuestas;

        public PorcentajeFavorito(String evento, int numeroApuestas) {
            this.evento = evento;
            this.numeroApuestas = numeroApuestas;
        }

        public String getEvento() {
            return evento;
        }

        public int getNumeroApuestas() {
            return numeroApuestas;
        }

        public void setEvento(String evento) {
            this.evento = evento;
        }

        public void setNumeroApuestas(int numeroApuestas) {
            this.numeroApuestas = numeroApuestas;
        }
    }
}
