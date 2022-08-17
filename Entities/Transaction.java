package com.example.bigfamilyv20.Entities;

import java.util.ArrayList;

public class Transaction {
    private static String type;
    private static int TId,TAmountPaid;
    private static ArrayList<Product> TransactionProducts;

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        Transaction.type = type;
    }

    public static int getTId() {
        return TId;
    }

    public static void setTId(int TId) {
        Transaction.TId = TId;
    }

    public static int getTAmountPaid() {
        return TAmountPaid;
    }

    public static void setTAmountPaid(int TAmountPaid) {
        Transaction.TAmountPaid = TAmountPaid;
    }

    public static ArrayList<Product> getTransactionProducts() {
        return TransactionProducts;
    }

    public static void setTransactionProducts(ArrayList<Product> transactionProducts) {
        TransactionProducts = transactionProducts;
    }
}
