package com.inlog.ecommerce.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.Wishlist;
import com.inlog.ecommerce.model.categoryProduct;
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.ShowAlert;
import com.inlog.ecommerce.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.inlog.ecommerce.activity.WishlistActivity.ADD_CART;
import static com.inlog.ecommerce.activity.WishlistActivity.REMOVE_WISHLIST_ITEM;

public class wishlistRecyclerViewAdapter
        extends RecyclerView.Adapter<wishlistRecyclerViewAdapter.ViewHolder> {
    private  MyListCallbackListner listnerCallBack;
    long lastClickTime = 0;
    public  ArrayList<Wishlist> productDetails;
    private Activity activityContext;

    public wishlistRecyclerViewAdapter(ArrayList<Wishlist> productlist, Activity mcontext, MyListCallbackListner listnerCallBack) {
        this.productDetails = productlist;
        this.listnerCallBack = listnerCallBack;
        activityContext = mcontext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final LinearLayout mLayoutRemove, mLAddToCart;
        public final TextView textViewName, unitprice, product_variant;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_cartlist);
            mLayoutRemove = (LinearLayout) view.findViewById(R.id.layout_action1);
            mLAddToCart = (LinearLayout) view.findViewById(R.id.layout_action2);
            textViewName = (TextView) view.findViewById(R.id.cardlist_name);
            unitprice = (TextView) view.findViewById(R.id.unitprice);
            product_variant = (TextView) view.findViewById(R.id.product_variant);
        }
    }


    @Override
    public wishlistRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishlist_item, parent, false);
        return new wishlistRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onViewRecycled(wishlistRecyclerViewAdapter.ViewHolder holder) {
//            if (holder.mImageView.getController() != null) {
//                holder.mImageView.getController().onDetach();
//            }
//            if (holder.mImageView.getTopLevelDrawable() != null) {
//                holder.mImageView.getTopLevelDrawable().setCallback(null);
////                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
//            }
    }

    @Override
    public void onBindViewHolder(final wishlistRecyclerViewAdapter.ViewHolder holder, final int position) {

        final Wishlist[] cardItem = {productDetails.get(position)};
        Picasso.with(activityContext).load(productDetails.get(position).getImage()).into(holder.mImageView);

        holder.textViewName.setText(productDetails.get(position).getProduct_name());
        holder.unitprice.setText(String.format("₹ %s", productDetails.get(position).getPrice()));
        holder.product_variant.setText(productDetails.get(position).getProduct_variant());

        final String name = productDetails.get(position).getProduct_name();
        final String price = productDetails.get(position).getPrice();
        final String desc = "";

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //Set click action
        holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1500) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                new ShowAlert(activityContext, activityContext.getString(R.string.app_name), activityContext.getString(R.string.are_your_suee_want_to_delete), activityContext.getString(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listnerCallBack.onItemClick(position,REMOVE_WISHLIST_ITEM);
                    }
                }, activityContext.getString(R.string.no), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, true).show();
            }
        });

        //Set click action
        holder.mLAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listnerCallBack.onItemClick(position,ADD_CART);
//                hitApiToAddToCartOrWishList(categoryProduct, true);
            }
        });

    }


    private void hitApiToEditCard(Cart cartItem, wishlistRecyclerViewAdapter simpleStringRecyclerViewAdapter) {
        SharedPreferences sharedPrefs = activityContext.getSharedPreferences("shopidkey", 0);
        int shopid = sharedPrefs.getInt("shopid", 0);
        int categoryid = sharedPrefs.getInt("categoryid", 0);
        if (shopid == -1 || categoryid == -1)
            return;
        RequestQueue requestQueue = Volley.newRequestQueue(activityContext);
        try {
            MyProgressBar.getInstance().showProgressDialog(activityContext);
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.editCart;
            HashMap<String, Object> paramSMap = new HashMap<String, Object>();
            HashMap<String, Object> paramSMapmain = new HashMap<String, Object>();
            paramSMap.put("clientId", GlobalVariables.clientID);
            paramSMap.put("cartId", cartItem.getId());
            paramSMap.put("productId", cartItem.getProduct_id());
            paramSMap.put("qty", cartItem.getQty());
            paramSMap.put("type", "Cart");

            paramSMapmain.put("params", paramSMap);
            JSONObject requetBody = new JSONObject(paramSMapmain);
            Utility.log("URL = ", url);
            Utility.log("Request body  = ", requetBody.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requetBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    MyProgressBar.getInstance().dismissProgressDialog();
                    String responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            jsonObject = jsonObject.getJSONObject("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                Toast.makeText(activityContext, activityContext.getString(R.string.cart_has_been_updated), Toast.LENGTH_SHORT).show();
//                                activityContext.getCartdata();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyProgressBar.getInstance().dismissProgressDialog();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressBar.getInstance().dismissProgressDialog();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return productDetails.size();
    }
}