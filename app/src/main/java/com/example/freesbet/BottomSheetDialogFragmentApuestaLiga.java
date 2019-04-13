package com.example.freesbet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.freesbet.widgets.General;


public class BottomSheetDialogFragmentApuestaLiga extends BottomSheetDialogFragment {
    String cuota;
    String puntosUsuario;
    boolean validate ;
    ProgressDialog progressDialog;
    public BottomSheetDialogFragmentApuestaLiga(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cuota = getArguments().getString("cuota");
        puntosUsuario = getArguments().getString("puntosUsuario");
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_apuesta_liga, container, false);
        TextView textoCuota = v.findViewById(R.id.textView_cuota);
        TextView textoGanancia = v.findViewById(R.id.textView_ganancia);
        EditText campoCantidad = v.findViewById(R.id.editText_cantidad);
        SeekBar seekBar = v.findViewById(R.id.seekBar);
        AppCompatButton botonMin = v.findViewById(R.id.button_min);
        AppCompatButton botonMax = v.findViewById(R.id.button_max);
        AppCompatButton botonJugar = v.findViewById(R.id.boton_jugar);
        textoCuota.setText(cuota);
        // calcular ganancia potencial
        int cantidad = Integer.parseInt(campoCantidad.getText().toString());
        int ganancia =(int)(((double)cantidad * Double.parseDouble(cuota))-(double)cantidad);
        textoGanancia.setText("+"+Integer.toString(+ganancia));

        campoCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()){

                    int cantidad = Integer.parseInt(campoCantidad.getText().toString());
                    int ganancia =(int)(((double)cantidad * Double.parseDouble(cuota))-(double)cantidad);
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
                    MyAsyncTasksApostarLiga myAsyncTasksApostarLiga = new MyAsyncTasksApostarLiga();
                    myAsyncTasksApostarLiga.execute(campoCantidad.getText().toString());
                }
            }
        });

        return v;
    }

    public class MyAsyncTasksApostarLiga extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Realizando apuesta");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            int puntosApostados = Integer.parseInt(params[0]);
            // restar puntos a usuario y guardar apuesta en base de datos

            return null;
            //return responseString;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            /*
            if (s.equalsIgnoreCase("OK")){
                //setear puntos usuario en toolbar

            }else{
                onLoginFailed();
            }*/

            dismiss();
            getActivity().findViewById(R.id.button_cuota1).setEnabled(false);
            getActivity().findViewById(R.id.button_cuota2).setEnabled(false);
            RelativeLayout relativeLayout = getActivity().findViewById(R.id.relativeLayout_apuesta_hecha_liga_advertencia);
            relativeLayout.setVisibility(View.VISIBLE);

        }
    }
}
