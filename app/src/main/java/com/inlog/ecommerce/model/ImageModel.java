package com.inlog.ecommerce.model;

public class ImageModel {
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;
    int id;
}