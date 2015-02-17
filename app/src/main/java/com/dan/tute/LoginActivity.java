package com.dan.tute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;

public class LoginActivity extends ActionBarActivity {

    private static final String url_login_user = "http://68.119.36.37/tute/login.php";

    Button loginButton;
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mLoginButton;

    protected TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mSignUpTextView = (TextView)findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                new LoadProfileActivity().execute();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadProfileActivity extends AsyncTask<String, String, String>{
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            EditText txtEmail = (EditText) findViewById(R.id.usernameField);
            EditText txtPassword = (EditText) findViewById(R.id.passwordField);

            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            Log.d("Tute", email);
            Log.d("Tute", password);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = jsonParser.makeHttpRequest(url_login_user, "POST", params);

            try{
                int success = json.getInt("success");

                if(success == 1){
                    Intent i = getIntent();
                    pDialog.setMessage("Logged In!");
                    setResult(100, i);
                    finish();
                }else{
                    pDialog.setMessage("Failed to log in.");
                }
            }catch(JSONException e){
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(String file_url){
            pDialog.dismiss();
        }
    }
}
