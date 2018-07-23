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

import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{
    public List<BlogPost> blogPostList;
    public Context context;
    public BlogRecyclerAdapter(List<BlogPost> blogPostList) {
        this.blogPostList=blogPostList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_list_item,viewGroup,false);
        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String descData=blogPostList.get(i).getDesc();
        viewHolder.setDescText(descData);
        String imageUrl = blogPostList.get(i).getImageUri();
        viewHolder.setBlogImage(imageUrl); //this variable might need to be changed to descData but it worked for him like this
        String user_id = blogPostList.get(i).getUserID();
        //user data will be retrieved here in the next tutorial haha he thought i would want a fun challenge
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
            Glide.with(context).load(downloadUri).into(blogImageView);
        }
        public void setTime(String date){
            blogDate = mView.findViewById(R.id.blogPostDate);
            blogDate.setText(date);
        }
    }
}
