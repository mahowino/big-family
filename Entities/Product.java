package com.example.bigfamilyv20.Entities;

public class Product {
    private static String productName,ProductDescription;
    private static int productAmount,productPrice,ProductCode;

    public static String getProductName() {
        return productName;
    }

    public static void setProductName(String productName) {
        Product.productName = productName;
    }

    public static String getProductDescription() {
        return ProductDescription;
    }

    public static void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public static int getProductAmount() {
        return productAmount;
    }

    public static void setProductAmount(int productAmount) {
        Product.productAmount = productAmount;
    }

    public static int getProductPrice() {
        return productPrice;
    }

    public static void setProductPrice(int productPrice) {
        Product.productPrice = productPrice;
    }

    public static int getProductCode() {
        return ProductCode;
    }

    public static void setProductCode(int productCode) {
        ProductCode = productCode;
    }
}
