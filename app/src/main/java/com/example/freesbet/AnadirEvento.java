package com.example.freesbet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.freesbet.bases.CustomAdapterSpinnerCompetidores;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.freesbet.bases.BaseActivity.getInfoUsuarioNuevo;

public class AnadirEvento extends AppCompatActivity {

    @BindView(R.id.editText_nombre_evento)
    EditText editTextNombreEvento;
    @BindView(R.id.spinner_evento)
    Spinner spinnerEventos;
    @BindView(R.id.editText_zona_evento)
    EditText editTextZonaEvento;
    @BindView(R.id.editText_fecha_evento)
    EditText editTextFechaEvento;
    @BindView(R.id.editText_urlImagen_evento)
    EditText editTextUrlImagenEvento;
    @BindView(R.id.editText_competidores)
    EditText editTextCompetidores;
    @BindView(R.id.editText_cuota1)
    EditText editTextCuota1;
    @BindView(R.id.editText_cuota2)
    EditText editTextCuota2;
    @BindView(R.id.editText_boteAcumulado)
    EditText editTextBote;
    @BindView(R.id.textInputLayout_cuota1)
    TextInputLayout textInputLayoutCuota1;
    @BindView(R.id.textInputLayout_cuota2)
    TextInputLayout textInputLayoutCuota2;
    @BindView(R.id.textInputLayout_bote)
    TextInputLayout textInputLayoutBote;

    @BindView(R.id.button_anadir_evento)
    Button buttonAnadirEvento;

    String selecccionEvento;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_evento);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Añadir Evento");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        db = FirebaseFirestore.getInstance();

        inicializarSpinner();


        buttonAnadirEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = true;

                if (editTextNombreEvento.getText().toString().isEmpty()){
                    editTextNombreEvento.setError("Introduce datos");
                    validate = false;
                }
                if (editTextZonaEvento.getText().toString().isEmpty()){
                    editTextZonaEvento.setError("Introduce datos");
                    validate = false;
                }
                if (editTextFechaEvento.getText().toString().isEmpty()){
                    editTextFechaEvento.setError("Introduce datos");
                    validate = false;
                }
                if (editTextUrlImagenEvento.getText().toString().isEmpty()){
                    editTextUrlImagenEvento.setError("Introduce datos");
                    validate = false;
                }
                if (editTextCompetidores.getText().toString().isEmpty()){
                    editTextCompetidores.setError("Introduce datos");
                    validate = false;
                }
                if (editTextCuota1.getText().toString().isEmpty()){
                    editTextCuota1.setError("Introduce datos");
                    validate = false;
                }
                if (editTextCuota2.getText().toString().isEmpty()){
                    editTextCuota2.setError("Introduce datos");
                    validate = false;
                }
                if (validate){
                    anadirEvento();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayList<String> listaEventos = new ArrayList<>();
        listaEventos.add("FMS");
        listaEventos.add("Red Bull");
        listaEventos.add("BDM");
        listaEventos.add("Supremacía MC");

        CustomAdapterSpinnerCompetidores customAdapterSpinnerCompetidores = new CustomAdapterSpinnerCompetidores(AnadirEvento.this,listaEventos);
        spinnerEventos.setAdapter(customAdapterSpinnerCompetidores);

        spinnerEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecccionEvento = parent.getItemAtPosition(position).toString();
                if (selecccionEvento.equalsIgnoreCase("FMS")){
                    textInputLayoutCuota1.setVisibility(View.VISIBLE);
                    textInputLayoutCuota2.setVisibility(View.VISIBLE);
                    textInputLayoutBote.setVisibility(View.GONE);
                }else{
                    textInputLayoutCuota1.setVisibility(View.GONE);
                    textInputLayoutCuota2.setVisibility(View.GONE);
                    textInputLayoutBote.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selecccionEvento = parent.getItemAtPosition(0).toString();
            }
        });
    }

    private void anadirEvento(){
        Map<String, Object> evento = new HashMap<>();
        evento.put("nombre", editTextNombreEvento.getText().toString());
        evento.put("evento", selecccionEvento);
        evento.put("zona", editTextZonaEvento.getText().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaEvento = null;
        try {
            fechaEvento = formatter.parse(editTextFechaEvento.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        evento.put("fecha", fechaEvento);
        evento.put("urlImagen", editTextUrlImagenEvento.getText().toString());
        String[] competidores = editTextCompetidores.getText().toString().split(",");
        List<String> listaCompetidores = Arrays.asList(competidores);
        evento.put("competidores", listaCompetidores);
        if (selecccionEvento.equalsIgnoreCase("FMS")){
            evento.put("tipo", "Liga");
            double numero1 = Double.parseDouble(editTextCuota1.getText().toString());
            evento.put("cuota1", numero1);
            double numero2 = Double.parseDouble(editTextCuota2.getText().toString());
            evento.put("cuota2", numero2);
        }else{
            evento.put("tipo", "competicion");
            evento.put("boteAcumulado", Integer.parseInt(editTextBote.getText().toString()) );
        }
        List<Map<String,Object>> apuestas = new ArrayList<>();
        evento.put("apuestas", apuestas);
        evento.put("checkEvento", false);
        evento.put("finalizado", false);
        evento.put("numeroApuestas", 0);

        db.collection("eventos")
                .add(evento).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("EVENTO=>", "Evento añadido con ID: " + documentReference.getId());
                Toast.makeText(AnadirEvento.this,"El evento se ha añadido con éxito", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("EVENTO", "Error adding document", e);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Ha ocurrido un error", Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
