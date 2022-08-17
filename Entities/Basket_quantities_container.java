package com.example.bigfamilyv20.Entities;

import java.util.ArrayList;
import java.util.List;

public class Basket_quantities_container {
    private static List<sending_data_structure> basket_items=new ArrayList<>();

    public Basket_quantities_container() {


    }

    public static List<sending_data_structure> getBasket_items() {
        return basket_items;
    }

    public  void setBasket_items(List<sending_data_structure> basket_items) {
        this.basket_items = basket_items;
    }
}
