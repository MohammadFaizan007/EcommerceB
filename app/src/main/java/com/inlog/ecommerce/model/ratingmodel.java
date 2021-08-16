package com.inlog.ecommerce.model;

public class ratingmodel {
    private int consumer_id,order_id,shop_id;
    private String name,create_date,description;
    private int avg_rating,delivery_rating,good_quality_rating,professional_rating,responsive_rating;
    public ratingmodel(int consumer_id, int order_id, int shop_id, String name, String create_date, String description, int avg_rating, int delivery_rating, int good_quality_rating, int professional_rating, int responsive_rating) {
        this.consumer_id = consumer_id;
        this.order_id = order_id;
        this.shop_id = shop_id;
        this.name = name;
        this.create_date = create_date;
        this.description = description;
        this.avg_rating = avg_rating;
        this.delivery_rating = delivery_rating;
        this.good_quality_rating = good_quality_rating;
        this.professional_rating = professional_rating;
        this.responsive_rating = responsive_rating;
    }
    public int getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(int consumer_id) {
        this.consumer_id = consumer_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(int avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getDelivery_rating() {
        return delivery_rating;
    }

    public void setDelivery_rating(int delivery_rating) {
        this.delivery_rating = delivery_rating;
    }

    public int getGood_quality_rating() {
        return good_quality_rating;
    }

    public void setGood_quality_rating(int good_quality_rating) {
        this.good_quality_rating = good_quality_rating;
    }

    public int getProfessional_rating() {
        return professional_rating;
    }

    public void setProfessional_rating(int professional_rating) {
        this.professional_rating = professional_rating;
    }

    public int getResponsive_rating() {
        return responsive_rating;
    }

    public void setResponsive_rating(int responsive_rating) {
        this.responsive_rating = responsive_rating;
    }

}
