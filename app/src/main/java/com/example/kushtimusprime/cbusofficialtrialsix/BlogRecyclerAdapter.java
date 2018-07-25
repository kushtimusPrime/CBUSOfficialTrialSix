package com.example.kushtimusprime.cbusofficialtrialsix;

import android.content.Context;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
    public List<BlogPost> blogPostList=new ArrayList<>();
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private ViewGroup viewGroup;
    private static int blogPostListPastSize=0;
    public BlogRecyclerAdapter(List<BlogPost> blogPostLists) {
        Set<BlogPost> hs = new HashSet<>();
        hs.addAll(blogPostLists);
        blogPostLists.clear();
        blogPostLists.addAll(hs);
        this.blogPostList=blogPostLists;
       /* if(blogPostLists.size()!=blogPostListPastSize) {
            Context context= viewGroup.getContext();
            Activity activity=(Activity)context;
            blogPostListPastSize=blogPostList.size();
            activity.finish();
            activity.startActivity(new Intent(context,MainActivity.class));
        }*/
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.viewGroup=viewGroup;
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_list_item,viewGroup,false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        String blogUserID=blogPostList.get(i).getUserID();
        String currentUserID=mAuth.getCurrentUser().getUid();
        final String blogPostID=blogPostList.get(i).getBlogID();
        String descData=blogPostList.get(i).getDesc();
        viewHolder.setDescText(descData);
        String imageUrl = blogPostList.get(i).getImageUri();
        String thumbUrl=blogPostList.get(i).getThumbUri();
        viewHolder.setBlogImage(imageUrl,thumbUrl); //this variable might need to be changed to descData but it worked for him like this
        if(blogUserID.equals(currentUserID)) {
            viewHolder.deleteButton.setEnabled(true);
            viewHolder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.deleteButton.setEnabled(false);
            viewHolder.deleteButton.setVisibility(View.GONE);
        }
        String userID = blogPostList.get(i).getUserID();
        firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image"); //check these string names in firebase
                    viewHolder.setUserData(userName, userImage);
                } else {

                }
            }
        });
        //long millisecond = blogPostList.get(i).getTimestamp().getTime();
        //String dateString = DateFormat.format( "MM/dd/yyyy", new Date(millisecond)).toString();
        //viewHolder.setTime(dateString);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Posts").document(blogPostID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(context,"Post is deleted. Please refresh screen",Toast.LENGTH_LONG).show();
                                Set<BlogPost> hs = new HashSet<>();
                                hs.addAll(blogPostList);
                                blogPostList.clear();
                                blogPostList.addAll(hs);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogPostList.size();
    }
    public ViewHolder returnViewholder(View view) {
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;
        private TextView blogUserName;
        private ImageView blogUserImage;
        private Button deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            deleteButton=mView.findViewById(R.id.deleteButton);
        }
        public void setDescText(String descText) {
            descView=mView.findViewById(R.id.blogPostDescription);
            descView.setText(descText);
        }
        public void setBlogImage(String downloadUri,String thumbnailUri) {
            blogImageView = mView.findViewById(R.id.blogPostImage);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(Glide.with(context).load(thumbnailUri)).into(blogImageView);
        }
        public void setTime(String date){
            blogDate = mView.findViewById(R.id.blogPostDate);
            blogDate.setText(date);
        }
        public void setUserData(String name, String image){
            blogUserImage = mView.findViewById(R.id.blogUserImage);
            blogUserName = mView.findViewById(R.id.blogUsername);
            blogUserName.setText(name);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_picture);
            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(blogUserImage);
        }
    }
}
