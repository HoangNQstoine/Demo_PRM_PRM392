package com.example.kingpho.model;

public class Order {
    private int imageResource;
    private String title;
    private String price;
    private String status;

    public Order(int imageResource, String title, String price, String status) {
        this.imageResource = imageResource;
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
