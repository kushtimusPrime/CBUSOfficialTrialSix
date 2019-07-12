package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Friend {
    private String name;
    private String userID;

    public Friend(String name, String userID) {
        this.name = name;
        this.userID = userID;
    }

    public Friend(List<DocumentSnapshot> dataPoint) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
