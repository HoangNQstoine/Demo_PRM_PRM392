package com.example.kingpho.dto;

public class UserFavouriteDTO {

    private Integer id;

    private Integer userId;

    private Integer productId;
    private boolean favorite;

    public UserFavouriteDTO() {
    }

    // Constructor
    public UserFavouriteDTO(Integer userId, Integer productId, boolean favorite) {
        this.userId = userId;
        this.productId = productId;
        this.favorite = favorite;
    }

    // Getters and setters (can be generated automatically)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public boolean isFavourite() {
        return favorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.favorite = isFavorite;
    }
}