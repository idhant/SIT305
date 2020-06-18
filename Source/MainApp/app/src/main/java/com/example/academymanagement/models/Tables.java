package com.example.academymanagement.models;

// Customer class to make interaction with the firebase database easier
// Stores the name, price and status of the table

public class Tables {

    private String tablename;
    private int tableprice;
    private boolean tablestatus;

    public Tables() {}

    public Tables(String tablename, int tableprice, boolean tablestatus) {
        this.tablename = tablename;
        this.tableprice = tableprice;
        this.tablestatus = tablestatus;
    }

    public String getTablename() {
        return tablename;
    }

    public int getTableprice() {
        return tableprice;
    }

    public boolean getTablestatus(){
        return tablestatus;
    }

//    public void setTableName(String tableName)
//    {
//        this.tableName = tableName;
//    }
//
//    public void setTablePrice(int tablePrice){
//        this.tablePrice = tablePrice;
//    }
//
//    public void setTableStatus(boolean tableStatus){
//        this.tableStatus = tableStatus;
//    }

}
