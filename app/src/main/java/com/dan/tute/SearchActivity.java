package com.dan.tute;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dan.tute.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends ListActivity {

    protected String url_load_tutors = "http://68.119.36.255/tute/requestTutorList.php";

    protected ArrayList<HashMap<String,String>> tutors = new ArrayList<HashMap<String,String>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        new LoadTutorsInformation().execute();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String emailClicked = ((TextView) view.findViewById(R.id.email)).getText().toString();

                Intent in = new Intent(getApplicationContext(), ProfileActivity.class);
                in.putExtra("email", emailClicked);

                startActivityForResult(in, 100);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    class LoadTutorsInformation extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONParser jsonParser = new JSONParser();

            final JSONObject json = jsonParser.makeHttpRequest(url_load_tutors, "POST", params);

            runOnUiThread(new Runnable() {
                //
                @Override
                public void run() {
                    int success;
                    try {
                        success = json.getInt("success");
                        if (success == 1) {

                            JSONArray jsonTutors = json.getJSONArray("tutors");

                            for(int i = 0; i < jsonTutors.length(); i++){
                                JSONObject t = jsonTutors.getJSONObject(i);

                                HashMap m = new HashMap<String,String>();
                                m.put("name", t.getString("name"));
                                m.put("email", t.getString("email"));
                                m.put("gender", t.getString("gender"));
                                m.put("picture", t.getString("picture"));
                                m.put("major", t.getString("major"));
                                m.put("price", t.getString("price"));
                                m.put("bio", t.getString("bio"));
                                m.put("experience", t.getString("experience"));
                                m.put("availability", t.getString("availability"));

                                tutors.add(m);
                            }

                        } else {

                        }
                    } catch (JSONException e) {
                        Log.d("Boogityboo", "bby");
                    }
                }
            });
            return null;
        }


        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {

                    ListAdapter adapter = new SimpleAdapter(
                            SearchActivity.this, tutors,
                            R.layout.tutor_list_item, new String[] {"email", "name", "price", "bio"},
                            new int[] { R.id.email,R.id.name,R.id.price,R.id.bio,});

                    setListAdapter(adapter);
                }
            });
        }
    }
}
