package com.example.bigfamilyv20.Entities;

public class shopkeeper_status {
    private static boolean is_shopkeeper=false;
    private static String county;
    private static String subcounty;

    public static boolean isIs_shopkeeper() {
        return is_shopkeeper;
    }

    public static void setIs_shopkeeper(boolean is_shopkeeper) {
        shopkeeper_status.is_shopkeeper = is_shopkeeper;
    }

    public static String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public static String getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }
}
