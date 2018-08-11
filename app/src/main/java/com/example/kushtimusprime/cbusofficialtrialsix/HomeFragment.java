package com.example.kushtimusprime.cbusofficialtrialsix;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView blogPostView;
    private List<BlogPost> blogPosts = new ArrayList<BlogPost>();
    private FirebaseFirestore firebaseFirestore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private FirebaseAuth firebaseAuth;

    public List<BlogPost> getBlogPosts() {
        return blogPosts;
    }

    public void setBlogPosts(List<BlogPost> blogPosts) {
        this.blogPosts = blogPosts;
    }

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        blogPostView = view.findViewById(R.id.blogPostView);
        firebaseAuth = FirebaseAuth.getInstance();
        blogRecyclerAdapter = new BlogRecyclerAdapter(blogPosts);
        blogPostView.setLayoutManager(new LinearLayoutManager(getActivity()));
        blogPostView.setAdapter(blogRecyclerAdapter);
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
            blogPostView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);
                    if (reachedBottom) {
                        //theres supposed to be a toast here but it's causing trouble so i took it out
                        //loadMorePosts();
                        //:)
                    }
                }
            });
        Query firstQuery = firebaseFirestore.collection("Posts").orderBy("blogID", Query.Direction.DESCENDING).limit(5);
        firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(isFirstPageFirstLoad) {
                    if(documentSnapshots.size()>0) {
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    }

                }
                try {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            DocumentReference documentReference = doc.getDocument().getReference();
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()) {
                                        BlogPost blogPost = documentSnapshot.toObject(BlogPost.class);
                                        if (isFirstPageFirstLoad) {
                                            if (!blogPosts.contains(blogPost)) {
                                                blogPosts.add(blogPost);
                                            }
                                        } else {
                                            if (!blogPosts.contains(blogPost)) {
                                                blogPosts.add(0, blogPost);
                                            }
                                        }
                                        blogRecyclerAdapter.notifyDataSetChanged();
                                    }

                                }
                            });
                            //BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                            /**/
                        }
                    }
                } catch (Exception exception) {

                }

                    isFirstPageFirstLoad = false;


            }
            });
        }
        // Inflate the layout for this fragment
        return view;
    }
    //the method below is from part 14 and is supposed to take you to the next page since a set number of posts
    //which I set to 5 is displayed. we could either take this out or use it to rotate the pins if the user
    //wants to see other ones?
    public void loadMorePosts(){
        Query nextQuery = firebaseFirestore.collection("Posts").orderBy("blogID", Query.Direction.DESCENDING).startAfter(lastVisible).limit(5);
        nextQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) {
                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                            if(!blogPosts.contains(blogPost)) {
                                blogPosts.add(blogPost);
                            }
                                blogRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

}