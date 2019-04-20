package com.example.freesbet.bases;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.freesbet.Ajustes;
import com.example.freesbet.Fms;
import com.example.freesbet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    public static int coinsUsuario;
    public static Menu menu;

    public static FirebaseFirestore db;

    public static String nombreUsuario;
    public static String emailUsuario;
    public static Uri photoUrlUsuario;
    public static String idUsuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        db = FirebaseFirestore.getInstance();

        getInfoUsuario();
        invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        invalidateOptionsMenu();

    }

    protected void setActionBar(@IdRes int idResToolbar) {
        toolbar = findViewById(idResToolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    protected void showHomeUpButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuToolbar_coins:
                startActivity(BaseActivity.this, Ajustes.class);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        DocumentReference docRef = db.collection("usuarios").document(idUsuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Usuario", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> user = document.getData();
                        coinsUsuario =((Long) user.get("coins")).intValue();
                        menu.findItem(R.id.menuToolbar_coins).setTitle(String.valueOf(coinsUsuario)+" Coins");
                    } else {
                        Log.d("Usuario", "No such document");
                    }
                } else {
                    Log.d("Usuario", "get failed with ", task.getException());
                }
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    protected void showSnackBar(String message, View container) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackBarLong(String message, View container) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show();
    }

    protected void startActivity(Activity activity, Class nextClass) {
        Intent i = new Intent(activity, nextClass);
        startActivity(i);
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public NumberFormat getNumberFormatWithoutDec() {
        return new DecimalFormat("#,###,##0");
    }

    public NumberFormat getNumberFormatWithDec() {
        return new DecimalFormat("#,###,##0.00");
    }


    public static void getCoinsUsuario(){

       //obtener coinsUsuario
        Fms.MyAsyncTasksGetCoinsUsuario myAsyncTasksGetCoinsUsuario = new MyAsyncTasksGetCoinsUsuario();
        myAsyncTasksGetCoinsUsuario.execute();

    }

    public static class MyAsyncTasksGetCoinsUsuario extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {





            return null;
        }
    }

    public static void getInfoUsuario(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            nombreUsuario = user.getDisplayName();
            emailUsuario = user.getEmail();
            photoUrlUsuario = user.getPhotoUrl();
            if (photoUrlUsuario == null){
                photoUrlUsuario = Uri.parse("android.resource://com.example.freesbet/drawable/usuario");
            }
            idUsuario = user.getUid();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();


        }
    }

}
