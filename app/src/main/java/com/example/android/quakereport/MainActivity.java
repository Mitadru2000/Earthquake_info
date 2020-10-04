package com.example.android.quakereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String magnitude,year;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dots, menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        magnitude=sharedPreferences.getString("Min_mag_key","6");
        year=sharedPreferences.getString("year_key","2020");
        ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&activeNetwork.isConnectedOrConnecting();
        if(isConnected==false){

            LinearLayout t0=(LinearLayout) findViewById(R.id.load);
            t0.setVisibility(View.GONE);
            TextView t=(TextView)findViewById(R.id.empty);
            t.setVisibility(View.GONE);
            ListView l=(ListView)findViewById(R.id.list);
            l.setVisibility(View.GONE);
            TextView t5=(TextView)findViewById(R.id.no);
            t5.setVisibility(View.VISIBLE);
        }
        else{
        earthquakecheck earth=new earthquakecheck();
        earth.execute();}
    }
    private class earthquakecheck extends AsyncTask<String,Void,ArrayList<Earthquake>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LinearLayout t0=(LinearLayout) findViewById(R.id.load);
            t0.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Earthquake> doInBackground(String... urls) {
            QueryUtils q=new QueryUtils();
            final ArrayList<Earthquake> earthquakes =q.extractEarthquakes(year,magnitude);
            return earthquakes;
        }



        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {
            super.onPostExecute(earthquakes);
            LinearLayout t0=(LinearLayout) findViewById(R.id.load);
            t0.setVisibility(View.GONE);
            if(earthquakes==null){
                ListView earthquakeListView=(ListView)findViewById(R.id.list);
                earthquakeListView.setEmptyView((TextView)findViewById(R.id.empty));
                return;
            }
            updatescreen(earthquakes);
        }
    }
    private void updatescreen(final ArrayList<Earthquake> earthquakes){

        ListView earthquakeListView = (ListView) findViewById(R.id.list);


        EarthAdapter eat = new EarthAdapter(this,earthquakes);

        earthquakeListView.setEmptyView((TextView)findViewById(R.id.empty));
        earthquakeListView.setAdapter(eat);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake=earthquakes.get(position);
                Uri e=Uri.parse(earthquake.getUrl());
                Intent i=new Intent(Intent.ACTION_VIEW,e);
                startActivity(i);
            }
        });

    }
}