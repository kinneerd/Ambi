package com.dan.tute;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class SignUpActivity extends ActionBarActivity {

    private static final String url_signup_user = "http://68.119.36.37/tute/signup.php";
    protected ProgressDialog pDialog;
    protected String phpMessage = "";
    protected JSONParser jsonParser = new JSONParser();
    boolean signupSuccess;


    public static final String TAG = SignUpActivity.class.getSimpleName();

    @InjectView(R.id.emailField) protected TextView mEmail;
    @InjectView(R.id.passwordField) protected TextView mPassword;
    @InjectView(R.id.nameField) protected TextView mName;
    @InjectView(R.id.radioGender) protected RadioGroup mGenderGroup;
    //@InjectView(R.id.genderID)

    @InjectView(R.id.signupButton) protected Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        ButterKnife.inject(this);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    // Success
                    new StoreUserActivity().execute();
                }
            }
        });
    }

    class StoreUserActivity extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Signing up...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            signupSuccess = false;
        }

        protected String doInBackground(String... args) {

            int genderID = mGenderGroup.getCheckedRadioButtonId();
            RadioButton mGender = (RadioButton) findViewById(genderID);

            String gender = (mGender.getText()+"").toLowerCase().charAt(0) + "";

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String name = mName.getText().toString().trim();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("gender", gender));

            JSONObject json = jsonParser.makeHttpRequest(url_signup_user, "POST", params);

            try{
                int success = json.getInt("success");

                if(success == 1){
                    signupSuccess = true;

                    Intent i = getIntent();
                    setResult(100, i);
                    finish();
                }else{
                    phpMessage = json.getString("message");
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String file_url){
            pDialog.dismiss();

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast;

            if(signupSuccess){
                CharSequence text = "Sign up successful!";
                toast = Toast.makeText(context, text, duration);
                toast.show();
            }else{
                CharSequence text = phpMessage;
                toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

}
