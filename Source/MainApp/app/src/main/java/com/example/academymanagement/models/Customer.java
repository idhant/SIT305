package com.example.academymanagement.models;

public class Customer {

    // data to be stored when registering
    private String email;
    private String username;
    private int credits;
    //private String photo;

    public Customer() {}

    public Customer(String username, String email, int credits) {
        this.username = username;
        this.email = email;
        this.credits = credits;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername(){
        return username;
    }

    public int getCredits(){
        return credits;
    }
    public String setEmail(String email){
        this.email = email;
        String mes = "Your new email is:" + email;
        return mes;
    }
}
