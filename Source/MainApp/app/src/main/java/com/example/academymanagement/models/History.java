package com.example.academymanagement.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

// History class to make interaction with the firebase database easier
// Stores the time,category,change,useremail

public class History {

    @ServerTimestamp
    private Date time;

    private String category;
    private int change;
    private String user;

    public History() {}

    public History(Date time, String category, int change, String user){
        this.time = time;
        this.category = category;
        this.change = change;
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public int getChange() {
        return change;
    }

    public String getCategory() {
        return category;
    }

    public String getUser() {
        return user;
    }
}
