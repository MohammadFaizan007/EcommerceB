package com.inlog.ecommerce.model;

import java.util.ArrayList;

public class Cart {
    private int id;
    private int product_id;
    private int product_variant_id;
    private String product_name;
    private String qty;
    private String price;
    private String subtotal;
    private String product_variant;
    private int shop_id;
    private String shop;
    private String type;
    private String image;

    private String taxIds;
    private String taxPercentage;
    private String amountTaxed;
    private String taxDescription;
    private boolean iscombo;
    static ArrayList<Cart> MyCard = new ArrayList<>();



    public Cart(int id, int product_id, int product_variant_id, String product_name, String qty, String price, String subtotal, String product_variant, int shop_id, String shop, String type, String image, ArrayList<Cart> MyCard,boolean iscombo) {
        this.id = id;
        this.product_id = product_id;
        this.product_variant_id = product_variant_id;
        this.product_name = product_name;
        this.qty = qty;
        this.price = price;
        this.subtotal = subtotal;
        this.product_variant = product_variant;
        this.shop_id = shop_id;
        this.shop = shop;
        this.type = type;
        this.image = image;
        this.MyCard = MyCard;
        this.iscombo = iscombo;
    }
    public Cart()
    {
        product_name = null;
        qty = null;
        price = null;
        subtotal = null;
        product_variant = null;
        shop = null;
        image = null;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_variant_id() {
        return product_variant_id;
    }

    public void setProduct_variant_id(int product_variant_id) {
        this.product_variant_id = product_variant_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQty() {
        return qty;
    }
    public int getQuntity() {
        try {
            return Integer.parseInt(qty);
        }catch (Exception exc){

        }
        return 1;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
    public void setQty(int qty) {
        this.qty = String.valueOf(qty);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getProduct_variant() {
        return product_variant;
    }

    public void setProduct_variant(String product_variant) {
        this.product_variant = product_variant;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public static ArrayList<Cart> getMyCard() {
        return MyCard;
    }

    public static void setMyCard(ArrayList<Cart> myCard) {
        MyCard = myCard;
    }
    public static void removeMyCard(int position)
    {
        MyCard.remove(position);
    }
    public Cart getCartItem(int position) {
        return MyCard.get(position);
    }

    public String getTaxIds() {
        return taxIds;
    }

    public void setTaxIds(String taxIds) {
        this.taxIds = taxIds;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getAmountTaxed() {
        return amountTaxed;
    }

    public void setAmountTaxed(String amountTaxed) {
        this.amountTaxed = amountTaxed;
    }

    public String getTaxDescription() {
        return taxDescription;
    }

    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    public boolean isIscombo() {
        return iscombo;
    }

    public void setIscombo(boolean iscombo) {
        this.iscombo = iscombo;
    }
}
