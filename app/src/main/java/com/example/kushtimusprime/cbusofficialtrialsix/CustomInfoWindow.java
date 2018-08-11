package com.example.kushtimusprime.cbusofficialtrialsix;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private String classImageUri;
    private String classBigUri;

    public CustomInfoWindow(Context ctx){
        context = ctx;
    }
    public CustomInfoWindow(Context ctx,String bigUri,String imageUri){
        context = ctx;
        classBigUri=bigUri;
        classImageUri=imageUri;
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
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);
        Glide.with(context).applyDefaultRequestOptions(requestOptions).load(classBigUri).thumbnail(Glide.with(context).load(classImageUri)).into(img);

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

