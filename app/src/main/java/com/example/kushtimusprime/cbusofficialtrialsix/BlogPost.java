package com.example.kushtimusprime.cbusofficialtrialsix;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class BlogPost {
    private String userID;
    private String imageUri;
    private String thumbUri;
    private String blogID;
    private String desc;
    private String date;
    private String tickets;
    private String transportation;
    private LatLng location;
    private String title;

    public BlogPost() {
    }

    public BlogPost(String userID, String imageUri, String thumbUri, String blogID, String desc, String date, String tickets, String transportation, LatLng location, String title) {
        this.userID = userID;
        this.imageUri = imageUri;
        this.thumbUri = thumbUri;
        this.blogID = blogID;
        this.desc = desc;
        this.date = date;
        this.tickets = tickets;
        this.transportation = transportation;
        this.location = location;
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public String getBlogID() {
        return blogID;
    }

    public void setBlogID(String blogID) {
        this.blogID = blogID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
