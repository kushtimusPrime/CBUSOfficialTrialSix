package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameOfFriend);
            image=itemView.findViewById(R.id.profileImageOfFriend);
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
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder viewHolder, int i) {
        DocumentSnapshot documentSnapshot=documentSnapshots.get(i);
        if(documentSnapshot.exists()) {
            String name=(String) documentSnapshot.get("name");
                String image = (String) documentSnapshot.get("image");
                TextView nameTextView = viewHolder.name;
                nameTextView.setText(name);
                ImageView imageTextView = viewHolder.image;
                if(!image.equals("defaultUsed")) {
                    Glide.with(viewHolder.itemView.getContext()).load(image).into(imageTextView);
                }

        }
    }

    @Override
    public int getItemCount() {
        return documentSnapshots.size();
    }


}
