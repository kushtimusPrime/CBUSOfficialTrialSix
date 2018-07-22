package com.example.kushtimusprime.cbusofficialtrialsix;

import java.sql.Timestamp;

public class BlogPost {
    public String userID;
    public String imageUri;
    public String desc;
    public String imageThumb;

    public BlogPost(String userID, String imageUri, String desc, String imageThumb) {
        this.userID = userID;
        this.imageUri = imageUri;
        this.desc = desc;
        this.imageThumb = imageThumb;
    }
    public BlogPost() {

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




}
