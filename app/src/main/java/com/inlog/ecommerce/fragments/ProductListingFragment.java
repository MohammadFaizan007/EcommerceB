/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inlog.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.activity.ProductListActivity;
import com.inlog.ecommerce.activity.ItemDetailsActivity;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.adapter.ProductvarientsAdapter;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.ProdctVariants;
import com.inlog.ecommerce.model.ProductAttribute;
import com.inlog.ecommerce.model.Wishlist;
import com.inlog.ecommerce.model.categoryProduct;
import com.inlog.ecommerce.model.searchbrand;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.CartManager;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;
import com.inlog.ecommerce.utility.WishlistManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProductListingFragment extends Fragment {

    public static int SELECTED_PRODUCTS_INDEX = -1,SELECTED_PRODUCTS_QTY=-1,SELECTED_PRODUCTS_VARI_ID=-1;
    public static final String STRING_IMAGE_URI = "ImageUri";
    public static final String PRODUCT_DETAIL_EXTRA = "productDetail";
    public static final String STRING_IMAGE_POSITION = "ImagePosition";
    private static ProductListActivity mActivity;
    public static ArrayList<categoryProduct> productlist;

    List<Integer> categoryidarray = new ArrayList<Integer>();
    JSONArray productlistarray = null;
    JSONArray attr_array = null;
    private static int shopid =-1,categoryid =-1;
    private JSONArray filteredBrand,filteredAttributs/*,filteredSizeAttributs*/;
    private int filteredMinPrice = 0,filteredMaxPrice = -1;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private List<ProdctVariants> productvarient = new ArrayList<ProdctVariants>();
    private SimpleStringRecyclerViewAdapter productListAdapter;
    private int pageIndex = 0;
    private boolean isForNextPageData = false;
    private static int customerId;
    private int totalCount=-1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SELECTED_PRODUCTS_INDEX = -1;
        SELECTED_PRODUCTS_QTY=-1;
        mActivity = (ProductListActivity) getActivity();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mActivity instanceof SimpleStringRecyclerViewAdapter.Oncartcountlistner) {
            SimpleStringRecyclerViewAdapter.Oncartcountlistner mListener = (SimpleStringRecyclerViewAdapter.Oncartcountlistner) mActivity;
            if(mListener!=null && isVisibleToUser)
                mListener.Oncartcountlistner(CartManager.getInstance().getCartSize());
//            productListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (productListAdapter!=null) {
            if(SELECTED_PRODUCTS_INDEX!=-1 && SELECTED_PRODUCTS_QTY!=-1 && SELECTED_PRODUCTS_VARI_ID!=-1 && productlist!=null && productlist.size()>SELECTED_PRODUCTS_INDEX){
                for (ProdctVariants  obj: productlist.get(SELECTED_PRODUCTS_INDEX).getProductvarient()) {
                    if(obj.getProductVariantId() == SELECTED_PRODUCTS_VARI_ID) {
                        obj.setCart_count(SELECTED_PRODUCTS_QTY);
                        break;
                    }

                }
//                productlist.get(SELECTED_PRODUCTS_INDEX).setSelectedProductVariantQTY((SELECTED_PRODUCTS_QTY));
            }
            productListAdapter.notifyDataSetChanged();
            SELECTED_PRODUCTS_INDEX = -1;
            SELECTED_PRODUCTS_QTY=-1;
            SELECTED_PRODUCTS_VARI_ID=-1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView  recyclerView = (RecyclerView) inflater.inflate(R.layout.layout_recylerview_list, container, false);
        initialize();
        initAdapter(recyclerView);
        getProductList();
        return recyclerView;
    }

    private void initialize() {
        SharedPreferences sharedPrefs = getContext().getSharedPreferences("shopidkey", 0);
        shopid =  sharedPrefs.getInt("shopid",0);
        categoryid =  sharedPrefs.getInt("categoryid",0);
        filteredBrand =  new JSONArray();
        filteredAttributs =  new JSONArray();
//        filteredSizeAttributs =  new JSONArray();

        SharedPreferences sharedPrefsLogin = getContext().getSharedPreferences("globalloginvalues", MODE_PRIVATE);
        customerId = sharedPrefsLogin.getInt("globalid",0);
    }

    private void initAdapter(RecyclerView recyclerView) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        productlist =  new ArrayList<>();
        productListAdapter =  new SimpleStringRecyclerViewAdapter(recyclerView, productlist);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productListAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
//                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//                    pastVisiblesItems = layoutManager.findFirstCompletelyVisibleItemPositions();
                    int[] firstVisibleItemPositions = new int[2];
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItemPositions)[0];
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && totalCount>totalItemCount) {
                            isForNextPageData = true;
                            loading = false;
                            pageIndex = pageIndex + 1 + 20;
                            getProductList();
                        }
                    }
                }
            }
        });

    }


    public void getProductList(){
        if(shopid == -1 || categoryid == -1)
            return;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        try {
            categoryidarray.clear();
            categoryidarray.add(categoryid);
            MyProgressBar.getInstance().showProgressDialog(getContext());
//            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getProductListmethod+"?clientId="+GlobalVariables.clientID+"&shopId=" +shopid+"&categId="+categoryidarray+ "&index="+0+"&limit="+1000+"&attributeValueIds=[]&priceMin=0&priceMax=&productBrandIds=";

            String maxPrice = "&priceMax=";
            if(filteredMaxPrice>0)
                maxPrice = maxPrice+filteredMaxPrice;
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getProductListmethod+"?clientId="+GlobalVariables.clientID+"&shopId=" +shopid+"&categId="+categoryidarray+ "&index="+pageIndex+"&limit="+20+"&attributeValueIds="+filteredAttributs+"&priceMin="+filteredMinPrice+maxPrice+"&productBrandIds="+filteredBrand+"&consumerId="+customerId;
            Utility.log("URL = ", url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    loading = true;
                    String responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            productlistarray = new JSONArray();
                            attr_array = new JSONArray();
                            if(!isForNextPageData)
                            productlist.clear();
                            productlistarray = jsonObject.getJSONArray("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                totalCount = jsonObject.optInt("totalCount");
                                if (productlistarray.length() > 0)
                                {
                                    for (int i = 0; i < productlistarray.length(); i++) {
                                        JSONObject tempDict = (JSONObject) productlistarray.get(i);
                                        attr_array = tempDict.getJSONArray("json_agg");

                                        productvarient = new ArrayList<ProdctVariants>();
                                        for (int j = 0; j < attr_array.length(); j++) {
                                            JSONObject varientDict = (JSONObject) attr_array.get(j);
                                            ProdctVariants varients =  new ProdctVariants();
                                            varients.setProductVariantId(varientDict.optInt("ProductVariantId"));
                                            varients.setName(varientDict.optString("attr_string"));
                                            varients.setCart_count(varientDict.optInt("cart_count"));
                                            JSONArray attrValArray = varientDict.getJSONArray("attr_val");
                                           ArrayList<ProductAttribute> attributeValue = new ArrayList<>();
                                            for (int k = 0;k<attrValArray.length();k++){

                                                JSONObject attrJsonOBJ = attrValArray.optJSONObject(k);
                                                ProductAttribute attrObj = new ProductAttribute();
                                                attrObj.setId(attrJsonOBJ.optInt("id"));
                                                attrObj.setLabel(attrJsonOBJ.optString("Name"));
                                                attrObj.setName(attrJsonOBJ.optString("Name"));
                                                attributeValue.add(attrObj);
                                            }
                                            varients.setAttrValueList(attributeValue);
                                            productvarient.add(varients);
                                        }

                                        categoryProduct productObj = new categoryProduct(tempDict.getInt("product_id"),
                                                tempDict.optString("name"),
                                                "",
                                                tempDict.optInt("mrp"),
                                                tempDict.optInt("sale_price"),
                                                tempDict.optString("image"),
                                                tempDict.optInt("product_variant_id"),
                                                productvarient,
                                                tempDict.optString("shop"), tempDict.optString("shop_id"),
                                                tempDict.optInt("discount"),
                                                tempDict.optBoolean("is_combo")
                                        );
                                        productObj.setWishListAdded(WishlistManager.getInstance().isProductAdded(productObj.getProduct_variant_id()));
                                        productObj.setQty(tempDict.optString("cart_count"));
                                        productlist.add(productObj);

                                    }

                                }
                                else {
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                    Toast.makeText( getContext(),getString(R.string.noproductsfound) , Toast.LENGTH_SHORT).show();
                                }
                                  MyProgressBar.getInstance().dismissProgressDialog();
                                productListAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        MyProgressBar.getInstance().dismissProgressDialog();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading = true;
                    MyProgressBar.getInstance().dismissProgressDialog();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // adapter to be changed.
    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        static Oncartcountlistner mListener;
        private RecyclerView mRecyclerView;
        private List<categoryProduct> productdetials;
        private int lastPosition = -1;
        List<String> varientname = new ArrayList<String>();
        public class ViewHolder extends RecyclerView.ViewHolder implements com.inlog.ecommerce.fragments.ViewHolder, View.OnClickListener {

            public final View mView;
            public final LinearLayout mLayoutItem;
            public final ImageView mImageViewWishlist;
            public final TextView textView;
            //            public final TextView textViewDesc;
            public final TextView textviewsaleprice;
            public final TextView textViewbasePrice;
            public final TextView discount;

            public final ImageView mImageView;
            public final EditText prnumber;
            public final CardView less,more,addcart;
            public boolean checktype;
            private int lastPosition = -1;
            private int minteger = 0;
            private int tempminteger = 0;
            private int currentinteger = 0;
            public final Spinner itemspinner;
            private String strprno;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image1);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item);
                mImageViewWishlist = (ImageView) view.findViewById(R.id.ic_wishlist);
                textView = (TextView) view.findViewById(R.id.list_item_name);
//                textViewDesc = (TextView) view.findViewById(R.id.list_item_Desc);
                textviewsaleprice = (TextView) view.findViewById(R.id.list_item_price);
                textViewbasePrice = (TextView) view.findViewById(R.id.list_item_sale_price);
                discount = (TextView) view.findViewById(R.id.discount);
                prnumber = (EditText) view.findViewById(R.id.prnumber);
                less = (CardView) view.findViewById(R.id.less);
                more = (CardView) view.findViewById(R.id.more);
                addcart = (CardView) view.findViewById(R.id.addcart);

                itemspinner = (Spinner) view.findViewById(R.id.itemspinner);
//                less.setBackgroundResource(R.drawable.rounded_corner);
//                more.setBackgroundResource(R.drawable.rounded_corner);

                if (mActivity instanceof Oncartcountlistner) {
                    mListener = (Oncartcountlistner) mActivity;
                }

                prnumber.setText("0");
                minteger = 0;
                tempminteger = 0;
                currentinteger = 0;
                more.setTag(R.integer.btn_plus_view, itemView);
                less.setTag(R.integer.btn_minus_view, itemView);
                addcart.setTag(R.integer.btn_addcart_pos, itemView);

                mImageViewWishlist.setTag(R.integer.btn_wishlist_pos, itemView);
                more.setOnClickListener(this);
                less.setOnClickListener(this);
//                addcart.setOnClickListener(this);
//                mImageViewWishlist.setOnClickListener(this);



            }

            public int getQuantity(final ViewHolder holder) {
                return Integer.parseInt(holder.prnumber.getText().toString());
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (view.getId() == more.getId())
                {
                    View tempview = (View) more.getTag(R.integer.btn_plus_view);

                    minteger ++;
//                    prnumber.setText(String.valueOf(minteger));
                    Integer position = (Integer) view.getTag();
                    int qty = productlist.get(position).getSelectedProductVariantQTY();
                    qty++;
                    productlist.get(position).setQty(String.valueOf(qty));
                    productlist.get(position).setSelectedProductVariantQTY((qty));
                    notifyDataSetChanged();


                }
                else if(view.getId() == less.getId())
                {
                    View tempview = (View) less.getTag(R.integer.btn_minus_view);
                    if (prnumber.getText().toString().equals("0"))
                    {
                        return;
                    }
                    else
                    {
                        -- minteger;

                        Integer position = (Integer) view.getTag();
                        int qty = productlist.get(position).getSelectedProductVariantQTY();
                        if(qty>0) {
                            qty--;
                            productlist.get(position).setQty(String.valueOf(qty));
                            productlist.get(position).setSelectedProductVariantQTY((qty));
                            notifyDataSetChanged();
                        }

                    }
                }
                else if(view.getId() == addcart.getId())
                {

                    if (mListener != null)
                    {
                        mListener.Oncartcountlistner(CartManager.getInstance().getCartSize());
                    }
                }
                else if (view.getId() == mImageViewWishlist.getId())
                {

                    if (mImageViewWishlist.getDrawable().getConstantState() == mActivity.getResources().getDrawable( R.drawable.ic_favorite_black_18dp).getConstantState())
                    {
                        mImageViewWishlist.setImageResource(R.drawable.ic_favorite_border_black_18dp);
//                    notifyDataSetChanged();
                        Toast.makeText(mActivity,"Item removed from  Wishlist.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mImageViewWishlist.setImageResource(R.drawable.ic_favorite_black_18dp);
//                    notifyDataSetChanged();
                        Toast.makeText(mActivity,"Item added to Wishlist.",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }


        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, List<categoryProduct> products) {
            mRecyclerView = recyclerView;
            productdetials = products;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onViewRecycled(ViewHolder holder) {

        }
        public interface Oncartcountlistner {
            void Oncartcountlistner(int count);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Uri uri = Uri.parse(productdetials.get(position).getImageurl());
            Picasso.with(mActivity).load(uri).into( holder.mImageView);
            ProductvarientsAdapter adapter = new ProductvarientsAdapter(productdetials.get(position).getProductvarient(),mActivity);
            holder.itemspinner.setAdapter(adapter);
            holder.itemspinner.setSelection(productdetials.get(position).getSelectedproductVrientPostion());
            holder.itemspinner.setTag(position);
            holder.itemspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                    try {
                        if(parent.getTag() instanceof  Integer) {
                            Integer tagPosition = (Integer) parent.getTag();
                            productdetials.get(tagPosition).setSelectedproductVrientPostion(position);
                            holder.prnumber.setText(String.valueOf(productdetials.get(tagPosition).getSelectedProductVariantQTY()));
                        }
                    }catch (Exception ex){
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            holder.textView.setText(productdetials.get(position).getName());
            holder.textviewsaleprice.setText("₹ "+String.valueOf(productdetials.get(position).getSale_price()));
            holder.textViewbasePrice.setText("₹ "+String.valueOf(productdetials.get(position).getBase_price()));
            holder.textViewbasePrice.setPaintFlags(holder.textViewbasePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            holder.discount.setText(String.format("%d%% Offer", productdetials.get(position).getDiscount()));

            if (productdetials.get(position).getDiscount() == 0)
            {
                holder.discount.setVisibility(View.GONE);
            }
            else
            {
                holder.discount.setVisibility(View.VISIBLE);
            }
            holder.checktype = false;
            lastPosition = position;
            final String name = productdetials.get(position).getName();
            final int saleprice = productdetials.get(position).getSale_price();
            final int baseprice = productdetials.get(position).getBase_price();
            final String desc = productdetials.get(position).getDesc();
            holder.less.setTag(position);
            holder.more.setTag(position);
            holder.mLayoutItem.setTag(position);
            holder.itemspinner.setTag(position);
            holder.mImageViewWishlist.setTag(position);

            if (productdetials.get(position).isIs_combo() == true)
            {
                holder.itemspinner.setVisibility(View.GONE);
            }
            else
            {
                holder.itemspinner.setVisibility(View.VISIBLE);
            }
            lastPosition = position;

            /*ArrayAdapter<ProdctVarients> adapter = new ArrayAdapter<ProdctVarients>(mActivity.getApplicationContext(),
                    android.R.layout.simple_spinner_item, productdetials.get(position).getProductvarient());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

*/
             holder.prnumber.setTag(position);
//            holder.prnumber.setText(String.valueOf(productdetials.get(position).getQty()));
            holder.prnumber.setText(String.valueOf(productdetials.get(position).getSelectedProductVariantQTY()));
            holder.prnumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int positionLocal = (int) holder.prnumber.getTag();
                    productdetials.get(positionLocal).setQty(holder.prnumber.getText().toString());
                    productdetials.get(positionLocal).setSelectedProductVariantQTY(Utility.strToInt(holder.prnumber.getText().toString()));
                }
            });

            holder.addcart.setTag(position);
            holder.addcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tagIndex = (int) view.getTag();
                    if (tagIndex < productdetials.size())

                        Log.i("sdasdasd", holder.prnumber.getText().toString());
                    if (holder.prnumber.getText().toString().equals("0")) {
                        Toast.makeText(mActivity, mActivity.getString(R.string.please_add_quantity), Toast.LENGTH_SHORT).show();
                        return;
                    }
                   /* if (CartManager.getInstance().isCartItemAdded(productdetials.get(tagIndex).getProduct_variant_id())) {
                        hitApiToEditCard(productdetials.get(tagIndex));
                    } else*/{
                        hitApiToAddToCartOrWishList(productdetials.get(tagIndex), holder.addcart.getContext(), true);
                }
                }
            });

            if(productdetials.get(position).getWishListAdded()){
                holder.mImageViewWishlist.setImageResource(R.drawable.ic_favorite_black_18dp);
            }else {
                holder.mImageViewWishlist.setImageResource(R.drawable.ic_favorite_border_black_18dp);
            }

            holder.mImageViewWishlist.setTag(position);
            holder.mImageViewWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tagIndex = (int) view.getTag();
                    if(tagIndex<productdetials.size())
                        hitApiToAddToCartOrWishList(productdetials.get(tagIndex),holder.addcart.getContext(),false);

                }
            });
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tagIndex = (int) v.getTag();
//                    Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
                    SELECTED_PRODUCTS_INDEX = tagIndex;
                    SELECTED_PRODUCTS_QTY = productdetials.get(position).getQty();
                    SELECTED_PRODUCTS_VARI_ID = productdetials.get(position).getSelectedProductVariant().getProductVariantId();
                   /* intent.putExtra("productid",productdetials.get(tagIndex).getProductid());
                    intent.putExtra("checkroot",0);
                    intent.putExtra("currentcount", holder.prnumber.getText().toString());
                    intent.putExtra(PRODUCT_DETAIL_EXTRA, productdetials.get(position));
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
*/
                    ItemDetailsActivity.startActivity(mActivity,productdetials.get(tagIndex).getProductid(),0,holder.prnumber.getText().toString(),productdetials.get(position));

                }
            });


        }
        private void hitApiToEditCard(categoryProduct cartItem) {
            SharedPreferences sharedPrefs = mActivity.getSharedPreferences("shopidkey", 0);
            int shopid = sharedPrefs.getInt("shopid", 0);
            int categoryid = sharedPrefs.getInt("categoryid", 0);
            if(shopid == -1 || categoryid == -1)
                return;
            RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
            try {

                String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.editCart;
                HashMap<String,Object> paramSMap = new  HashMap<String,Object>();
                HashMap<String,Object> paramSMapmain = new  HashMap<String,Object>();
                paramSMap.put("clientId",GlobalVariables.clientID);
                if(CartManager.getInstance().getCartItemId(cartItem.getProduct_variant_id())==-1)
                {
                    Toast.makeText(mActivity,mActivity.getString(R.string.cart_item_not_found) , Toast.LENGTH_SHORT).show();

                    return;
                }
                paramSMap.put("cartId",CartManager.getInstance().getCartItemId(cartItem.getProduct_variant_id()));
                paramSMap.put("productId",cartItem.getProductid());
                paramSMap.put("qty", cartItem.getQty());
                paramSMap.put("type","Cart");

                paramSMapmain.put("params",paramSMap);
                JSONObject requetBody = new JSONObject(paramSMapmain);
                Utility.log("URL = ", url);
                Utility.log("Request body  = ", requetBody.toString());
                MyProgressBar.getInstance().showProgressDialog(mActivity);
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
                                    Toast.makeText(mActivity,mActivity.getString(R.string.cart_has_been_updated) , Toast.LENGTH_SHORT).show();
                                    Cart cartItem1 = CartManager.getInstance().getCartItem(cartItem.getProduct_variant_id());
                                    if(cartItem1!=null) {
                                        cartItem1.setQty(cartItem.getQty());
                                        CartManager.getInstance().addCartItem(cartItem1);
                                    }
                                }
                                if(mListener!=null)
                                mListener.Oncartcountlistner(CartManager.getInstance().getCartSize());

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

        private void hitApiToAddToCartOrWishList(categoryProduct categoryProduct, Context context,boolean isCart) {
            if(shopid == -1 || categoryid == -1)
                return;
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            try {
                MyProgressBar.getInstance().showProgressDialog(context);
                String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.addToCart;
                HashMap<String,Object> paramSMap = new  HashMap<String,Object>();
                HashMap<String,Object> paramSMapmain = new  HashMap<String,Object>();
                paramSMap.put("clientId",GlobalVariables.clientID);
                paramSMap.put("consumerId",customerId);
                paramSMap.put("productId",categoryProduct.getProductid());
                paramSMap.put("productVariantId",categoryProduct.getProduct_variant_id());
                paramSMap.put("productName",categoryProduct.getName());
                if(isCart) {
                    paramSMap.put("qty", categoryProduct.getQty());
                }else {
                        paramSMap.put("qty", 0);

                }

                paramSMap.put("price",categoryProduct.getSale_price());
                if(categoryProduct.getProductvarient().size()>0)
                    paramSMap.put("productVariant",categoryProduct.getProductvarient().get(categoryProduct.getSelectedproductVrientPostion()).getName());
                else
                    paramSMap.put("productVariant","");
                paramSMap.put("shopId",categoryProduct.getShop_id());
                paramSMap.put("shopName",categoryProduct.getShopName());
                if(isCart)
                    paramSMap.put("type","Cart");
                else
                    paramSMap.put("type","Wishlist");
                paramSMap.put("isCombo", categoryProduct.isIs_combo());
                paramSMapmain.put("params",paramSMap);

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
                                    if (mListener != null && isCart) {
                                        JSONArray cartValueJsonArray =jsonObject.getJSONArray("cart_value");
                                        if(cartValueJsonArray!=null && cartValueJsonArray.length()>0){
                                            JSONObject cartValueJsonObj = cartValueJsonArray.getJSONObject(0);
//                                            CartManager.getInstance().setTotalQty(cartValueJsonObj.getInt("total_qty"));
                                            Object json =jsonObject.get("result");
                                            Cart cartItem = new Cart();
                                            if (json instanceof String){
                                                Toast.makeText( mActivity,json.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                            else if (json instanceof JSONArray){
                                                JSONArray result = jsonObject.getJSONArray("result");
                                                if(result.length()>0){
                                                    cartItem.setId(result.optInt(0));
                                                }
                                                Toast.makeText( mActivity,mActivity.getString(R.string.added_into_cart) , Toast.LENGTH_SHORT).show();
                                            }
                                            cartItem.setProduct_id(categoryProduct.getProductid());
                                            cartItem.setQty((categoryProduct.getSelectedProductVariantQTY()));
                                            cartItem.setProduct_variant_id(categoryProduct.getProduct_variant_id());
                                            CartManager.getInstance().addCartItem(cartItem);

                                            mListener.Oncartcountlistner(cartValueJsonObj.getInt("total_qty"));

                                        }
                                         }else if(!isCart){
                                        if(categoryProduct.getWishListAdded()) {
                                            categoryProduct.setWishListAdded(false);
                                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.item_removed_to_wishlist), Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Wishlist.addProductToWishList(categoryProduct);
                                            categoryProduct.setWishListAdded(true);
                                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.item_added_to_wishlist), Toast.LENGTH_SHORT).show();
                                        }
                                        notifyDataSetChanged();
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
            return productdetials.size();
        }

    }
    public void applyFilter(ArrayList<searchbrand> brandFilter, int maxPrice, int minPrice, ArrayList<ProductAttribute> attributeList/*, ArrayList<ProductAttribute> attributeListSize*/){
        this.filteredMinPrice = minPrice;
        this.filteredMaxPrice = maxPrice;
        isForNextPageData = false;
        pageIndex = 0;
        filteredBrand =  new JSONArray();
        filteredAttributs =  new JSONArray();
//        filteredSizeAttributs =  new JSONArray();
        for (searchbrand  obj:brandFilter) {
            filteredBrand.put(obj.getBrandid());
        }
        for (ProductAttribute  obj:attributeList) {
            filteredAttributs.put(obj.getId());
        }
        /*for (ProductAttribute  obj:attributeList) {
            filteredSizeAttributs.put(obj.getId());
        }*/
        getProductList();
    }
    public void resetFilter(){
        isForNextPageData =  false;
        pageIndex = 0;
        filteredMinPrice = 0;
        filteredMaxPrice = -1;
        filteredBrand =  new JSONArray();
        filteredAttributs =  new JSONArray();
//        filteredSizeAttributs =  new JSONArray();
        getProductList();
    }


}
