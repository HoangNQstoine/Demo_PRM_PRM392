package com.example.kingpho.model;

public class FoodItem {
    private String name;
    private String details;
    private int quantity;
    private double price;
    private int imageResId;

    public FoodItem(String name, String details, int quantity, double price, int imageResId) {
        this.name = name;
        this.details = details;
        this.quantity = quantity;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getDetails() { return details; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}

