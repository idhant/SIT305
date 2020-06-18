package com.example.academymanagement.models;

// Customer class to make interaction with the firebase database easier
// Stores the price, name, details

public class Rewards {

    private int price;
    private String name;
    private String details;

    public Rewards() {}

    public Rewards(int price, String name, String details){
        this.price = price;
        this.name = name;
        this.details = details;
    }

    public int getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }

    public String getDetails(){
        return details;
    }

}
