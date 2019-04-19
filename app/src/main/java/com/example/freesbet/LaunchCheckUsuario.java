package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class LaunchCheckUsuario extends AppCompatActivity {

    ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_check_usuario);
        iniciarLoader();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent in = new Intent(LaunchCheckUsuario.this,Home.class);
            nDialog.dismiss();
            startActivity(in);
        } else {
            // No user is signed in
            Intent in = new Intent(LaunchCheckUsuario.this,FirebaseUIActivity.class);
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
