package com.example.android.quakereport;

public class Earthquake
{
    String mag;
    String place;
    String date;
    String time;
    String focus;
    String url;
    int back;
    Earthquake(String a,String b,String c,String d,String e,int f,String g){
        mag=a;
        place=b;
        date=c;
        time=d;
        focus=e;
        back=f;
        url=g;
    }
    String magnitude(){
        return mag;
    }
    String placeofoccur(){
        return place;
    }
    String dateofoccur(){
        return date;
    }
    String timeofoccur(){
        return time;
    }
    String focusofoccur(){
        return focus;
    }
    int background(){
        return back;
    }
    String getUrl()
    {
        return url;
    }

}
