package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * BlogPost
 *
 * The BlogPost class creates an object, which stores all of the data for an event post
 *
 * @author Kush Hari, Melanie Marszal
 * @version 1.0
 */
public class BlogPost {
    private String userID;
    private String imageUri;
    private String thumbUri;
    private String blogID;
    private String desc;
    private String date;
    private String tickets;
    private String address;
    private String title;
    private String category;
    private ArrayList<String> usersGoing;

    public BlogPost() {
    }

    public BlogPost(String userID, String imageUri, String thumbUri, String blogID, String desc, String date, String tickets, String address, String title, String category, ArrayList<String> usersGoing) {
        this.userID = userID;
        this.imageUri = imageUri;
        this.thumbUri = thumbUri;
        this.blogID = blogID;
        this.desc = desc;
        this.date = date;
        this.tickets = tickets;
        this.address = address;
        this.title = title;
        this.category = category;
        this.usersGoing=usersGoing;
       // convertToDate(date);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageUri() {
        //commenting
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public ArrayList<String> getUsersGoing() { return usersGoing; }

    public void setUsersGoing(ArrayList<String> usersGoing) { this.usersGoing = usersGoing; }

    public Date convertToDate(String theDate) {
        String month=theDate.substring(0,2);
        String day=theDate.substring(3,5);
        String year=theDate.substring(6);
        int theMonth=Integer.parseInt(month)-1;
        int theDay=Integer.parseInt(day);
        int theYear=Integer.parseInt(year)-1900;
        if(theMonth>=0&&theMonth<=11&&theDay>=1&&theDay<=31) {
            return(new Date(theYear, theMonth, theDay));
        }
        return null;
    }
}