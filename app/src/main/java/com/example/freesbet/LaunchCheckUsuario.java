package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class LaunchCheckUsuario extends AppCompatActivity {

    ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_check_usuario);
        iniciarLoader();

        File f = new File("/data/data/com.example.freesbet/shared_prefs/preferenciasUsuario.xml");
        if (f.exists()){
            Log.d("TAG", "SharedPreferences preferenciasUsuario : existe");
            SharedPreferences sharedpreferences = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
            if (sharedpreferences.contains("correo")||sharedpreferences.contains("contrasena")){
                Intent in = new Intent(LaunchCheckUsuario.this,Home.class);
                nDialog.dismiss();
                startActivity(in);
            }
            else{
                Intent in = new Intent(LaunchCheckUsuario.this,MainActivity.class);
                nDialog.dismiss();
                startActivity(in);
            }
        }else{
            Intent in = new Intent(LaunchCheckUsuario.this,MainActivity.class);
            nDialog.dismiss();
            startActivity(in);
        }
        finish();
    }

    private void iniciarLoader(){
        nDialog = new ProgressDialog(LaunchCheckUsuario.this);
        nDialog.setMessage("Cargando..");
        nDialog.setTitle("Obteniendo datos");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

}
