package com.example.android.quakereport;
import android.content.Context;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.quakereport.Quake;
import com.example.android.quakereport.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class QuakeAdapter extends ArrayAdapter<Quake>  {


    public QuakeAdapter(Context context, ArrayList<Quake> earthquakes) {
        super(context, 0, earthquakes);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Quake currentWord = getItem(position);


        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        // Get the Miwok translation from the currentWord object and set this text on
        // the Miwok TextView.
        String formattedmag = formatMag(currentWord.getmMagnitude());
        magnitudeTextView.setText(formattedmag);
        GradientDrawable magCircle =(GradientDrawable) magnitudeTextView.getBackground();
        int backcolor = getMagnitudeColor(currentWord.getmMagnitude());
        magCircle.setColor(backcolor);


        // Find the TextView in the list_item.xml layout with the ID default_text_view.
        TextView location1TextView = (TextView) listItemView.findViewById(R.id.location1);
        // Get the default translation from the currentWord object and set this text on
        // the default TextView.
        location1TextView.setText(currentWord.getmLocation1());

        TextView location2TextView = (TextView) listItemView.findViewById(R.id.location2);
        // Get the default translation from the currentWord object and set this text on
        // the default TextView.
        location2TextView.setText(currentWord.getmLocation2());


        Date dateobj = new Date(currentWord.getmTime());

        TextView dateTextview = (TextView) listItemView.findViewById(R.id.date);
        dateTextview.setText(formatDate(dateobj));

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        timeTextView.setText(formatTime(dateobj));

        return listItemView;
    }
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private String formatMag(double magn){
        DecimalFormat magformat = new DecimalFormat("0.0");
        return magformat.format(magn);
    }
    private int getMagnitudeColor(double magnitude){
        int magnitudefloor = (int) Math.floor(magnitude);
        int magcolorResourceId;
        switch(magnitudefloor){
            case 0:
            case 1: magcolorResourceId = R.color.magnitude1;
            break;
            case 2:
                magcolorResourceId = R.color.magnitude2;
                break;
            case 3:
                magcolorResourceId = R.color.magnitude3;
                break;
            case 4:
                magcolorResourceId = R.color.magnitude4;
                break;
            case 5:
                magcolorResourceId = R.color.magnitude5;
                break;
            case 6:
                magcolorResourceId = R.color.magnitude6;
                break;
            case 7:
                magcolorResourceId = R.color.magnitude7;
                break;
            case 8:
                magcolorResourceId = R.color.magnitude8;
                break;
            case 9:
                magcolorResourceId = R.color.magnitude9;
                break;
            default:
                magcolorResourceId = R.color.magnitude10plus;

        }
        return ContextCompat.getColor(getContext(),magcolorResourceId);
    }
}