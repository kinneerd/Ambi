package com.dan.tute;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {


    @InjectView(R.id.create_request) protected TextView mCreate_Request;
    @InjectView(R.id.search_tutor) protected TextView mSearch_tutor;
    @InjectView(R.id.edit_profile) protected TextView mEdit_Profile;
    @InjectView(R.id.tool_bar) protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);   // Setting toolbar as the ActionBar with setSupportActionBar() call
        setupUser();

        // request button listener
        mCreate_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RequestActivity.class);
                startActivity(intent);
            }
        });

        mSearch_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

        mEdit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditBasicProfile.class);
                String user_email = SessionManager.getLoggedInEmailUser(getApplicationContext());
                intent.putExtra("email",user_email);
                startActivity(intent);
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*
    Uses SessionManager to check whether user is currently logged in
    Navigates to appropriate screen
     */
    private void setupUser() {
        if(SessionManager.getUserLoggedInStatus(getApplicationContext())) {
            String email = SessionManager.getLoggedInEmailUser(getApplicationContext());
            //Intent intent = new Intent(getApplicationContext(), EditBasicProfile.class);
            //intent.putExtra("email", email);
            //startActivity(intent);
        }else {
            // Not logged in
            navigateToLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_logout:
                SessionManager.clearUserSharedPreferences(getApplicationContext());
                navigateToLogin();
                break;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
