package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;


public class QueryUtils {
    QueryUtils(){}
    public ArrayList<Earthquake> extractEarthquakes(String s1,String s2) {
        String Earthquake_url="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+s1+"-01-01&endtime="+s1+"-12-31&minmagnitude="+s2;//"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&endtime=2020-12-31&minmagnitude=6";
        String json_response="";
        String time="";
        String times="";

        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();
        URL url=null;
        try{
            url=new URL(Earthquake_url);
        }
        catch (MalformedURLException e) {
            Log.e("MainActivity","Incorrect url"+e);
        }
        try {
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setConnectTimeout(10000);
            http.setReadTimeout(15000);
            http.connect();
            if(http.getResponseCode()==200) {
                InputStream input = http.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(input, Charset.forName("UTF-8"));
                BufferedReader read=new BufferedReader(inputStreamReader);
                StringBuilder join=new StringBuilder();
                String line=read.readLine();
                while(line!=null){
                    join.append(line);
                    line=read.readLine();
                }
                json_response=join.toString();
                inputStreamReader.close();
                input.close();
            }
            http.disconnect();

        }
        catch (IOException e){
            Log.e("MainActivity","Could not connect"+e);
        }

        if(json_response.length()==0){
            return null;
        }
        try {
            int back=0;
         JSONObject root=new JSONObject(json_response);
         JSONArray feat=root.getJSONArray("features");
         for(int i=0;i<feat.length();i++)
         {
             JSONObject subfeat=feat.getJSONObject(i);
             JSONObject prop=subfeat.getJSONObject("properties");
             Double decmag=prop.getDouble("mag");
             switch ((int)Math.floor(decmag)){
                 case 0:
                 case 1:
                     back=R.color.magnitude1;
                     break;
                 case 2:
                     back=R.color.magnitude2;
                     break;
                 case 3:
                     back=R.color.magnitude3;
                     break;
                 case 4:
                     back=R.color.magnitude4;
                     break;
                 case 5:
                     back=R.color.magnitude5;
                     break;
                 case 6:
                     back=R.color.magnitude6;
                     break;
                 case 7:
                     back=R.color.magnitude7;
                     break;
                 case 8:
                     back=R.color.magnitude8;
                     break;
                 case 9:
                     back=R.color.magnitude9;
                     break;
                 case 10:
                     back=R.color.magnitude10plus;

             }
             DecimalFormat dec=new DecimalFormat("0.0");
             String mag=dec.format(decmag);
             String place=prop.getString("place");
             String focus="";
             String exactplace="";
             String urlgo=prop.getString("url");
             if(place.contains("of")){
                 int index=place.indexOf("of");
                 focus=place.substring(0,index+2);
                 exactplace=place.substring(index+2,place.length());
             }
             else{
                 exactplace=place;
                 focus="";
             }
             long utime=prop.getLong("time");
             time=getDate(utime);
             times=gettime(utime);
             earthquakes.add(new Earthquake(mag,exactplace,time,times,focus,back,urlgo));
         }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }
    private String getDate(Long a){
        Date unix=new Date(a);
        SimpleDateFormat time1=new SimpleDateFormat("MMM dd,yyyy");
        time1.setLenient(false);
        String s=time1.format(unix);
        return s;
    }
    private String gettime(Long b){
        Date unix=new Date(b);
        SimpleDateFormat time1=new SimpleDateFormat("hh:mm a");
        String s=time1.format(unix);
        return s;
    }


}