package com.example.kingpho.model;

public class UserFavourite {
    private int id;
    private User user;
    private Product product;
    private boolean isFavourite;

    public UserFavourite() {
    }

    public UserFavourite(int id, User user, Product product, boolean isFavourite) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.isFavourite = isFavourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
