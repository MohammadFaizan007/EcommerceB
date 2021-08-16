package com.inlog.ecommerce.utility;

import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.CartShopItem;

import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static CartManager obj=new CartManager();
    private HashMap<Integer, CartShopItem> cartList = new HashMap<>();
    private int totalQty;

    private CartManager(){}

    public static CartManager getInstance(){
        return obj;
    }

    public void addCartItem(Cart cartItem){
        CartShopItem cartShopItem = new CartShopItem();
        cartShopItem.setCart(cartItem);
        cartShopItem.setQty(cartItem.getQuntity());
        cartList.put(cartItem.getProduct_variant_id(),cartShopItem);
    }
    public void removeCartItem(Cart cartItem){
        cartList.remove(cartItem.getProduct_variant_id());
    }
    public boolean isCartItemAdded(Cart cartItem){
            return (cartList.containsKey(cartItem.getProduct_id()));
    }
    public boolean isCartItemAdded(int varient_id){
            return (cartList.containsKey(varient_id) /*|| WishlistManager.getInstance().isProductAdded(varient_id)*/);
    }

    public int getCartItemId(int productId){
        if(cartList.containsKey(productId))
            return cartList.get(productId).getCart().getId();
        return -1;
    }
    public Cart getCartItem(int productId){
        if(cartList.containsKey(productId))
            return cartList.get(productId).getCart();
        return null;
    }

    public HashMap<Integer, CartShopItem> getCartList() {
        return cartList;
    }
    public int getCartSize(){
        int total = 0;
        for (Map.Entry<Integer,CartShopItem> entry : cartList.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
            CartShopItem cartItem = entry.getValue();
            total+=cartItem.getQty();
        }
        return total;
    }

    public String getProductQty(int productid) {
        if(cartList.containsKey(productid)){
            if(cartList.get(productid).getCart().getQty()!=null)
            return cartList.get(productid).getCart().getQty();
            else return "0";
        }
        return "0";
    }

    public void setTotalQty(int total_qty) {
        totalQty = total_qty;
    }
    public int getTotalQty() {
        return totalQty;
    }
}

