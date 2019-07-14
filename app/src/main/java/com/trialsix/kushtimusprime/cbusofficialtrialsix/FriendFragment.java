package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EditText friendSearch;
    private ImageButton searchButton;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;
    private FloatingActionButton friendRefresh;
    private FirebaseAuth mAuth;
    private Button seePeopleRequestingMe;
    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_friend, container, false);
        friendSearch=view.findViewById(R.id.searchBar);
        searchButton=view.findViewById(R.id.searchButton);
        progressBar=view.findViewById(R.id.progressBarFriend);
        seePeopleRequestingMe=view.findViewById(R.id.seePeopleRequestingMe);
        mAuth=FirebaseAuth.getInstance();
        final String myUserID=mAuth.getUid();
        final RecyclerView rView=view.findViewById(R.id.friend_view);
        friendRefresh=view.findViewById(R.id.friendRefresh);
        friendRefresh.setVisibility(View.GONE);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        ArrayList<String> friends=(ArrayList<String>)task.getResult().get("friends");
                        RealFriendAdapter realFriendAdapter=new RealFriendAdapter(friends);
                        rView.setAdapter(realFriendAdapter);
                        //rView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                seePeopleRequestingMe.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                friendSearch.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                final String username=friendSearch.getText().toString();
                firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<DocumentSnapshot> dataPoint=task.getResult().getDocuments();
                            for(int i=dataPoint.size()-1;i>=0;i--) {
                                String aName=(String)dataPoint.get(i).get("name");
                                String up=aName.toUpperCase();
                                String low=aName.toLowerCase();
                                if(!up.contains(username.toUpperCase())&&!low.contains(username.toLowerCase())) {
                                    dataPoint.remove(i);
                                }
                                try {
                                    String theirName = (String) dataPoint.get(i).get("userID");
                                    if(theirName.equals(mAuth.getUid())) {
                                        dataPoint.remove(i);
                                    }
                                } catch(Exception e) {

                                }

                            }
                            TreeMap<Double,DocumentSnapshot> map=new TreeMap<>();

                            for(int j=0;j<dataPoint.size();j++) {
                                String fullname=(String)dataPoint.get(j).get("name");
                                double usernameDouble=username.length();
                                double fullnameDouble=fullname.length();
                                double similarityKey=usernameDouble/fullnameDouble;
                                map.put(similarityKey,dataPoint.get(j));
                            }
                            List<DocumentSnapshot> realStuff=new ArrayList<DocumentSnapshot>();
                            for (Map.Entry<Double,DocumentSnapshot> entry : map.entrySet()) {
                                DocumentSnapshot value = entry.getValue();
                                realStuff.add(value);
                            }
                            Collections.reverse(realStuff);
                            FriendAdapter friendAdapter=new FriendAdapter(realStuff,username);
                            rView.setLayoutManager(new LinearLayoutManager(getContext()));
                            rView.setAdapter(friendAdapter);
                            
                            friendRefresh.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            }
        });
        friendRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().detach(FriendFragment.this).attach(FriendFragment.this).commit();
            }
        });
        seePeopleRequestingMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent seePeopleRequestingMeIntent=new Intent(getContext(),SeePeopleRequestingMeActivity.class);
                firebaseFirestore.collection("Users").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                ArrayList<String> peopleRequestingMe=(ArrayList<String>)task.getResult().get("peopleRequestingMe");
                                seePeopleRequestingMeIntent.putExtra("peopleRequestingMe",peopleRequestingMe);
                                startActivity(seePeopleRequestingMeIntent);

                            }
                        }
                    }
                });
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
