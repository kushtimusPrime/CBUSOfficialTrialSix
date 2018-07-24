package com.example.kushtimusprime.cbusofficialtrialsix;

import java.sql.Timestamp;
import java.util.Date;

public class BlogPost {
    public String userID;
    public String imageUri;
    public String desc;
    public String imageThumb;
    public Date timestamp;

    //sets everything in the layout when viewing a blog post
    public BlogPost(String userID, String imageUri, String desc, String imageThumb, Date timestamp) {
        this.userID = userID;
        this.imageUri = imageUri;
        this.desc = desc;
        this.imageThumb = imageThumb;
        this.timestamp = timestamp;
    }

    public BlogPost() {
        //empty required public constructor
    }

    //getter and setter methods for all components of blog posts
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}