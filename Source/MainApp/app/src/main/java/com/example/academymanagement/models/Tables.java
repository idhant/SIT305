package com.example.academymanagement.models;

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
