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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public ImageView image;
        public String userID;
        public Button friendRequest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name=itemView.findViewById(R.id.name_of_friend);
            image=itemView.findViewById(R.id.profile_image_of_friend);
            friendRequest=itemView.findViewById(R.id.friendRequest);
        }

        @Override
        public void onClick(View view) {
            Intent loginIntent = new Intent(view.getContext(), NewPostActivity.class);
            view.getContext().startActivity(loginIntent);
        }

        public void setUserID(String userID) {
            this.userID=userID;
        }

        public String getUserID() {
            return this.userID;
        }

    }

    private List<DocumentSnapshot> documentSnapshots;
    private String searched;

    public FriendAdapter(List<DocumentSnapshot> documentSnapshots,String searched) {
        this.documentSnapshots=documentSnapshots;
        this.searched=searched;
    }
    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context=viewGroup.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View mView=inflater.inflate(R.layout.item_friend,viewGroup,false);
        FriendAdapter.ViewHolder viewHolder=new FriendAdapter.ViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendAdapter.ViewHolder viewHolder, int i) {
        DocumentSnapshot documentSnapshot=documentSnapshots.get(i);
        if(documentSnapshot.exists()) {

            viewHolder.setUserID((String)documentSnapshot.get("userID"));

            String name=(String) documentSnapshot.get("name");
                String image = (String) documentSnapshot.get("image");
            final String theirUserID=viewHolder.getUserID();

            TextView nameTextView = viewHolder.name;
                nameTextView.setText(name);
                ImageView imageTextView = viewHolder.image;
                if(!image.equals("defaultUsed")) {
                    Glide.with(viewHolder.itemView.getContext()).load(image).into(imageTextView);
                } else {
                    Glide.with(viewHolder.itemView.getContext()).load(R.drawable.profile_picture).into(imageTextView);
                }
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            final String myUserID=mAuth.getUid();
            FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        if(task.getResult().exists()) {
                            ArrayList<String> peopleIRequest=(ArrayList<String>)task.getResult().get("peopleIRequest");
                            if(peopleIRequest.contains(theirUserID)) {
                                viewHolder.friendRequest.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
                viewHolder.friendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("Users").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    if(task.getResult().exists()) {
                                        Map<String,Object> userData=task.getResult().getData();
                                        ArrayList<String> peopleIRequest=(ArrayList<String>)userData.get("peopleIRequest");
                                        peopleIRequest.add(theirUserID);
                                        userData.put("peopleIRequest",peopleIRequest);
                                        firebaseFirestore.collection("Users").document(myUserID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        firebaseFirestore.collection("Users").document(theirUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    if(task.getResult().exists()) {
                                        Map<String,Object> userData=task.getResult().getData();
                                        ArrayList<String> peopleRequestingMe=(ArrayList<String>)userData.get("peopleRequestingMe");
                                        peopleRequestingMe.add(myUserID);
                                        userData.put("peopleRequestingMe",peopleRequestingMe);
                                        firebaseFirestore.collection("Users").document(theirUserID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(view.getContext(),"Friend request sent",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });

        }
    }

    @Override
    public int getItemCount() {
        return documentSnapshots.size();
    }


}
