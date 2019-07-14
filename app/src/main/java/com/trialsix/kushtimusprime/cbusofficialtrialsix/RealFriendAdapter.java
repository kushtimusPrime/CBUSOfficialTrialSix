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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class RealFriendAdapter extends RecyclerView.Adapter<RealFriendAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public ImageView image;
        public String userID;
        public Button deleteFriend;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name=itemView.findViewById(R.id.real_friend_name);
            image=itemView.findViewById(R.id.real_friend_image);
            deleteFriend=itemView.findViewById(R.id.delete_friend);
        }

        @Override
        public void onClick(final View view) {
            final Intent seeFriendActivityIntent = new Intent(view.getContext(), SeeFriendActivity.class);
            FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        if(task.getResult().exists()) {
                            ArrayList<String> eventsGoing=(ArrayList<String>)task.getResult().get("eventsGoing");
                            seeFriendActivityIntent.putExtra("eventsGoing",eventsGoing);
                            view.getContext().startActivity(seeFriendActivityIntent);

                        }
                    }
                }
            });
        }


    }

    private ArrayList<String> friends;
    private FirebaseAuth mAuth;
    private String myUserID;
    public RealFriendAdapter(ArrayList<String> friends) {
        this.friends=friends;
        mAuth=FirebaseAuth.getInstance();
        myUserID=mAuth.getUid();
    }
    @NonNull
    @Override
    public RealFriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context=viewGroup.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View mView=inflater.inflate(R.layout.item_real_friend,viewGroup,false);
        RealFriendAdapter.ViewHolder viewHolder=new RealFriendAdapter.ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RealFriendAdapter.ViewHolder viewHolder, final int i) {
        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        viewHolder.userID=friends.get(i);
        viewHolder.deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                firebaseFirestore.collection("Users").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Map<String,Object> userData=task.getResult().getData();
                                ArrayList<String> myFriends=(ArrayList<String>)userData.get("friends");
                                myFriends.remove(friends.get(i));
                                userData.put("friends",myFriends);
                                firebaseFirestore.collection("Users").document(myUserID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }

                    }
                });
                firebaseFirestore.collection("Users").document(friends.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                Map<String,Object> userData=task.getResult().getData();
                                ArrayList<String> theirFriends=(ArrayList<String>)userData.get("friends");
                                theirFriends.remove(myUserID);
                                userData.put("friends",theirFriends);
                                firebaseFirestore.collection("Users").document(myUserID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(view.getContext(),"Delete friend",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        firebaseFirestore.collection("Users").document(friends.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        String name=(String)task.getResult().get("name");
                        String image=(String)task.getResult().get("image");
                        viewHolder.name.setText(name);
                        ImageView imageTextView=viewHolder.image;
                        if(!image.equals("defaultUsed")) {
                            Glide.with(viewHolder.itemView.getContext()).load(image).into(imageTextView);
                        } else {
                            Glide.with(viewHolder.itemView.getContext()).load(R.drawable.profile_picture).into(imageTextView);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
