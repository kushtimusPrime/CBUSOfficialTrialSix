package com.example.kushtimusprime.cbusofficialtrialsix;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView blogPostView;
    private List<BlogPost> blogPosts=new ArrayList<BlogPost>();
    private FirebaseFirestore firebaseFirestore;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        blogPostView=view.findViewById(R.id.blogPostView);
        blogRecyclerAdapter=new BlogRecyclerAdapter(blogPosts);
        blogPostView.setLayoutManager(new LinearLayoutManager(getActivity()));
        blogPostView.setAdapter(blogRecyclerAdapter);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for(DocumentChange doc:documentSnapshots.getDocumentChanges()) {
                    if(doc.getType()==DocumentChange.Type.ADDED) {
                        BlogPost blogPost=doc.getDocument().toObject(BlogPost.class);
                        blogPosts.add(blogPost);
                        blogRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
