package com.inlog.ecommerce.utility;

import com.inlog.ecommerce.model.Wishlist;

import java.util.HashMap;

public class WishlistManager {
    private static WishlistManager obj=new WishlistManager();
    private HashMap<Integer, Wishlist> wishlist = new HashMap<>();
    private WishlistManager(){
    }
    public static WishlistManager getInstance(){
        return obj;
    }
    public void addCartItem(Wishlist wishlist){
        this.wishlist.put(wishlist.getProduct_variant_id(),wishlist);
    }
    public int getSize(){
        return wishlist.size();
    }
    public HashMap<Integer, Wishlist> getWishlist(){
        return wishlist;
    }

    public Boolean isProductAdded(int productid) {
        return wishlist.containsKey(productid);
    }
}
