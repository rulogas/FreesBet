package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class LaunchCheckUsuario extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_check_usuario);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent in = new Intent(LaunchCheckUsuario.this,Home.class);
                    startActivity(in);
                } else {
                    // No user is signed in
                    Intent in = new Intent(LaunchCheckUsuario.this,FirebaseUIActivity.class);
                    startActivity(in);
                }
                finish();
            }
        }, 1000);

    }

}
