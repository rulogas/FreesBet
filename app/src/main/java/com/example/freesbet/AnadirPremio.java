package com.example.freesbet;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class AnadirPremio extends AppCompatActivity {

    @BindView(R.id.editText_nombre_premio)
    EditText editTextNombrePremio;
    @BindView(R.id.editText_urlImagen_premio)
    EditText editTextUrlImagenPremio;
    @BindView(R.id.editText_coste_premio)
    EditText editTextCostePremio;

    @BindView(R.id.button_anadir_premio)
    Button buttonAnadirPremio;

    FirebaseFirestore db;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_premio);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Añadir premio");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        db = FirebaseFirestore.getInstance();

        buttonAnadirPremio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = true;

                if (editTextNombrePremio.getText().toString().isEmpty()){
                    editTextNombrePremio.setError("Introduce datos");
                    validate = false;
                }
                if (editTextUrlImagenPremio.getText().toString().isEmpty()){
                    editTextUrlImagenPremio.setError("Introduce datos");
                    validate = false;
                }
                if (editTextCostePremio.getText().toString().isEmpty()){
                    editTextCostePremio.setError("Introduce datos");
                    validate = false;
                }
                if (validate){
                    anadirPremio();
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

    private void anadirPremio(){

        progressDialog = new ProgressDialog(AnadirPremio.this);
        progressDialog.setMessage("Cargando eventos");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> premio = new HashMap<>();
        premio.put("nombre", editTextNombrePremio.getText().toString());
        premio.put("urlImagen", editTextUrlImagenPremio.getText().toString());
        premio.put("costeCoins", Integer.parseInt(editTextCostePremio.getText().toString()));


        db.collection("premios")
                .add(premio).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("PREMIO=>", "Premio añadido con ID: " + documentReference.getId());
                progressDialog.dismiss();
                Toast.makeText(AnadirPremio.this,"El premio se ha añadido con éxito", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("PREMIO=>", "Error adding document", e);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Ha ocurrido un error", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
