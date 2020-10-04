package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EarthAdapter extends ArrayAdapter<Earthquake> {

    EarthAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context,0,earthquakes);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View cust=convertView;
        if(cust == null) {
            cust = LayoutInflater.from(getContext()).inflate(R.layout.custom, parent, false);
        }
        Earthquake earth=getItem(position);
        TextView t1=(TextView)cust.findViewById(R.id.magnitude);
        t1.setText(earth.magnitude());
        TextView t2=(TextView)cust.findViewById(R.id.place);
        t2.setText(earth.placeofoccur());
        TextView t3=(TextView)cust.findViewById(R.id.date);
        t3.setText(earth.dateofoccur());
        TextView t4=(TextView)cust.findViewById(R.id.time);
        t4.setText(earth.timeofoccur());
        TextView t5=(TextView)cust.findViewById(R.id.focus);
        t5.setText(earth.focusofoccur());
        GradientDrawable g=(GradientDrawable)t1.getBackground();
        g.setColor(ContextCompat.getColor(getContext(),earth.background()));
        return cust;
    }
}
