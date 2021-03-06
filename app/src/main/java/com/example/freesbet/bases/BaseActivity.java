package com.example.freesbet.bases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.freesbet.Admin;
import com.example.freesbet.Ajustes;
import com.example.freesbet.Apuesta;
import com.example.freesbet.Fms;
import com.example.freesbet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public abstract class BaseActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    public static int coinsUsuario;
    public static int nivelUsuario;
    public static Menu menu;

    public static FirebaseFirestore db;

    public static String nombreUsuario;
    public static String emailUsuario;
    public static Uri photoUrlUsuario;
    public static String idUsuario;

    public static StorageReference mStorageRef;
    static ProgressDialog progressDialog;

    public static BooVariable datosUsuarioActualizados;

    MenuItem itemSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        datosUsuarioActualizados = new BooVariable();
        db = FirebaseFirestore.getInstance();
        getInfoUsuario();

        comprobarApuestasFinalizadas();
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
        if (item.getItemId()== R.id.menuToolbar_coins){
            startActivity(BaseActivity.this, Ajustes.class);
            finish();
        }
        if (item.getItemId()== 1){
            startActivity(BaseActivity.this, Admin.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;

        DocumentReference docRef = db.collection("usuarios").document(idUsuario);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Log.d("Usuario", "DocumentSnapshot data: " + documentSnapshot.getData());
                    Map<String, Object> user = documentSnapshot.getData();
                    coinsUsuario =((Long) user.get("coins")).intValue();
                    menu.findItem(R.id.menuToolbar_coins).setTitle(String.valueOf(coinsUsuario)+" Coins");

                        if (idUsuario.equalsIgnoreCase("UCGqcovz6oYpRxFH7Mlhreyk4Ht2")){
                            itemSettings = menu.add(Menu.NONE,1,Menu.NONE,"");
                            itemSettings.setIcon(R.drawable.ic_admin).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                        }

                } else {
                    Log.d("Usuario", "No such document");
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



    public static void getInfoUsuario(){
        datosUsuarioActualizados = new BooVariable();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()){
                // Name, email address, and profile photo Url
                nombreUsuario = user.getDisplayName();
                emailUsuario = user.getEmail();
                idUsuario = user.getUid();
                getNivelUsuario();

                getImagenUsuario();

                // Check if user's email is verified
                boolean emailVerified = user.isEmailVerified();

                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.
                String uid = user.getUid();
            }


        }
    }

    public static void getInfoUsuarioNuevo(){
        datosUsuarioActualizados = new BooVariable();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()){
                // Name, email address, and profile photo Url
                nombreUsuario = user.getDisplayName();
                emailUsuario = user.getEmail();
                idUsuario = user.getUid();
                getNivelUsuario();
                //obtener foto del provider
                Uri photoUsuarioProvider;
                photoUsuarioProvider = profile.getPhotoUrl();

                if (photoUsuarioProvider != null){
                    guardarImagenUsuarioUrl(photoUsuarioProvider.toString());
                }else {
                    Bitmap bitmapImagenUsuario = ((BitmapDrawable)ResourcesCompat.getDrawable(AppFreesBet.mContext.getResources(),R.drawable.usuario,null)).getBitmap();
                    //photoUsuarioProvider = Uri.parse("android.resource://com.example.freesbet/drawable/usuario");
                    guardarImagenUsuarioDefecto(bitmapImagenUsuario);
                }


                // Check if user's email is verified
                boolean emailVerified = user.isEmailVerified();

                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.
                String uid = user.getUid();
            }


        }
    }

    public static void guardarImagenUsuario(Uri fotoUsuario){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = mStorageRef.child("imagenes/usuarios/"+idUsuario+"/"+"imagen_usuario.jpg");

        userRef.putFile(fotoUsuario)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        System.out.println("Imagen guardada");
                        getImagenUsuario();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public static void getImagenUsuario(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child("imagenes/usuarios/"+idUsuario+"/imagen_usuario.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                photoUrlUsuario = uri;
                System.out.println("uri obtenida");
                datosUsuarioActualizados.setBoo(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }

    public static void guardarImagenUsuarioUrl(String url){
        MyAsyncTasksguardarImagenUsuarioUrl myAsyncTasksguardarImagenUsuarioUrl = new MyAsyncTasksguardarImagenUsuarioUrl(url);
        myAsyncTasksguardarImagenUsuarioUrl.execute();

    }

    public static class MyAsyncTasksguardarImagenUsuarioUrl extends AsyncTask<String, String, String> {

        String url;

        public MyAsyncTasksguardarImagenUsuarioUrl(String url){
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {


            URL urlImagen;
            try {
                urlImagen  = new URL(url);
                HttpURLConnection connection  = null;

                try {
                    connection = (HttpURLConnection) urlImagen.openConnection();
                    InputStream stream = connection.getInputStream();

                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference userRef = mStorageRef.child("imagenes/usuarios/"+idUsuario+"/"+"imagen_usuario.jpg");
                    UploadTask uploadTask = userRef.putStream(stream);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            System.out.println("ImagenURL guardada");
                            getImagenUsuario();

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void guardarImagenUsuarioDefecto(Bitmap bitmapImagenUsuario){
        // Get the data from an ImageView as bytes
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userRef = mStorageRef.child("imagenes/usuarios/"+idUsuario+"/"+"imagen_usuario.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImagenUsuario.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Imagen por defecto guardada");
                getImagenUsuario();
            }
        });
    }

    public static void getNivelUsuario(){
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("usuarios").document(idUsuario);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Log.d("Usuario", "DocumentSnapshot data: " + documentSnapshot.getData());
                    Map<String, Object> user = documentSnapshot.getData();
                    nivelUsuario =((Long) user.get("nivel")).intValue();
                    datosUsuarioActualizados.setBoo(true);
                } else {
                    Log.d("Usuario", "No such document");
                }
            }
        });
    }

    public static void comprobarApuestasFinalizadas(){
        Query query = db.collection("eventos")
                .whereEqualTo("finalizado", true).whereEqualTo("checkEvento",false);
        ListenerRegistration registration =query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@android.support.annotation.Nullable QuerySnapshot value,
                                @android.support.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("evento", "Listen failed.", e);
                    return;
                }
                if (value != null && !value.isEmpty()){
                    for (QueryDocumentSnapshot document : value) {

                        Log.d("Evento", document.getId() + " => " + document.getData());

                        Map<String, Object> eventoDb = document.getData();
                        List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas");

                        if (!listaApuestasDb.isEmpty()){
                            for (Map<String,Object> apuesta : listaApuestasDb){
                                if (((String)apuesta.get("elección")).equalsIgnoreCase(((String)eventoDb.get("ganador")))){
                                    DocumentReference docRefUsuario = db.collection("usuarios").document((String)apuesta.get("idUsuario"));
                                    docRefUsuario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                DocumentSnapshot document = task.getResult();
                                                Map<String, Object> usuarioDb = document.getData();
                                                int coins = ((Long) usuarioDb.get("coins")).intValue();
                                                int gananciaPotencial = ((Long) apuesta.get("gananciaPotencial")).intValue();
                                                int coinsApostados = ((Long) apuesta.get("coins")).intValue();
                                                int gananciaFinal = gananciaPotencial + coinsApostados;
                                                int coinsFinales = coins + gananciaFinal;

                                                docRefUsuario.update("coins",coinsFinales).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            if (((String)apuesta.get("idUsuario")).equalsIgnoreCase(idUsuario)){
                                                                Toast.makeText(AppFreesBet.mContext,"Has ganado "+ gananciaPotencial
                                                                        + " en "+((String) eventoDb.get("nombre")),Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        DocumentReference docRefEvento = db.collection("eventos").document(document.getId());
                        docRefEvento.update("checkEvento",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    comprobarCoins();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static void comprobarCoins(){
        List<String> usuariosPosiblesSinCoins = new ArrayList<>();

        Query query = db.collection("usuarios");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot document : querySnapshot){
                        usuariosPosiblesSinCoins.add(document.getId());

                    }

                    Query query2 = db.collection("eventos").whereEqualTo("checkEvento", false);
                    query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                for (QueryDocumentSnapshot document : querySnapshot){
                                    Map<String, Object> eventoDb = document.getData();
                                    List<Map<String,Object>> listaApuestasDb = (List<Map<String,Object>>)eventoDb.get("apuestas");
                                    for (Map<String,Object> apuesta : listaApuestasDb){
                                        for (int i = 0; i<usuariosPosiblesSinCoins.size(); i++){
                                            if (((String)apuesta.get("idUsuario")).equalsIgnoreCase(usuariosPosiblesSinCoins.get(i))){
                                                usuariosPosiblesSinCoins.remove(i);
                                            }
                                        }
                                    }
                                }

                                if (!usuariosPosiblesSinCoins.isEmpty()){
                                    for (String idUsuarioSinCoins : usuariosPosiblesSinCoins){
                                        DocumentReference docRefUsuario = db.collection("usuarios").document(idUsuarioSinCoins);
                                        docRefUsuario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()){
                                                        Map<String, Object> usuarioDb = document.getData();
                                                        if (((Long) usuarioDb.get("coins")).intValue() == 0){
                                                            if (idUsuarioSinCoins.equalsIgnoreCase(idUsuario)){
                                                                Toast.makeText(AppFreesBet.mContext, "Has perdido todos tus coins en juego. Recargando...",
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    docRefUsuario.update("coins",4000)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                if (idUsuarioSinCoins.equalsIgnoreCase(idUsuario)){
                                                                                    Toast.makeText(AppFreesBet.mContext, "Tus coins se han recargado",
                                                                                            Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }, 1000);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

}
