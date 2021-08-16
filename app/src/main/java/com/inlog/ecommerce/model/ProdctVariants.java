package com.inlog.ecommerce.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

public class ProdctVariants implements Serializable {
    String name;
    int productVariantId,cart_count;
    private ArrayList<ProductAttribute> attrValueList;
    static int selectedPosition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
    }

    public static int getSelected() {
        return selectedPosition;
    }

    public ArrayList<ProductAttribute> getAttrValueList() {
        return attrValueList;
    }

    public void setAttrValueList(ArrayList<ProductAttribute> attrValueList) {
        this.attrValueList = attrValueList;
    }

    public static void setSelected(int selected) {
        ProdctVariants.selectedPosition = selected;
    }

    public int getCart_count() {
        return cart_count;
    }

    public void setCart_count(int cart_count) {
        this.cart_count = cart_count;
    }
}
