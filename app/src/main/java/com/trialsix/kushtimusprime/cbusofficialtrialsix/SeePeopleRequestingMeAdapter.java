package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class SeePeopleRequestingMeAdapter extends RecyclerView.Adapter<SeePeopleRequestingMeAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePictureOfFriendRequest;
        public TextView nameOfFriendRequest;
        public Button acceptRequest;
        public Button declineRequest;
        public String userID;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePictureOfFriendRequest=itemView.findViewById(R.id.profile_picture_of_friend_request);
            nameOfFriendRequest=itemView.findViewById(R.id.name_of_friend_request);
            acceptRequest=itemView.findViewById(R.id.accept_request);
            declineRequest=itemView.findViewById(R.id.decline_request);
        }
    }

    private ArrayList<String> peopleRequestingMe;
    private FirebaseAuth mAuth;
    private String myUserID;

    public SeePeopleRequestingMeAdapter(ArrayList<String> peopleRequestingMe) {
        this.peopleRequestingMe = peopleRequestingMe;
        mAuth=FirebaseAuth.getInstance();
        myUserID=mAuth.getUid();
    }

    @NonNull
    @Override
    public SeePeopleRequestingMeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context=viewGroup.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View mView=inflater.inflate(R.layout.item_friend_request,viewGroup,false);
        SeePeopleRequestingMeAdapter.ViewHolder viewHolder=new SeePeopleRequestingMeAdapter.ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SeePeopleRequestingMeAdapter.ViewHolder viewHolder, final int i) {
        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(this.peopleRequestingMe.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        String image=task.getResult().getString("image");
                        String name=task.getResult().getString("name");
                        viewHolder.nameOfFriendRequest.setText(name);
                        ImageView imageTextView = viewHolder.profilePictureOfFriendRequest;
                        if(!image.equals("defaultUsed")) {
                            Glide.with(viewHolder.itemView.getContext()).load(image).into(imageTextView);
                        } else {
                            Glide.with(viewHolder.itemView.getContext()).load(R.drawable.profile_picture).into(imageTextView);
                        }

                    }
                }
            }
        });
        viewHolder.acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                firebaseFirestore.collection("Users").document(peopleRequestingMe.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Map<String,Object> userData=task.getResult().getData();
                                ArrayList<String> peopleIRequest=(ArrayList<String>) userData.get("peopleIRequest");
                                peopleIRequest.remove(myUserID);
                                userData.put("peopleIRequest",peopleIRequest);
                                ArrayList<String> friends=(ArrayList<String>) userData.get("friends");
                                friends.add(myUserID);
                                userData.put("friends",friends);
                                firebaseFirestore.collection("Users").document(peopleRequestingMe.get(i)).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }
                    }
                });
                firebaseFirestore.collection("Users").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Map<String,Object> userData=task.getResult().getData();
                                ArrayList<String> peopleRequestingMeFire=(ArrayList<String>) userData.get("peopleRequestingMe");
                                peopleRequestingMeFire.remove(peopleRequestingMe.get(i));
                                userData.put("peopleRequestingMe",peopleRequestingMeFire);
                                ArrayList<String> friends=(ArrayList<String>) userData.get("friends");
                                friends.add(myUserID);
                                userData.put("friends",friends);
                                firebaseFirestore.collection("Users").document(myUserID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        viewHolder.declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                firebaseFirestore.collection("Users").document(peopleRequestingMe.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Map<String,Object> userData=task.getResult().getData();
                                ArrayList<String> peopleIRequest=(ArrayList<String>) userData.get("peopleIRequest");
                                peopleIRequest.remove(myUserID);
                                userData.put("peopleIRequest",peopleIRequest);
                                firebaseFirestore.collection("Users").document(peopleRequestingMe.get(i)).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }
                    }
                });
                firebaseFirestore.collection("Users").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Map<String,Object> userData=task.getResult().getData();
                                ArrayList<String> peopleRequestingMeFire=(ArrayList<String>) userData.get("peopleRequestingMe");
                                peopleRequestingMeFire.remove(peopleRequestingMe.get(i));
                                userData.put("peopleRequestingMe",peopleRequestingMeFire);
                                firebaseFirestore.collection("Users").document(myUserID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return peopleRequestingMe.size();
    }
}
