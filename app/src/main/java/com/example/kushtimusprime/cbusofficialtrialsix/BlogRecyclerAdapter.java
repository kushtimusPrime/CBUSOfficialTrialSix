package com.example.kushtimusprime.cbusofficialtrialsix;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;
import com.bumptech.glide.Glide;
import java.util.Date;
import android.text.format.DateFormat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{
    public List<BlogPost> blogPostList;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    public BlogRecyclerAdapter(List<BlogPost> blogPostList) {
        this.blogPostList=blogPostList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_list_item,viewGroup,false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String descData=blogPostList.get(i).getDesc();
        viewHolder.setDescText(descData);
        String imageUrl = blogPostList.get(i).getImageUri();
        viewHolder.setBlogImage(imageUrl); //this variable might need to be changed to descData but it worked for him like this
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
        long millisecond = blogPostList.get(i).getTimestamp().getTime();
        String dateString = DateFormat.format( "MM/dd/yyyy", new Date(millisecond)).toString();
        viewHolder.setTime(dateString);
    }

    @Override
    public int getItemCount() {
        return blogPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;
        private TextView blogUserName;
        private CircleImageView blogUserImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setDescText(String descText) {
            descView=mView.findViewById(R.id.blogPostDescription);
            descView.setText(descText);
        }
        public void setBlogImage(String downloadUri) {
            blogImageView = mView.findViewById(R.id.blogPostImage);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).into(blogImageView);
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
