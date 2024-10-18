package com.example.kingpho.model;

public class FoodActivity {
    private int foodId;
    private String foodTitle;
    private double foodPrice;
    private String foodImage;
    private int numberInCart;
    private boolean isFavourite; // Thêm trường isFavourite

    public FoodActivity(String foodTitle, double foodPrice, String foodImage) {
        this.foodTitle = foodTitle;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.numberInCart = 0;
        this.isFavourite = false; // Khởi tạo mặc định là false
        this.foodId = foodId;
    }
    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
