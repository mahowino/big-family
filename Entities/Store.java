package com.example.bigfamilyv20.Entities;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private static String storeOwnerFirstName,StoreOwnerLastName;
    private static ArrayList<Product> storeItems;

    public Store(){
        storeItems=new ArrayList<>();
    }
    public Store(int PhoneNumber){
        storeItems=new ArrayList<>();
    }


    public static String getStoreOwnerFirstName() {
        return storeOwnerFirstName;
    }

    public static void setStoreOwnerFirstName(String storeOwnerFirstName) {
        Store.storeOwnerFirstName = storeOwnerFirstName;
    }

    public static String getStoreOwnerLastName() {
        return StoreOwnerLastName;
    }

    public static void setStoreOwnerLastName(String storeOwnerLastName) {
        StoreOwnerLastName = storeOwnerLastName;
    }

    public static List<Product> getStoreItems() {
        return storeItems;
    }

    public static void setStoreItems(ArrayList<Product> storeItems) {
        Store.storeItems = storeItems;
    }
    public static void emptyStore(){
        for(int x=0;x<storeItems.size();x++){
            storeItems.remove(x);
        }
        //submit the new empty list to database after this
    }
    public static void deleteStore(int phoneNumber){
        //get the store from the database
        //write mysql code to delete the store

    }
    public static void recieveItems(ArrayList<Product> items, int type, Context context){
        if(type==0){
            //add items to bulk store
        }
        else if(type==1){
            //add items to product store
        }
        else {
            Toast.makeText(context,"error getting items to store",Toast.LENGTH_LONG).show();
        }
        //push to database

    }
}
