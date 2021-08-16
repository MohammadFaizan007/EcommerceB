package com.inlog.ecommerce.model;

public class RedeemPoints {
    String shopname, points, worth;

    public RedeemPoints(String shopname, String points, String worth) {
        this.shopname = shopname;
        this.points = points;
        this.worth = worth;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getWorth() {
        return worth;
    }

    public void setWorth(String worth) {
        this.worth = worth;
    }
}
