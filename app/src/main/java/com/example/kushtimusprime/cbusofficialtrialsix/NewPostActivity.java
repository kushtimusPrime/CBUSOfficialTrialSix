package com.example.kushtimusprime.cbusofficialtrialsix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class NewPostActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar newPostToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        newPostToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.newPostToolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add a new post");
    }
}
