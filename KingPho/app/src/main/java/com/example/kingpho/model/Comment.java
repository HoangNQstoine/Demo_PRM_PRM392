package com.example.kingpho.model;

public class Comment {
    private int imgReviewer;
    private String nameReviewer;
    private String emailReviewer;
    private double numberRating;
    private String contentReview;
    private String foodOrder;
    private String timeOrder;

    public Comment(int imgReviewer, String nameReviewer, String emailReviewer, double numberRating, String contentReview, String foodOrder, String timeOrder) {
        this.imgReviewer = imgReviewer;
        this.nameReviewer = nameReviewer;
        this.emailReviewer = emailReviewer;
        this.numberRating = numberRating;
        this.contentReview = contentReview;
        this.foodOrder = foodOrder;
        this.timeOrder = timeOrder;
    }

    public int getImgReviewer() {
        return imgReviewer;
    }

    public void setImgReviewer(int imgReviewer) {
        this.imgReviewer = imgReviewer;
    }

    public String getNameReviewer() {
        return nameReviewer;
    }

    public void setNameReviewer(String nameReviewer) {
        this.nameReviewer = nameReviewer;
    }

    public String getEmailReviewer() {
        return emailReviewer;
    }

    public void setEmailReviewer(String emailReviewer) {
        this.emailReviewer = emailReviewer;
    }

    public double getNumberRating() {
        return numberRating;
    }

    public void setNumberRating(double numberRating) {
        this.numberRating = numberRating;
    }

    public String getContentReview() {
        return contentReview;
    }

    public void setContentReview(String contentReview) {
        this.contentReview = contentReview;
    }

    public String getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(String foodOrder) {
        this.foodOrder = foodOrder;
    }

    public String getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(String timeOrder) {
        this.timeOrder = timeOrder;
    }
}
