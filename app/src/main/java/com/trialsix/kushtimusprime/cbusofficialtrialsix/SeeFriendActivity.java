package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class SeeFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_friend);
        Bundle bundle=getIntent().getExtras();
        ArrayList<String> friends=(ArrayList<String>)bundle.get("eventsGoing");
        RecyclerView seeFriendRecyclerView=findViewById(R.id.see_friend_recycler_view);
        EventAdapter eventAdapter=new EventAdapter(friends);
        seeFriendRecyclerView.setAdapter(eventAdapter);
        seeFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
