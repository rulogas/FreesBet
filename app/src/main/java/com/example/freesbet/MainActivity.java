package com.example.freesbet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.freesbet.bases.BaseActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.SignInButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.input_alias)
    EditText _aliasText;
    @BindView(R.id.input_pin)
    EditText _pinText;
    @BindView(R.id.sign_in_button)
    SignInButton googleButton;
    @BindView(R.id.enlace_registro)
    TextView mTextViewRegistro;
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String mCorreo;
    String mContrasena;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        showHomeUpButton();
        googleButton.setSize(SignInButton.SIZE_STANDARD);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        mTextViewRegistro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(MainActivity.this,Registro.class);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);





        // TODO: Implement your own authentication logic here.
        enviarFormulario();


    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        sharedpreferences = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("correo", mCorreo);
        editor.putString("contrasena", mContrasena);
        editor.commit();
        _aliasText.setText("");
        _pinText.setText("");
        startActivity(MainActivity.this,Home.class);
        finish();
    }

    public void onLoginFailed() {
        showSnackBarLong("Error en el formulario",getWindow().getDecorView().findViewById(android.R.id.content));
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _aliasText.getText().toString();
        String password = _pinText.getText().toString();

        if (email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            _aliasText.setError(null);
        } else {
            _aliasText.setError("Introduce un correo electrónico válido");
            valid = false;
        }
        if (email.isEmpty() /*|| password.length() < 4 || password.length() > 10*/) {
            _pinText.setError("Introduce una correo electrónico");
            valid = false;
        } else {
            _pinText.setError(null);
        }

        if (password.isEmpty() /*|| password.length() < 4 || password.length() > 10*/) {
            _pinText.setError("Introduce una contraseña");
            valid = false;
        } else {
            _pinText.setError(null);
        }
        if (password.length() < 6 || password.length() > 10) {
            _pinText.setError("La contraseña debe tener entre 6 y 10 caracteres");
            valid = false;
        } else {
            _pinText.setError(null);
        }
        return valid;
    }

    private void enviarFormulario(){
        MyAsyncTasksFormLogin myAsyncTasks = new MyAsyncTasksFormLogin();
        myAsyncTasks.execute();
    }

    public class MyAsyncTasksFormLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Login");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            mCorreo = _aliasText.getText().toString();
            mContrasena = _pinText.getText().toString();
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
            onLoginSuccess();
            /*
            if (s.equalsIgnoreCase("OK")){

            }else{
                onLoginFailed();
            }*/
        }
    }
}
