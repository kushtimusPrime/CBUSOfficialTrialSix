package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class SeePeopleRequestingMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_people_requesting_me);
        Bundle peopleRequestingMeBundle=getIntent().getExtras();
        ArrayList<String> peopleRequestingMe=(ArrayList<String>) peopleRequestingMeBundle.get("peopleRequestingMe");
        Toast.makeText(getApplicationContext(),""+peopleRequestingMe,Toast.LENGTH_LONG).show();
    }
}
