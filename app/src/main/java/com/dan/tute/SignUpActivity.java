package com.dan.tute;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends ActionBarActivity {

    public static final String TAG = SignUpActivity.class.getSimpleName();

    @InjectView(R.id.emailField) protected TextView mEmail;
    @InjectView(R.id.passwordField) protected TextView mPassword;
    @InjectView(R.id.nameField) protected TextView mName;

    //@InjectView(R.id.genderMaleRadio) protected RadioButton mMale;
    //@InjectView(R.id.genderFemaleRadio) protected RadioButton mFemale;
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
                String name = mName.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                name = name.trim();
                password = password.trim();
                email = email.trim();

                if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    // Send Verification email
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
}
