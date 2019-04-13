package com.example.freesbet.widgets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.freesbet.MainActivity;
import com.example.freesbet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogFormEnviarPremio extends DialogFragment {

    View view;
    AppCompatButton buttonCancelar;
    AppCompatButton buttonEnviarDatos;
    EditText editTextNombreYapellidos;
    EditText editTextDireccion;
    EditText editTextCiudad;
    EditText editTextProvincia;
    EditText editTextCp;
    EditText editTextNumTelefono;
    ProgressDialog progressDialog;
    boolean enviado = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

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
                    enviarDatos();
                    dismiss();
                }
            }
        });
        return builder.create();
    }

    private void inicializarElementos(){
        buttonCancelar = view.findViewById(R.id.button_enviarpremio_cancelar);
        buttonEnviarDatos = view.findViewById(R.id.button_enviarpremio_enviar);
        editTextNombreYapellidos = view.findViewById(R.id.editText_nombreyapellidos);
        editTextDireccion = view.findViewById(R.id.editText_direccion);
        editTextCiudad = view.findViewById(R.id.editText_Ciudad);
        editTextProvincia = view.findViewById(R.id.editText_Provincia);
        editTextCp = view.findViewById(R.id.editText_cp);
        editTextNumTelefono = view.findViewById(R.id.editText_numero_telefono);
    }

    private void enviarDatos(){
        MyAsyncTasksFormEnviarPremio myAsyncTasksFormEnviarPremio = new MyAsyncTasksFormEnviarPremio();
        myAsyncTasksFormEnviarPremio.execute();
    }

    public class MyAsyncTasksFormEnviarPremio extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Enviando Datos");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String responseString = "";

            return responseString;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            enviado = true;
        }
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
