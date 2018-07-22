package com.example.kushtimusprime.cbusofficialtrialsix;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{
    public List<BlogPost> blogPostList;
    public BlogRecyclerAdapter(List<BlogPost> blogPostList) {
        this.blogPostList=blogPostList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_list_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String descData=blogPostList.get(i).getDesc();
        viewHolder.setDescText(descData);
    }

    @Override
    public int getItemCount() {
        return blogPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView descView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setDescText(String descText) {
            descView=mView.findViewById(R.id.blogPostDescription);
            descView.setText(descText);
        }
    }
}
