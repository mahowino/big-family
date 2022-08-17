package com.example.bigfamilyv20.Entities;

import java.util.ArrayList;
import java.util.List;

public class namesArray {
    private static List<Long> productId;
    private static List<Integer> amount;
    private static List<String> name,price,description;
    private static List<String> docIds;

    public namesArray() {
        productId=new ArrayList<>();
        amount=new ArrayList<>();
        name=new ArrayList<>();
        price=new ArrayList<>();
        description=new ArrayList<>();
        docIds=new ArrayList<>();
    }

    public static List<String> getDocIds() {
        return docIds;
    }

    public static void setDocIds(List<String> docIds) {
        namesArray.docIds = docIds;
    }

    public static List<Long> getProductId() {
        return productId;
    }

    public static void setProductId(List<Long> productId) {
        namesArray.productId = productId;
    }

    public static List<Integer> getAmount() {
        return amount;
    }

    public static void setAmount(List<Integer> amount) {
        namesArray.amount = amount;
    }

    public static List<String> getName() {
        return name;
    }

    public static void setName(List<String> name) {
        namesArray.name = name;
    }

    public static List<String> getPrice() {
        return price;
    }

    public static void setPrice(List<String> price) {
        namesArray.price = price;
    }

    public static List<String> getDescription() {
        return description;
    }

    public static void setDescription(List<String> description) {
        namesArray.description = description;
    }
}
