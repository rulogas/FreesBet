package com.example.freesbet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.widgets.General;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.freesbet.bases.BaseActivity.coinsUsuario;
import static com.example.freesbet.bases.BaseActivity.idUsuario;
import static com.example.freesbet.bases.BaseActivity.nombreUsuario;


public class BottomSheetDialogFragmentApuestaLiga extends BottomSheetDialogFragment {
    String cuota;
    String puntosUsuario;
    String competidor;
    int ganancia;
    String idEvento;
    boolean validate ;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    public BottomSheetDialogFragmentApuestaLiga(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cuota = getArguments().getString("cuota");
        puntosUsuario = String.valueOf(coinsUsuario);
        competidor = getArguments().getString("competidor");
        idEvento = getArguments().getString("idEvento");
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_apuesta_liga, container, false);
        TextView textViewCompetidor = v.findViewById(R.id.textView_competidor);
        TextView textoCuota = v.findViewById(R.id.textView_cuota);
        TextView textoGanancia = v.findViewById(R.id.textView_ganancia);
        EditText campoCantidad = v.findViewById(R.id.editText_cantidad);
        SeekBar seekBar = v.findViewById(R.id.seekBar);
        AppCompatButton botonMin = v.findViewById(R.id.button_min);
        AppCompatButton botonMax = v.findViewById(R.id.button_max);
        AppCompatButton botonJugar = v.findViewById(R.id.boton_jugar);
        textViewCompetidor.setText(competidor);
        textoCuota.setText(cuota);
        // calcular ganancia potencial
        int cantidad = Integer.parseInt(campoCantidad.getText().toString());
        ganancia =(int)(((double)cantidad * Double.parseDouble(cuota))-(double)cantidad);
        textoGanancia.setText("+"+Integer.toString(+ganancia));

        campoCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()){

                    int cantidad = Integer.parseInt(campoCantidad.getText().toString());
                    ganancia =(int)(((double)cantidad * Double.parseDouble(cuota))-(double)cantidad);
                    textoGanancia.setText("+"+Integer.toString(+ganancia));
                    seekBar.setProgress(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        seekBar.setMax(Integer.parseInt(puntosUsuario));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(200);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                campoCantidad.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        botonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(200);

            }
        });

        botonMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(Integer.parseInt(puntosUsuario));

            }
        });

        botonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate = true;
                if (campoCantidad.getText().toString().isEmpty()){
                    General dialogFragment = new General();
                    Bundle mensaje = new Bundle();
                    mensaje.putString("titulo","Error");
                    mensaje.putString("mensaje","Debes introducir una cantidad.");
                    dialogFragment.setArguments(mensaje);
                    FragmentManager fragmentManager = getFragmentManager();
                    dialogFragment.show(fragmentManager,"general");
                    campoCantidad.setText(Integer.toString(200));
                    validate = false;
                }
                if (Integer.parseInt(campoCantidad.getText().toString())<200){
                    General dialogFragment = new General();
                    Bundle mensaje = new Bundle();
                    mensaje.putString("titulo","Error");
                    mensaje.putString("mensaje","Debes apostar como mínimo 200 coins ¡Arriésgate!");
                    dialogFragment.setArguments(mensaje);
                    FragmentManager fragmentManager = getFragmentManager();
                    dialogFragment.show(fragmentManager,"general");
                    campoCantidad.setText(Integer.toString(200));
                    validate = false;
                }
                if (Integer.parseInt(campoCantidad.getText().toString())>Integer.parseInt(puntosUsuario)){
                    General dialogFragment = new General();
                    Bundle mensaje = new Bundle();
                    mensaje.putString("titulo","Error");
                    mensaje.putString("mensaje","Tus coins actuales son "+puntosUsuario+", no puedes apostar más.");
                    dialogFragment.setArguments(mensaje);
                    FragmentManager fragmentManager = getFragmentManager();
                    dialogFragment.show(fragmentManager,"general");
                    campoCantidad.setText(puntosUsuario);
                    validate = false;
                }
                if (validate){
                    // realizar envío
                    anadirApuesta(Integer.parseInt(campoCantidad.getText().toString()));
                }
            }
        });

        return v;
    }

    private void anadirApuesta(int cantidad){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRefEvento = db.collection("eventos").document(idEvento);
        DocumentReference docRefUsuario = db.collection("usuarios").document(idUsuario);
        docRefEvento.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Realizando apuesta");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        Map<String, Object> eventoDb = document.getData();
                        int numeroApuestas = ((Long)eventoDb.get("numeroApuestas")).intValue();
                        numeroApuestas++;
                        List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas") ;

                        Map<String,Object> apuesta = new HashMap<>();
                        apuesta.put("coins",cantidad);
                        apuesta.put("elección",competidor);
                        apuesta.put("gananciaPotencial",ganancia);
                        apuesta.put("idUsuario",idUsuario);
                        apuesta.put("nombreUsuario",nombreUsuario);
                        apuesta.put("fechaApuesta",new Date());
                        listaApuestasDb.add(apuesta);

                        docRefEvento.update(
                                "apuestas", listaApuestasDb,
                                "numeroApuestas",numeroApuestas);
                    } else {
                        Log.d("EVENTO", "No such document");
                    }
                } else {
                    Log.d("EVENTO", "get failed with ", task.getException());
                }
            }
        });


        docRefUsuario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> usuarioDb = document.getData();

                    int coins = ((Long)usuarioDb.get("coins")).intValue();
                    int coinsFinales = coins-cantidad;

                    List<Map<String,Object>> listaActividadesDb = (List<Map<String,Object>>)usuarioDb.get("actividades") ;
                    Map<String,Object> actividad = new HashMap<>();
                    actividad.put("coins",cantidad);
                    actividad.put("elección",competidor);
                    actividad.put("gananciaPotencial",ganancia);
                    actividad.put("idEvento",idEvento);
                    actividad.put("fechaApuesta",new Date());
                    listaActividadesDb.add(actividad);

                    int experiencia = ((Long)usuarioDb.get("experiencia")).intValue();
                    experiencia = experiencia+200;

                    docRefUsuario.update(
                            "actividades", listaActividadesDb,
                            "coins",coinsFinales,
                            "experiencia",experiencia).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AppFreesBet.mContext,"¡Has ganado 200 puntos de experiencia!",Toast.LENGTH_LONG).show();
                                comprobarNivel(cantidad);
                            }
                        }
                    });

                    progressDialog.dismiss();
                    dismiss();

                    if (document.exists()) {
                        Log.d("EVENTO", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("EVENTO", "No such document");
                    }
                } else {
                    Log.d("EVENTO", "get failed with ", task.getException());
                }
            }
        });
    }

    private void comprobarNivel(int cantidad){
        DocumentReference docRefUsuario = db.collection("usuarios").document(idUsuario);
        docRefUsuario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Map<String, Object> usuarioDb = document.getData();
                        int experiencia = ((Long)usuarioDb.get("experiencia")).intValue();
                        int experienciaSiguienteNivel = ((Long)usuarioDb.get("experienciaSiguienteNivel")).intValue();
                        int nivel = ((Long)usuarioDb.get("nivel")).intValue();
                        int coins = ((Long)usuarioDb.get("coins")).intValue();
                        if (experiencia == experienciaSiguienteNivel){
                            nivel++;
                            experienciaSiguienteNivel = experienciaSiguienteNivel*2;
                            coins = coins+1000;

                            docRefUsuario.update(
                                    "coins",coins,
                                    "experiencia",0,
                                    "experienciaSiguienteNivel",experienciaSiguienteNivel,
                                    "nivel",nivel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AppFreesBet.mContext,"¡Has subido de nivel , ganas 1000 coins!",Toast.LENGTH_LONG).show();
                                        // añadir campo subida de nivel para actividad
                                        List<Map<String,Object>> listaActividadesDb = (List<Map<String,Object>>)usuarioDb.get("actividades") ;
                                        for(int i = 0; i < listaActividadesDb.size(); i++){
                                            if (((String)listaActividadesDb.get(i).get("idEvento")).equalsIgnoreCase(idEvento)){
                                                Map<String,Object> actividadActualizada = new HashMap<>();
                                                actividadActualizada.put("coins",cantidad);
                                                actividadActualizada.put("elección",competidor);
                                                actividadActualizada.put("gananciaPotencial",ganancia);
                                                actividadActualizada.put("idEvento",idEvento);
                                                actividadActualizada.put("coinsNivel",1000);
                                                actividadActualizada.put("fechaApuesta",new Date());
                                                listaActividadesDb.set(i,actividadActualizada);
                                            }
                                        }
                                        docRefUsuario.update("actividades",listaActividadesDb);

                                        // coinsNivel en apuestas
                                        DocumentReference docRefEvento = db.collection("eventos").document(idEvento);
                                        docRefEvento.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()){
                                                        Map<String, Object> eventoDb = document.getData();
                                                        List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas") ;
                                                        for(int i = 0; i < listaApuestasDb.size(); i++){
                                                            if (((String)listaApuestasDb.get(i).get("idUsuario")).equalsIgnoreCase(idUsuario)){
                                                                Map<String,Object> actividadActualizada = new HashMap<>();
                                                                actividadActualizada.put("coins",cantidad);
                                                                actividadActualizada.put("elección",competidor);
                                                                actividadActualizada.put("gananciaPotencial",ganancia);
                                                                actividadActualizada.put("idUsuario",idUsuario);
                                                                actividadActualizada.put("nombreUsuario",nombreUsuario);
                                                                actividadActualizada.put("coinsNivel",1000);
                                                                actividadActualizada.put("fechaApuesta",new Date());
                                                                listaApuestasDb.set(i,actividadActualizada);
                                                            }
                                                        }
                                                        docRefEvento.update("apuestas",listaApuestasDb);
                                                    }else{
                                                        Log.d("USUARIO", "No such document");
                                                    }
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }else{
                        Log.d("USUARIO", "No such document");
                    }
                }
            }
        });
    }
}
