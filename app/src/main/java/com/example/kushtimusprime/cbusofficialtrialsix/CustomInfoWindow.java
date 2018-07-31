package com.example.kushtimusprime.cbusofficialtrialsix;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view= ((Activity)context).getLayoutInflater().inflate(R.layout.custom_infowindow,null);
        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.image);

        TextView dateOfEvent_tv = view.findViewById(R.id.dateOfEvent);
        TextView tickets_tv = view.findViewById(R.id.tickets);
        TextView transport_tv = view.findViewById(R.id.transport);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());
        img.setImageResource(R.drawable.baseline_zoom_in_black_24dp);
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        dateOfEvent_tv.setText(infoWindowData.getDateOfEvent());
        tickets_tv.setText(infoWindowData.getTickets());
        transport_tv.setText(infoWindowData.getTransport());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_infowindow, null);



      /*  int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        img.setImageResource(imageId);*/



        return view;
    }
}

