package com.example.bigfamilyv20.Entities;

import java.util.ArrayList;

public class Cart {
    private static ArrayList<Product> cartItems;
    private static int cartId;
    private static int storeId;

    public static ArrayList<Product> getCartItems() {
        return cartItems;
    }

    public static void setCartItems(ArrayList<Product> cartItems) {
        Cart.cartItems = cartItems;
    }

    public static int getCartId() {
        return cartId;
    }

    public static void setCartId(int cartId) {
        Cart.cartId = cartId;
    }

    public static int getStoreId() {
        return storeId;
    }

    public static void setStoreId(int storeId) {
        Cart.storeId = storeId;
    }
}
