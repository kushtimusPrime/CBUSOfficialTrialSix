package com.example.kushtimusprime.cbusofficialtrialsix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PinExpandedActivity extends AppCompatActivity {
    private EditText name;
    private EditText details;
    private EditText dateOfEvent;
    private EditText tickets;
    private EditText address;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_expanded);
        name=findViewById(R.id.name);
        details=findViewById(R.id.details);
        dateOfEvent=findViewById(R.id.dateOfEvent);
        tickets=findViewById(R.id.tickets);
        address=findViewById(R.id.address);
        image=findViewById(R.id.image);
        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            name.setText(extras.getString("title"));
            details.setText(extras.getString("description"));
            dateOfEvent.setText(extras.getString("date"));
            tickets.setText(extras.getString("tickets"));
            address.setText(extras.getString("address"));
            RequestOptions placeholderRequest=new RequestOptions();
            placeholderRequest.placeholder(R.drawable.profile_picture);
            Glide.with(PinExpandedActivity.this).setDefaultRequestOptions(placeholderRequest).load(extras.getString("imageUri")).into(image);
        }
    }
}
