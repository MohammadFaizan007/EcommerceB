package com.inlog.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.adapter.wishlistRecyclerViewAdapter;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.Wishlist;
import com.inlog.ecommerce.model.categoryProduct;
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;
import com.inlog.ecommerce.utility.WishlistManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class WishlistActivity extends AppCompatActivity implements MyListCallbackListner {
    private static int customerId;
    public static int ADD_CART = 1,REMOVE_WISHLIST_ITEM=2;
    private Context mcontext;
    String responsefinal;
    JSONArray detailsarray;
    TextView txtVNoDataFound;
    public  ArrayList<Wishlist> productlist;
    RecyclerView recyclerViewId;
    private wishlistRecyclerViewAdapter wishListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mcontext = this;
        initViews();
        setupAdapter();
        initialize();
        getCartdata();
    }

    private void initViews() {
        txtVNoDataFound = findViewById(R.id.txtVNoDataFound);
        TextView header = findViewById(R.id.header);
        Button acaddressback = findViewById(R.id.acaddressback);
        acaddressback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        header.setText(getString(R.string.my_wish_list));
    }

    private void setupAdapter() {
        recyclerViewId = findViewById(R.id.recyclerViewId);
        recyclerViewId.setLayoutManager(new LinearLayoutManager(this));
        productlist = new ArrayList<>();
        wishListAdapter = new wishlistRecyclerViewAdapter(productlist,this,this);
        recyclerViewId.setAdapter(wishListAdapter);
    }

    private void initialize() {
        SharedPreferences sharedPrefsLogin = mcontext.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
        customerId = sharedPrefsLogin.getInt("globalid", 0);
    }

    public void getCartdata() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            SharedPreferences sharedPrefsLogin = mcontext.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
            customerId = sharedPrefsLogin.getInt("globalid", 0);
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerCart + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId + "&type=Wishlist&index=0&limit=100";
            Utility.log("URL = ", url);
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    MyProgressBar.getInstance().dismissProgressDialog();
                    responsefinal = null;
                    responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            detailsarray = new JSONArray();
                            JSONObject result = jsonObject.getJSONObject("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                detailsarray = result.getJSONArray("cart");
                                WishlistManager.getInstance().getWishlist().clear();
                                if (detailsarray.length() > 0) {
                                    productlist.clear();
                                    for (int i = 0; i < detailsarray.length(); i++) {
                                        JSONObject tempDict = (JSONObject) detailsarray.get(i);

                                        Wishlist cartItem = new Wishlist(tempDict.optInt("id"),
                                                tempDict.optInt("product_id"),
                                                tempDict.optInt("product_variant_id"),
                                                tempDict.optString("product_name"),
                                                tempDict.optString("qty"),
                                                tempDict.optString("price"),
                                                tempDict.optString("subtotal"),
                                                tempDict.optString("product_variant"),
                                                tempDict.optInt("shop_id"),
                                                tempDict.optString("shop"), tempDict.optString("type"),
                                                tempDict.optString("image"), productlist);
                                        productlist.add(cartItem);
                                        WishlistManager.getInstance().addCartItem(cartItem);
                                    }

                                    wishListAdapter.notifyDataSetChanged();

                                }
                                checkEmptyList(productlist);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        MyProgressBar.getInstance().dismissProgressDialog();
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

    private void checkEmptyList(ArrayList<Wishlist> productlist) {
        if(productlist!=null && productlist.size()>0){
            txtVNoDataFound.setVisibility(View.GONE);
            recyclerViewId.setVisibility(View.VISIBLE);
        }
        else {
            txtVNoDataFound.setVisibility(View.VISIBLE);
            recyclerViewId.setVisibility(View.GONE);
        }
    }

    private void hitApiToAddToCartOrWishList(categoryProduct categoryProduct, boolean isCart, int position) {
        SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", 0);
        int shopid = sharedPrefs.getInt("shopid", 0);
        int categoryid = sharedPrefs.getInt("categoryid", 0);

        SharedPreferences sharedPrefsLogin = getSharedPreferences("globalloginvalues", MODE_PRIVATE);
        int customerId = sharedPrefsLogin.getInt("globalid", 0);
        if (shopid == -1 || categoryid == -1)
            return;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.addToCart;
            HashMap<String, Object> paramSMap = new HashMap<String, Object>();
            HashMap<String, Object> paramSMapmain = new HashMap<String, Object>();
            paramSMap.put("clientId", GlobalVariables.clientID);
            paramSMap.put("consumerId", customerId);
            paramSMap.put("productId", categoryProduct.getProductid());
            paramSMap.put("productVariantId", categoryProduct.getProduct_variant_id());
            paramSMap.put("productName", categoryProduct.getName());
            if (isCart) {
                if (categoryProduct.getQty() == 0)
                {
                    paramSMap.put("qty", 1);
                }
                else
                {
                    paramSMap.put("qty", categoryProduct.getQty());
                }

            } else {
                    paramSMap.put("qty", 1);
            }
            paramSMap.put("price", categoryProduct.getSale_price());
            if (categoryProduct.getProductvarient() != null && categoryProduct.getProductvarient().size() > 0)
                paramSMap.put("productVariant", categoryProduct.getProductvarient().get(categoryProduct.getSelectedproductVrientPostion()).getName());
            else
                paramSMap.put("productVariant", "");
            paramSMap.put("shopId", categoryProduct.getShop_id());
            paramSMap.put("shopName", categoryProduct.getShopName());
            if (isCart)
                paramSMap.put("type", "Cart");
            else
                paramSMap.put("type", "Wishlist");

            paramSMapmain.put("params", paramSMap);

            JSONObject requetBody = new JSONObject(paramSMapmain);
            Utility.log("URL = ", url);
            Utility.log("Request body  = ", requetBody.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requetBody, new Response.Listener<JSONObject>() {
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
                                if (isCart) {
                                    if(productlist.size()>position)
                                    productlist.remove(position);
                                    Toast.makeText(WishlistActivity.this, getString(R.string.added_into_cart), Toast.LENGTH_SHORT).show();
                                    wishListAdapter.notifyDataSetChanged();
                                    checkEmptyList(productlist);
                                } else if (!isCart) {
                                    if (categoryProduct.getWishListAdded()) {
                                        categoryProduct.setWishListAdded(false);
                                        Toast.makeText(WishlistActivity.this, getResources().getString(R.string.item_removed_to_wishlist), Toast.LENGTH_SHORT).show();
                                    } else {
                                        categoryProduct.setWishListAdded(true);
                                        Toast.makeText(WishlistActivity.this, getResources().getString(R.string.item_added_to_wishlist), Toast.LENGTH_SHORT).show();
                                    }
                                }
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
    public void onItemClick(int position, int tag) {
        if(tag == ADD_CART){
            Wishlist cart = new Wishlist();
            Wishlist cartItem = cart.getCartItem(position);
            categoryProduct categoryProduct = new categoryProduct();
            categoryProduct.setProductid(cartItem.getProduct_id());
            categoryProduct.setSale_price(Integer.parseInt(cartItem.getPrice()));
            categoryProduct.setProduct_variant_id(cartItem.getProduct_variant_id());
            categoryProduct.setShopName(String.valueOf(cartItem.getShop()));
            categoryProduct.setQty(String.valueOf(cartItem.getQty()));
            categoryProduct.setShop_id(String.valueOf(cartItem.getShop_id()));
            hitApiToAddToCartOrWishList(categoryProduct,true,position);
        }
        else if(tag == REMOVE_WISHLIST_ITEM) {
            Wishlist cart = new Wishlist();
            Wishlist cartItem = cart.getCartItem(position);
            deleteCartItem(cartItem.getId(),position);
        }
    }
    public void deleteCartItem(int id, int position)
    {
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            new DeleteCartitem(id,position).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class DeleteCartitem extends AsyncTask<String, Void, String> {
        int cartid,pos;


        public DeleteCartitem(int id,int poss) {
            cartid = id;
            pos = poss;

        }

        protected void onPreExecute() {

        }

        @SuppressLint("WrongThread")
        protected String doInBackground(String... arg0) {
            return deletedata();
        }

        @Override
        protected void onPostExecute(String resultFinal) {
            super.onPostExecute(resultFinal);
            MyProgressBar.getInstance().dismissProgressDialog();
            if (resultFinal != null) {
                try {
                    JSONObject jsonObject = new JSONObject(resultFinal);
                    JSONObject result = jsonObject.getJSONObject("result");
                    if (result.optString("status").equalsIgnoreCase("OK")){
                                Toast.makeText(WishlistActivity.this,
                                        result.optString("result"),
                                        Toast.LENGTH_LONG).show();
                              if(productlist.size()>pos) {
                                  productlist.remove(pos);
                                  wishListAdapter.notifyDataSetChanged();
                              }
                        checkEmptyList(productlist);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String deletedata() {
            try {

                URL url = new URL(GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.deleteFromCart);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                JSONObject mainobj = new JSONObject();
                JSONObject childobj = new JSONObject();
                childobj.put("clientId", GlobalVariables.clientID);
                childobj.put("cartId", cartid);
                mainobj.put("params", childobj);
                mainobj.put( "type", "Wishlist");

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                bw.write(mainobj.toString());
                bw.flush();
                bw.close();


                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {

                    return new String("false : " + responseCode);

                }
            } catch (Exception e) {

                return new String("Exception: " + e.getMessage());

            }
        }
    }
}