package com.dan.tute;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends ActionBarActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final String url_login_user = "http://68.119.36.255/tute/login.php";

    //protected ProgressDialog pDialog;

    protected JSONParser jsonParser = new JSONParser();
    protected boolean loginSuccess;
    protected String email;
    protected int verified = 0;

    @InjectView(R.id.progressBar)protected ProgressBar mProgressBar;
    @InjectView(R.id.title) protected TextView appTitle;
    @InjectView(R.id.subtitle) protected TextView appSubtitle;

    @InjectView(R.id.emailField) protected TextView mEmail;
    @InjectView(R.id.passwordField) protected TextView mPassword;
    @InjectView(R.id.loginButton) protected Button mLoginButton;

    @InjectView(R.id.signUpText) protected TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        ButterKnife.inject(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        // Changed font
        Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        appTitle.setTypeface(type);
        appSubtitle.setTypeface(type);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                new LoadProfileActivity().execute();

            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            //mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            //mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    class LoadProfileActivity extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();
            toggleRefresh();
            //pDialog = new ProgressDialog(LoginActivity.this);
            //pDialog.setMessage("Logging in...");
            //pDialog.setIndeterminate(false);
            //pDialog.setCancelable(true);
            //pDialog.show();

            loginSuccess = false;
        }

        protected String doInBackground(String... args) {

            email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = jsonParser.makeHttpRequest(url_login_user, "POST", params);

            try{
                if(json != null) {
                    int success = json.getInt("success");

                    if (success == 1) {
                        //Intent i = getIntent();
                        loginSuccess = true;
                        verified = json.getInt("verified");
                        //setResult(100, i);
                        //finish();
                    }else {
                        // Failure
                    }
                }else {
                    // Failure
                }
            }catch(JSONException e) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast;
                CharSequence text = "Error! Couldn't connect to server!";
                toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            return null;
        }

        protected void onPostExecute(String file_url){
            //pDialog.dismiss();

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast;

            if(loginSuccess){
                if(verified == 1) {
                    // Save login in SharedPreferences
                    SessionManager.setLoggedInUserEmail(getApplicationContext(), email);
                    SessionManager.setUserLoggedInStatus(getApplicationContext(), true);

                    CharSequence text = "Log in successful!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    toggleRefresh();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // Prevent backtracking to LoginActivity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    CharSequence text = "Please verify your account!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    toggleRefresh();
                }
            }else {
                CharSequence text = "Log in failed.";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                toggleRefresh();
            }
        }
    }
}
