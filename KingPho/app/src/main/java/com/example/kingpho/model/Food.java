
package com.example.kingpho.model;

import java.io.Serializable;

public class Food implements Serializable {
    private String foodTitle;
    private String foodImage;
    private String foodDescription;
    private Double foodPrice;
    private int numberInCart;
    private Category category;

    private int rating;
    public Food(String foodTitle, String foodImage, String foodDescription, Double foodPrice, Category category) {
        this.foodTitle = foodTitle;
        this.foodImage = foodImage;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.category = category;
    }

    public Food(String foodTitle, String foodImage, String foodDescription, Double foodPrice, int numberInCart, Category category) {
        this.foodTitle = foodTitle;
        this.foodImage = foodImage;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.numberInCart = numberInCart;
        this.category = category;
    }

    public Food(String foodTitle, String foodImage, String foodDescription, Double foodPrice, int numberInCart, Category category, int rating) {
        this.foodTitle = foodTitle;
        this.foodImage = foodImage;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.numberInCart = numberInCart;
        this.category = category;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
