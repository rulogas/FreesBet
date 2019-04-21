package com.example.freesbet.widgets;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


import com.example.freesbet.Ajustes;
import com.example.freesbet.FirebaseUIActivity;
import com.example.freesbet.R;
import com.example.freesbet.MainActivity;
import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.BooVariable;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckLogout extends DialogFragment {




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.check_cerrarSesion)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AuthUI.getInstance().signOut(getContext());
                            Intent in = new Intent(getActivity(), FirebaseUIActivity.class);
                            startActivity(in);
                        BaseActivity.photoUrlUsuario = null;
                            getActivity().finish();


                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });




        // Create the AlertDialog object and return it
        return builder.create();
    }

}
