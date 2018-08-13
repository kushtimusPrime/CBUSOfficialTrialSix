package com.example.kushtimusprime.cbusofficialtrialsix;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private EditText addressEditText;
    private Button addressButton;
    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;
    private boolean infoWindowIsShow = false;
    private Marker lastMarker;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ArrayList<BlogPost> blogPostList=new ArrayList<BlogPost>();

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_notification, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView=(MapView)mView.findViewById(R.id.map);
       // addressEditText=mView.findViewById(R.id.addressEditText);
        //addressButton=mView.findViewById(R.id.addressButton);
        zoomInButton=mView.findViewById(R.id.zoomInButton);
        zoomOutButton=mView.findViewById(R.id.zoomOutButton);
        if(mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth!=null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        Query realQuery=firebaseFirestore.collection("Posts").orderBy("blogID", Query.Direction.DESCENDING);
        realQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                try {
                    for(DocumentChange doc:documentSnapshots.getDocumentChanges()) {
                        if(doc.getType()==DocumentChange.Type.ADDED) {
                            DocumentReference documentReference = doc.getDocument().getReference();
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()) {
                                        BlogPost blogPost = documentSnapshot.toObject(BlogPost.class);
                                        if(!blogPostList.contains(blogPost)) {
                                            blogPostList.add(blogPost);
                                        }
                                        ArrayList<Double> points=getLocationFromAddress(blogPost.getAddress());
                                        try {
                                            LatLng marker = new LatLng(points.get(0), points.get(1));
                                            InfoWindowData newInfo = new InfoWindowData();
                                            newInfo.setDateOfEvent(blogPost.getDate()); //hotel and food were the defaults it gave but we can change
                                            newInfo.setTickets(blogPost.getTickets());
                                            CustomInfoWindow customInfoWindow = new CustomInfoWindow(getActivity(),blogPost.getImageUri(),blogPost.getThumbUri());
                                            mGoogleMap.setInfoWindowAdapter(customInfoWindow);
                                            Marker newMarker= mGoogleMap.addMarker(new MarkerOptions().position(marker).title(blogPost.getTitle()).snippet(blogPost.getDesc())
                                                    .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_AZURE)));
                                            newMarker.setTag(newInfo);
                                            float zoomLevel = 16.0f; //This goes up to 21
                                            // mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                                        } catch (NullPointerException e) {
                                           // Toast.makeText(getContext(),"Please type a REAL address",Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }
                            });
                        }
                    }
                }catch (Exception exception) {

                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float cityLevel=12.0f;
        LatLng columbus=new LatLng(39.9612,-82.9988);
        //info window tester stuff
       /* LatLng melsHouse = new LatLng(40.057613, -83.082275);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(melsHouse)
                .title("Mel's House")
                .snippet("This is where I live")
                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));
      InfoWindowData info = new InfoWindowData();
        //info.setImage("puppyforapp.jpg"); //idk if it can find this image? this is just the example
        //hgdsfgdsdgas
        info.setDateOfEvent("I am here every day"); //hotel and food were the defaults it gave but we can change
        info.setTickets("No tickets available");
        info.setTransport("Reach the site by bus, car and train.");
        CustomInfoWindow customInfoWindow = new CustomInfoWindow(this.getActivity());
        mGoogleMap.setInfoWindowAdapter(customInfoWindow);
        Marker mel = mGoogleMap.addMarker(markerOptions);
        mel.setTag(info);*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(columbus,cityLevel));
       /* addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(addressEditText.getText().toString())) {
                    String address=addressEditText.getText().toString();
                    ArrayList<Double> points=getLocationFromAddress(address);
                    try {
                        LatLng marker = new LatLng(points.get(0), points.get(1));
                        InfoWindowData newInfo = new InfoWindowData();
                        newInfo.setImage("puppyforapp.jpg"); //idk if it can find this image? this is just the example
                        newInfo.setDateOfEvent("I am here every day"); //hotel and food were the defaults it gave but we can change
                        newInfo.setTickets("No tickets available");
                        newInfo.setTransport("Reach the site by bus, car and train.");
                        CustomInfoWindow customInfoWindow = new CustomInfoWindow(getActivity());
                        mGoogleMap.setInfoWindowAdapter(customInfoWindow);
                        Marker newMarker= googleMap.addMarker(new MarkerOptions().position(marker).title("Deep dish Thursday").snippet("Secret in the sauce")
                                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_AZURE)));
                        newMarker.setTag(newInfo);
                        float zoomLevel = 16.0f; //This goes up to 21
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel));
                        // mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    } catch (NullPointerException e) {
                        Toast.makeText(getContext(),"Please type a REAL address",Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getContext(),"Please type an address",Toast.LENGTH_LONG).show();
                }
            }
        });*/
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });
       mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (lastMarker == null) {
                    marker.showInfoWindow();
                    lastMarker = marker;
                    infoWindowIsShow = true;
                } else if (marker.getId().equals(lastMarker.getId())) {
                    if (infoWindowIsShow) {
                        marker.hideInfoWindow();
                        infoWindowIsShow = false;
                    } else {
                        marker.showInfoWindow();
                        infoWindowIsShow = true;
                    }
                } else {
                    //это щелчок по другому маркеру
                    //предыдущего
                    if (infoWindowIsShow) {//если открыто инфовиндов предыдущего маркера, скрываем его
                        lastMarker.hideInfoWindow();
                        //и отображаем для нового
                        marker.showInfoWindow();
                        infoWindowIsShow = true;
                        lastMarker = marker;
                    } else {
                        marker.showInfoWindow();
                        infoWindowIsShow = true;
                        lastMarker = marker;
                    }
                }
                return true;
            }
        });
       mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
           @Override
           public void onInfoWindowClick(Marker marker) {
               for(int a=0;a<blogPostList.size();a++) {
                   if(marker.getTitle().equals(blogPostList.get(a).getTitle())) {
                       Intent expandIntent=new Intent(getActivity(),PinExpandedActivity.class);
                       expandIntent.putExtra("title", blogPostList.get(a).getTitle());
                       expandIntent.putExtra("description",blogPostList.get(a).getDesc());
                       expandIntent.putExtra("date",blogPostList.get(a).getDate());
                       expandIntent.putExtra("tickets",blogPostList.get(a).getTickets());
                       expandIntent.putExtra("address",blogPostList.get(a).getAddress());
                       expandIntent.putExtra("imageUri",blogPostList.get(a).getImageUri());
                       expandIntent.putExtra("thumbUri",blogPostList.get(a).getThumbUri());
                       startActivity(expandIntent);
                   }
               }
           }
       });
    }
    public ArrayList getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        ArrayList<Double> p1 = new ArrayList<>();

        try {
            try {
                address = coder.getFromLocationName(strAddress,5);
            } catch (Exception e) {
                address=new ArrayList<>();
            }
            if (address.size()==0) {
                return null;
            }
            Address location=address.get(0);
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            p1.clear();
            p1.add(latitude);
            p1.add(longitude);

            return p1;
        } catch (Exception e) {
            Toast.makeText(getContext(),"error: "+e.getMessage(),Toast.LENGTH_LONG).show();

        }
        return p1;
    }
}