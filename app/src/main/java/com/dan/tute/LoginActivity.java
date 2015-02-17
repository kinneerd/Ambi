package com.dan.tute;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends ActionBarActivity {

    private static final String url_login_user = "http://68.119.36.37/tute/login.php";

    protected ProgressDialog pDialog;

    protected JSONParser jsonParser = new JSONParser();

    protected boolean loginSuccess;

    protected int verified = 0;

    public static final String TAG = LoginActivity.class.getSimpleName();

    @InjectView(R.id.emailField) protected TextView mEmail;
    @InjectView(R.id.passwordField) protected TextView mPassword;
    @InjectView(R.id.loginButton) protected Button mLoginButton;

    @InjectView(R.id.signUpText) protected TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ButterKnife.inject(this);

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

        mLoginButton = (Button) findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
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

    class LoadProfileActivity extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            loginSuccess = false;

        }

        protected String doInBackground(String... args) {
            EditText txtEmail = (EditText) findViewById(R.id.emailField);
            EditText txtPassword = (EditText) findViewById(R.id.passwordField);

            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = jsonParser.makeHttpRequest(url_login_user, "POST", params);

            try{
                int success = json.getInt("success");

                if(success == 1){
                    //Intent i = getIntent();
                    loginSuccess = true;
                    verified = json.getInt("verified");
                    //setResult(100, i);
                    //finish();
                }else{
                }
            }catch(JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url){
            pDialog.dismiss();

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast;

            if(loginSuccess){
                if(verified == 1) {
                    CharSequence text = "Log in successful!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    CharSequence text = "Please verify your account!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }else{
                CharSequence text = "Log in failed.";
                toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}
