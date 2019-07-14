package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_event, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String bPost = mBlogposts.get(i);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").document(bPost).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        BlogPost post=task.getResult().toObject(BlogPost.class);
                        TextView textView = viewHolder.title;
                        textView.setText(post.getTitle());
                        TextView textView2 = viewHolder.address;
                        textView2.setText(post.getAddress());
                        TextView textView3 = viewHolder.date;
                        textView3.setText(post.getDate());
                    }
                }
            }
        });
        // Set item views based on your views and data model

    }

    @Override
    public int getItemCount() {
        return mBlogposts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView address;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.text_view_title);
            address=itemView.findViewById(R.id.text_view_address);
            date=itemView.findViewById(R.id.text_view_date);
        }
    }

    private List<String> mBlogposts;
    private FirebaseFirestore firebaseFirestore;
    public EventAdapter(List<String> blogPosts) {
        mBlogposts=blogPosts;
    }

}