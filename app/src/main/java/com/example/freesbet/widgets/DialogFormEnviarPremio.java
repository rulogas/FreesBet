package com.example.freesbet.widgets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freesbet.MainActivity;
import com.example.freesbet.R;
import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.bases.Premio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.freesbet.bases.BaseActivity.coinsUsuario;
import static com.example.freesbet.bases.BaseActivity.emailUsuario;
import static com.example.freesbet.bases.BaseActivity.idUsuario;

public class DialogFormEnviarPremio extends DialogFragment {

    View view;
    AppCompatButton buttonCancelar;
    AppCompatButton buttonEnviarDatos;
    EditText editTextNombreYapellidos;
    EditText editTextEmail;
    EditText editTextDireccion;
    EditText editTextCiudad;
    EditText editTextProvincia;
    EditText editTextCp;
    EditText editTextNumTelefono;
    ProgressDialog progressDialog;
    boolean enviado = false;

    String premioId ;
    String nombrePremio;
    int costeCoins;

    FirebaseFirestore db;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        db = FirebaseFirestore.getInstance();
        premioId = getArguments().getString("idPremio");
        nombrePremio = getArguments().getString("nombrePremio");
        costeCoins = getArguments().getInt("costeCoins");

        view = inflater.inflate(R.layout.dialog_form_enviar_premio, null);
        builder.setView(view);

        inicializarElementos();

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonEnviarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = true;
                if (editTextNombreYapellidos.getText().toString().isEmpty()){
                    editTextNombreYapellidos.setError("Introduce tus datos");
                    validate = false;
                }
                if (editTextEmail.getText().toString().isEmpty()|| !editTextEmail.getText().toString().matches(Patterns.EMAIL_ADDRESS.pattern())){
                    editTextEmail.setError("Introduce un correo electrónico válido");
                    validate = false;
                }
                if (editTextDireccion.getText().toString().isEmpty()){
                    editTextDireccion.setError("Introduce tus datos");
                    validate = false;
                }
                if (editTextCiudad.getText().toString().isEmpty()){
                    editTextCiudad.setError("Introduce tus datos");
                    validate = false;
                }
                if (editTextProvincia.getText().toString().isEmpty()){
                    editTextProvincia.setError("Introduce tus datos");
                    validate = false;
                }
                if (editTextCp.getText().toString().isEmpty()){
                    editTextCp.setError("Introduce tus datos");
                    validate = false;
                }
                if (editTextNumTelefono.getText().toString().isEmpty()){
                    editTextNumTelefono.setError("Introduce tus datos");
                    validate = false;
                }
                if (validate){
                    anadirPedido();

                }
            }
        });
        return builder.create();
    }

    private void inicializarElementos(){
        buttonCancelar = view.findViewById(R.id.button_enviarpremio_cancelar);
        buttonEnviarDatos = view.findViewById(R.id.button_enviarpremio_enviar);
        editTextNombreYapellidos = view.findViewById(R.id.editText_nombreyapellidos);
        editTextEmail = view.findViewById(R.id.editText_email);
        editTextDireccion = view.findViewById(R.id.editText_direccion);
        editTextCiudad = view.findViewById(R.id.editText_Ciudad);
        editTextProvincia = view.findViewById(R.id.editText_Provincia);
        editTextCp = view.findViewById(R.id.editText_cp);
        editTextNumTelefono = view.findViewById(R.id.editText_numero_telefono);
    }

    private void anadirPedido(){

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Enviando Datos");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> pedido = new HashMap<>();

        pedido.put("nombre", editTextNombreYapellidos.getText().toString());
        pedido.put("email", editTextEmail.getText().toString());
        pedido.put("dirección", editTextDireccion.getText().toString());
        pedido.put("ciudad", editTextCiudad.getText().toString());
        pedido.put("provincia", editTextProvincia.getText().toString());
        pedido.put("CP", editTextCp.getText().toString());
        pedido.put("teléfono", editTextNumTelefono.getText().toString());
        pedido.put("idPremio", premioId);
        pedido.put("nombrePremio", nombrePremio);
        pedido.put("idUsuario", idUsuario);
        pedido.put("estado", "En espera");


        db.collection("pedidos")
                .add(pedido)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("PEDIDO", "DocumentSnapshot written with ID: " + documentReference.getId());
                        enviado = true;
                        DocumentReference docRefUsuario = db.collection("usuarios").document(idUsuario);
                        int coinsFinales = coinsUsuario - costeCoins;

                        docRefUsuario.update("coins",coinsFinales).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("USUARIO", "DocumentSnapshot successfully updated!");
                                dismiss();
                                progressDialog.dismiss();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("USUARIO", "Error updating document", e);
                                    }
                                });;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AppFreesBet.mContext,"Ha ocurrdo un error inesperado",Toast.LENGTH_LONG);
                        Log.w("PEDIDO", "Error adding document", e);
                    }
                });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (enviado){
            General dialogFragment = new General();
            Bundle mensaje = new Bundle();
            mensaje.putString("titulo","Hemos recibido tus datos");
            mensaje.putString("mensaje","El envío de tu premio puede tardar de 1 a 3 semanas. Se enviará un email de confirmación de pedido a tu correo en las próximas 24h.");
            dialogFragment.setArguments(mensaje);
            FragmentManager fragmentManager = getFragmentManager();
            dialogFragment.show(fragmentManager,"general");
        }
    }
}
