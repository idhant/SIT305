package com.example.academymanagement.models;

public class Customer {

    // data to be stored when registering
    private String lname;
    private String fname;
    private String email;
    private String username;
    private String credits;
    private String photo;

    public Customer() {}

    public Customer(String fname, String lname, String username, String email, String credits, String photo) {
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.email = email;
        this.credits = credits;
        this.photo = photo;
    }

    public String getLname() {
        return lname;
    }

    public String getFname() {
        return fname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getCredits(){
        return credits;
    }

    public String getPhoto(){
        return photo;
    }

    public String setEmail(String email){
        this.email = email;
        String mes = "Your new email is:" + email;
        return mes;
    }
}
