package com.example.bigfamilyv20.Entities;

import java.util.ArrayList;

public class ProductCard {

    public static String Pname,Pdescription,Pprice;
    public static int Pamount;

    public ProductCard(String pname, String pdescription, String pprice, int pamount) {
        this.Pname = pname;
       this.Pdescription = pdescription;
        this.Pprice = pprice;
        this.Pamount = pamount;
    }
    public ProductCard(String pname, String pdescription, String pprice) {
        this.Pname = pname;
        this.Pdescription = pdescription;
        this.Pprice = pprice;

    }

    public static ArrayList<ProductCard> CardPopulater(ArrayList<String> name,ArrayList<String> description,ArrayList<String> price,ArrayList<Integer> amount){
       ArrayList<ProductCard> list=new ArrayList<>();
        for (int i=0;i<name.size();i++){
            list.add(new ProductCard(name.get(i),description.get(i),price.get(i),amount.get(i)));

        }
        return list;
    }
    public static ArrayList<ProductCard> CardPopulater(ArrayList<String> name,ArrayList<String> description,ArrayList<String> price){
        ArrayList<ProductCard> list=new ArrayList<>();
        for (int i=0;i<name.size();i++){
            list.add(i,new ProductCard(name.get(i),description.get(i),price.get(i)));

        }
        return list;
    }

}
