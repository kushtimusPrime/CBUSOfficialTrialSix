package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class PinExpandedActivity extends AppCompatActivity {
    private EditText name;
    private EditText details;
    private EditText dateOfEvent;
    private EditText tickets;
    private EditText address;
    private ImageView image;
    private CheckBox isGoing;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String blogID;
    private String userID;
    private ProgressBar setupBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_expanded);
        name=findViewById(R.id.nameOfFriend);
        details=findViewById(R.id.details);
        dateOfEvent=findViewById(R.id.dateOfEvent);
        firebaseAuth=FirebaseAuth.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();
        tickets=findViewById(R.id.tickets);
        address=findViewById(R.id.address);
        image=findViewById(R.id.image);
        isGoing=findViewById(R.id.checkBox);
        setupBar=findViewById(R.id.progressBar2);
        Bundle extras=getIntent().getExtras();
        firebaseFirestore=FirebaseFirestore.getInstance();
        if(extras!=null) {
            name.setText(extras.getString("title"));
            details.setText(extras.getString("description"));
            dateOfEvent.setText(extras.getString("date"));
            tickets.setText(extras.getString("tickets"));
            address.setText(extras.getString("address"));
            blogID=extras.getString("blogID");
            RequestOptions placeholderRequest=new RequestOptions();
            placeholderRequest.placeholder(R.drawable.profile_picture);
            Glide.with(PinExpandedActivity.this).setDefaultRequestOptions(placeholderRequest).load(extras.getString("imageUri")).into(image);
        }
        isGoing.setEnabled(false);
        firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        Map<String,Object> userData=task.getResult().getData();
                        ArrayList<String> events=(ArrayList<String>)userData.get("eventsGoing");
                        if(events.contains(blogID)) {
                            isGoing.setChecked(true);
                        }
                    }
                }
            }
        });
        isGoing.setEnabled(true);
      /*  isGoing.setEnabled(false);
        firebaseFirestore.collection("Posts").document(blogID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        final BlogPost aPost=task.getResult().toObject(BlogPost.class);
                        ArrayList<String> userIDs=aPost.getUsersGoing();
                        firebaseAuth=FirebaseAuth.getInstance();
                        String userID=firebaseAuth.getCurrentUser().getUid();
                        if(userIDs.contains(userID)) {
                            isGoing.setChecked(true);
                        }
                    }
                }
                setupBar.setVisibility(View.INVISIBLE);
            }
        });*/
        isGoing.setEnabled(true);
        isGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PinExpandedActivity.this,"User ID: "+userID,Toast.LENGTH_LONG).show();
                firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Map<String,Object> userData=task.getResult().getData();
                                ArrayList<String> events=(ArrayList<String>)userData.get("eventsGoing");
                                if(isGoing.isChecked()==true) {
                                    if(!events.contains(blogID)) {
                                        events.add(blogID);
                                    }
                                } else {
                                    if(events.contains(blogID)) {
                                        events.remove(blogID);
                                    }
                                }
                                userData.put("eventsGoing",events);
                                firebaseFirestore.collection("Users").document(userID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(PinExpandedActivity.this,"Done man",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
               /* firebaseFirestore.collection("Users").document(blogID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                final BlogPost aPost=task.getResult().toObject(BlogPost.class);
                                firebaseAuth=FirebaseAuth.getInstance();
                                String userID=firebaseAuth.getCurrentUser().getUid();

                                if(isGoing.isChecked()==true) {
                                    ArrayList<String> userIDs=aPost.getUsersGoing();
                                    if(!userIDs.contains(userID)) {
                                        userIDs.add(userID);
                                    }
                                    aPost.setUsersGoing(userIDs);
                                } else {
                                    ArrayList<String> userIDs=aPost.getUsersGoing();
                                    if(userIDs.contains(userID)) {
                                        userIDs.remove(userID);
                                    }
                                    aPost.setUsersGoing(userIDs);
                                }
                                String blogID=aPost.getBlogID();
                                firebaseFirestore.collection("Posts").document(blogID).set(aPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            }
                        }
                    }
                });*/
            }
        });
    }
}
