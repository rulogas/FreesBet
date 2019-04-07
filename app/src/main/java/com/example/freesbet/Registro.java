package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.widgets.Terminos;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Registro extends BaseActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_usuario)
    EditText mEditTextUsuario;
    @BindView(R.id.input_email) EditText mEditTextEmail;
    @BindView(R.id.input_contrasena) EditText mEditTextContrasena;
    @BindView(R.id.btn_registro)
    Button mButtonRegistro;
    @BindView(R.id.link_login)
    TextView mTextViewLogin;
    @BindView(R.id.text_terminos2)
    TextView mTextViewTerminos2;
    @BindView(R.id.checkbox_terminos)
    CheckBox mCheckBoxTerminos;
    ProgressDialog progressDialog;
    String mUsuario;
    String mEmail;
    String mContrasena;
    SharedPreferences sharedpreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);

        mButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singup();
            }
        });

        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        mTextViewTerminos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Terminos dialogFragment = new Terminos();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager,"terminos");
            }
        });
    }

    public void singup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mButtonRegistro.setEnabled(false);


        // TODO: Implement your own signup logic here.

        enviarFormulario();
    }

    private void enviarFormulario(){
        MyAsyncTasksFormRegistro myAsyncTasks = new MyAsyncTasksFormRegistro();
        myAsyncTasks.execute();
    }

    public class MyAsyncTasksFormRegistro extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Registro.this);
            progressDialog.setMessage("Login");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            mUsuario = mEditTextUsuario.getText().toString();
            mEmail = mEditTextEmail.getText().toString();
            mContrasena = mEditTextContrasena.getText().toString();
            String responseString = "";
            /*
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("alias", alias));
                nameValuePairs.add(new BasicNameValuePair("pin", pin));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                responseString= response.toString();


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            */
            return responseString;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            onSignupSuccess();
            /*
            if (s.equalsIgnoreCase("OK")){

            }else{
                onLoginFailed();
            }*/
        }
    }


    public void onSignupSuccess() {
        mButtonRegistro.setEnabled(true);
        sharedpreferences = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("correo", mEmail);
        editor.putString("contrasena", mContrasena);
        editor.commit();
        startActivity(Registro.this,Home.class);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error en el formualario de registro", Toast.LENGTH_LONG).show();

        mButtonRegistro.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String usuario = mEditTextUsuario.getText().toString();
        String email = mEditTextEmail.getText().toString();
        String contrasena = mEditTextContrasena.getText().toString();

        if (usuario.isEmpty() || usuario.length() < 4) {
            mEditTextUsuario.setError("El usuario debe tener al menos 4 caracteres");
            valid = false;
        } else {
            mEditTextUsuario.setError(null);
        }

        if (email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            mEditTextEmail.setError(null);
        } else {
            mEditTextEmail.setError("Introduce un correo electrónico válido");
            valid = false;
        }
        if (email.isEmpty() /*|| password.length() < 4 || password.length() > 10*/) {
            mEditTextEmail.setError("Introduce un correo electrónico");
            valid = false;
        } else {
            mEditTextEmail.setError(null);
        }

        if (contrasena.isEmpty() /*|| password.length() < 4 || password.length() > 10*/) {
            mEditTextContrasena.setError("Introduce una contraseña");
            valid = false;
        } else {
            mEditTextContrasena.setError(null);
        }
        if (contrasena.length() < 6 || contrasena.length() > 10) {
            mEditTextContrasena.setError("La contraseña debe tener entre 6 y 10 caracteres");
            valid = false;
        } else {
            mEditTextContrasena.setError(null);
        }
        if (mCheckBoxTerminos.isChecked()){
            mCheckBoxTerminos.setError(null);
        }else{
            mCheckBoxTerminos.setError("Debes aceptar los terminos y condiciones de uso");
        }
        return valid;
    }
}
