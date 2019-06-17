package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class EventAdapter extends
        RecyclerView.Adapter<EventAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView addressTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.name);
            addressTextView = (TextView) itemView.findViewById(R.id.address);
        }
    }

    private List<BlogPost> mBlogPosts;

    // Pass in the contact array into the constructor
    public EventAdapter(List<BlogPost> blogPosts) {
        mBlogPosts = blogPosts;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_event, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    public void onBindViewHolder(EventAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        BlogPost blogPost = mBlogPosts.get(position);

        // Set item views based on your views and data model
        TextView name = viewHolder.nameTextView;
        TextView address=viewHolder.addressTextView;
        name.setText(blogPost.getTitle());
        address.setText(blogPost.getAddress());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mBlogPosts.size();
    }
}