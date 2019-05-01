package com.example.freesbet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.BaseActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.freesbet.bases.BaseActivity.getInfoUsuario;
import static com.example.freesbet.bases.BaseActivity.getInfoUsuarioNuevo;
import static com.example.freesbet.bases.BaseActivity.photoUrlUsuario;

public class FirebaseUIActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();

                // Access a Cloud Firestore instance from your Activity

                db = FirebaseFirestore.getInstance();

                DocumentReference docRef = db.collection("usuarios").document(this.user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Usuario", "Ya existe el usuario: " + document.getData());
                                System.out.println("Ya existe el usuario: " + document.getData());
                                getInfoUsuario();
                                Intent in = new Intent(FirebaseUIActivity.this, Home.class);
                                startActivity(in);
                                finish();
                            } else {
                                Log.d("Usuario", "Usuario no existe");
                                crearUsuarioNuevo();
                            }
                        } else {
                            Log.d("Error inesperado", "get failed with ", task.getException());
                        }
                    }
                });
                // ...
            } else {

                System.out.println(response.getError().getErrorCode());
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void crearUsuarioNuevo(){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("nombre", this.user.getDisplayName());
        user.put("nivel", 0);
        user.put("experiencia", 0);
        user.put("coins",4000);
        user.put("experienciaSiguienteNivel", 400);
        List<Map<String,Object>> listaActividades = new ArrayList<>();
        user.put("actividades",listaActividades);
        // Add a new document with a generated ID
        db.collection("usuarios")
                .document(this.user.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Usuario", "Usuario añadido");
                        System.out.println("Usuario añadido");
                        getInfoUsuarioNuevo();
                        Intent in = new Intent(FirebaseUIActivity.this, Home.class);
                        startActivity(in);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Usuario", "Error al añadir usuario", e);
                    }
                });
    }
}
