package com.inlog.ecommerce.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.activity.CartListActivity;
import com.inlog.ecommerce.activity.ItemDetailsActivity;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.categoryProduct;
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
import static com.inlog.ecommerce.fragments.ProductListingFragment.PRODUCT_DETAIL_EXTRA;

public class CartRecyclerViewAdapter
        extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    long lastClickTime = 0;
    public static ArrayList<Cart> productDetails;
    private final CartListActivity cartListActivityContext;
    private RecyclerView mRecyclerView;
    int cartidvalue;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final LinearLayout mLayoutItem, mLayoutRemove, mLayoutEdit;
        public final TextView textViewName, unitprice, subtotal, quantity, product_variant;
        private final CardView cardMinusQty;
        private final EditText txtVQuantity;
        private final CardView cardAddQty;
        private final CardView cardAddCartItemId;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_cartlist);
            mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
            mLayoutRemove = (LinearLayout) view.findViewById(R.id.layout_action1);
            mLayoutEdit = (LinearLayout) view.findViewById(R.id.layout_action2);
            textViewName = (TextView) view.findViewById(R.id.cardlist_name);
            unitprice = (TextView) view.findViewById(R.id.unitprice);
            subtotal = (TextView) view.findViewById(R.id.subtotal);
            quantity = (TextView) view.findViewById(R.id.quantity);
            product_variant = (TextView) view.findViewById(R.id.product_variant);
            txtVQuantity = view.findViewById(R.id.txtVQuantity);
            cardMinusQty = view.findViewById(R.id.cardMinusQty);
            cardAddQty = view.findViewById(R.id.cardAddQty);
            cardAddCartItemId = view.findViewById(R.id.cardAddCartItemId);
            txtVQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Cart cardItem = productDetails.get(getAdapterPosition());
                    cardItem.setQty(editable.toString());
                }
            });
        }
    }

    public CartRecyclerViewAdapter(RecyclerView recyclerView,
                                   ArrayList<Cart> listitem, CartListActivity cartListActivity) {
        mRecyclerView = recyclerView;
        productDetails = listitem;
        cartListActivityContext = cartListActivity;
    }

    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
        return new CartRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onViewRecycled(CartRecyclerViewAdapter.ViewHolder holder) {
//            if (holder.mImageView.getController() != null) {
//                holder.mImageView.getController().onDetach();
//            }
//            if (holder.mImageView.getTopLevelDrawable() != null) {
//                holder.mImageView.getTopLevelDrawable().setCallback(null);
////                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
//            }
    }

    @Override
    public void onBindViewHolder(final CartRecyclerViewAdapter.ViewHolder holder, final int position) {

        final Cart[] cardItem = {productDetails.get(position)};
        Picasso.with(cartListActivityContext).load(productDetails.get(position).getImage()).into(holder.mImageView);


        holder.textViewName.setText(productDetails.get(position).getProduct_name());
        holder.unitprice.setText(String.format("₹ %s", productDetails.get(position).getPrice()));
        holder.subtotal.setText(String.format("₹ %s", productDetails.get(position).getSubtotal()));
        holder.quantity.setText(String.format("Qty: %s", productDetails.get(position).getQty()));
        holder.product_variant.setText(productDetails.get(position).getProduct_variant());
        if (productDetails.get(position).isIscombo() == true)
        {
            holder.product_variant.setVisibility(View.GONE);
        }
        else
        {
            holder.product_variant.setVisibility(View.VISIBLE);
        }

        final String name = productDetails.get(position).getProduct_name();
        final String price = productDetails.get(position).getPrice();
        final String desc = "";

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cartListActivityContext, ItemDetailsActivity.class);
                intent.putExtra("productid",productDetails.get(position).getProduct_id());
                intent.putExtra("productvarientid",productDetails.get(position).getProduct_variant_id());
                intent.putExtra("currentcount", productDetails.get(position).getQty());
                intent.putExtra("checkroot",1);
                categoryProduct cateObj =  new categoryProduct();
                cateObj.setWishListAdded(false);
                cateObj.setQty( productDetails.get(position).getQty());
                cateObj.setProductid(productDetails.get(position).getProduct_id());
                cateObj.setProduct_variant_id(productDetails.get(position).getProduct_variant_id());
                cateObj.setShop_id(String.valueOf(productDetails.get(position).getShop_id()));
                cateObj.setShopName(productDetails.get(position).getShop());
                cateObj.setSale_price((int) Float.parseFloat(productDetails.get(position).getSubtotal()));
                intent.putExtra(PRODUCT_DETAIL_EXTRA,cateObj);

                cartListActivityContext.startActivity(intent);
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

                new ShowAlert(cartListActivityContext, cartListActivityContext.getString(R.string.app_name), cartListActivityContext.getString(R.string.are_your_suee_want_to_delete), cartListActivityContext.getString(R.string.yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cartListActivityContext instanceof CartListActivity) {
                            ((CartListActivity) cartListActivityContext).deleteCartItem(productDetails.get(position), position);
                        }
                    }
                }, cartListActivityContext.getString(R.string.no), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, true).show();
            }
        });

        //Set click action
        holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cartItem = productDetails.get(position);
                categoryProduct categoryProduct = new categoryProduct();
                categoryProduct.setProductid(cartItem.getProduct_id());
                categoryProduct.setSale_price(Integer.parseInt(cartItem.getPrice()));
                categoryProduct.setProduct_variant_id(cartItem.getProduct_variant_id());
                categoryProduct.setShopName(String.valueOf(cartItem.getShop()));
                categoryProduct.setQty(String.valueOf(cartItem.getQty()));
                categoryProduct.setShop_id(String.valueOf(cartItem.getShop_id()));
                hitApiToAddToCartOrWishList(categoryProduct, false,position);
            }
        });

        holder.txtVQuantity.setText(String.valueOf(Utility.strToInt(cardItem[0].getQty())));
        holder.txtVQuantity.setTag(position);

        holder.cardAddCartItemId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Utility.strToInt(cardItem[0].getQty());
                if (qty > 0) {
                    hitApiToEditCard(cardItem[0], CartRecyclerViewAdapter.this);
                }
            }
        });
        holder.cardMinusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Utility.strToInt(cardItem[0].getQty());
                if (qty > 1) {
                    cardItem[0].setQty(String.valueOf(--qty));
                    notifyDataSetChanged();
                }
            }
        });
        holder.cardAddQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Utility.strToInt(cardItem[0].getQty());
                cardItem[0].setQty(String.valueOf(++qty));
                notifyDataSetChanged();
            }
        });
    }


    private void hitApiToEditCard(Cart cartItem, CartRecyclerViewAdapter simpleStringRecyclerViewAdapter) {
        SharedPreferences sharedPrefs = cartListActivityContext.getSharedPreferences("shopidkey", 0);
        int shopid = sharedPrefs.getInt("shopid", 0);
        int categoryid = sharedPrefs.getInt("categoryid", 0);
        if (shopid == -1 || categoryid == -1)
            return;
        RequestQueue requestQueue = Volley.newRequestQueue(cartListActivityContext);
        try {
            MyProgressBar.getInstance().showProgressDialog(cartListActivityContext);
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

                    String responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            jsonObject = jsonObject.getJSONObject("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                Toast.makeText(cartListActivityContext, cartListActivityContext.getString(R.string.cart_has_been_updated), Toast.LENGTH_SHORT).show();
                                cartListActivityContext.getCartdata();
                            }
                            else
                                MyProgressBar.getInstance().dismissProgressDialog();

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

    private void hitApiToAddToCartOrWishList(categoryProduct categoryProduct, boolean isCart, int position) {
        SharedPreferences sharedPrefs = cartListActivityContext.getSharedPreferences("shopidkey", 0);
        int shopid = sharedPrefs.getInt("shopid", 0);
        int categoryid = sharedPrefs.getInt("categoryid", 0);

        SharedPreferences sharedPrefsLogin = cartListActivityContext.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
        int customerId = sharedPrefsLogin.getInt("globalid", 0);
        if (shopid == -1 || categoryid == -1)
            return;
        RequestQueue requestQueue = Volley.newRequestQueue(cartListActivityContext);
        try {
            MyProgressBar.getInstance().showProgressDialog(cartListActivityContext);
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.addToCart;
            HashMap<String, Object> paramSMap = new HashMap<String, Object>();
            HashMap<String, Object> paramSMapmain = new HashMap<String, Object>();
            paramSMap.put("clientId", GlobalVariables.clientID);
            paramSMap.put("consumerId", customerId);
            paramSMap.put("productId", categoryProduct.getProductid());
            paramSMap.put("productVariantId", categoryProduct.getProduct_variant_id());
            paramSMap.put("productName", categoryProduct.getName());
            if (isCart) {
                paramSMap.put("qty", categoryProduct.getQty());
            } else {
                    paramSMap.put("qty", 0);

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

                                    Toast.makeText(cartListActivityContext, cartListActivityContext.getString(R.string.added_into_cart), Toast.LENGTH_SHORT).show();

                                } else if (!isCart) {
                                    if (categoryProduct.getWishListAdded()) {
                                        categoryProduct.setWishListAdded(false);
                                        Toast.makeText(cartListActivityContext, cartListActivityContext.getResources().getString(R.string.item_removed_to_wishlist), Toast.LENGTH_SHORT).show();
                                    } else {
                                        categoryProduct.setWishListAdded(true);
                                        Toast.makeText(cartListActivityContext, cartListActivityContext.getResources().getString(R.string.item_added_to_wishlist), Toast.LENGTH_SHORT).show();
                                        cartListActivityContext.removeFromList(position);
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
    public int getItemCount() {
        return productDetails.size();
    }
}