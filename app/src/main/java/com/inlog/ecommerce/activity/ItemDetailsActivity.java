package com.inlog.ecommerce.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.andremion.counterfab.CounterFab;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.adapter.ComboAdapter;
import com.inlog.ecommerce.adapter.Pager;
import com.inlog.ecommerce.adapter.ProductAttributeAdapter;
import com.inlog.ecommerce.adapter.SlidingImage_Adapter;
import com.inlog.ecommerce.adapter.listviewAdapter;
import com.inlog.ecommerce.fragments.ProductListingFragment;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.ImageModel;
import com.inlog.ecommerce.model.ProdctVariants;
import com.inlog.ecommerce.model.ProductAttribute;
import com.inlog.ecommerce.model.categoryProduct;
import com.inlog.ecommerce.model.specification;
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner;
import com.inlog.ecommerce.util.DynamicImageGetter;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.CartManager;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;
import com.inlog.ecommerce.utility.WishlistManager;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.inlog.ecommerce.fragments.ProductListingFragment.PRODUCT_DETAIL_EXTRA;
import static java.lang.String.format;


public class ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    categoryProduct productDetail;
    private String name;
    private int saleprice,baseprice;
    private final ArrayList<ProductAttribute> productAttribute  = new ArrayList<>() ;
    private final ArrayList<ProductAttribute> productAttributeSize  = new ArrayList<>() ;
    private StringBuilder selectedVariant ;
    EditText prnumber;
    TextView description_part;
    int productPosition;
    CardView less,more;
    private int minteger = 0;
    private static int shopid =-1,categoryid =-1;
    private static int customerId;
    String responsefinal;
    int productid,productvarientid;
    int rootcheck;
    TextView product_names;
    TextView list_item_price;
    TextView list_item_sale_price;
    TextView text_ratings,text_ratings_reviews;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private ViewPager tabViewPagerId;
    private SlidingImage_Adapter adapter;
    private LinearLayout thumbnailsContainer;
    private View btnNext, btnPrev;
    ViewPager viewPagerpop;
    Pager pager;
    ArrayList<ImageModel> imageModelsArray;
    LinearLayout addcartview;
    RelativeLayout pagermainpop;
    Button close_obj;
    JSONArray specificationsarray;
    ArrayList<specification> speclist;
    listviewAdapter specadapter;
    JSONArray attributeIds;
    private LinearLayout ratingLL;
    private ArrayList<ProductAttribute> productArttributeList = new ArrayList<>();
    private float DISABLE_ALFA =.3f;
    LinearLayout llComboProductsId;

    public static void startActivity(Activity activity,int productid, int rootcheck, String currentCount, categoryProduct categoryProduct){
        Intent intent = new Intent(activity, ItemDetailsActivity.class);
        intent.putExtra("productid",productid);
        intent.putExtra("checkroot",rootcheck);
        intent.putExtra("currentcount", currentCount);
        intent.putExtra(PRODUCT_DETAIL_EXTRA, categoryProduct);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        initilize();
        ImageView mImageView = (ImageView)findViewById(R.id.image1);
        llComboProductsId = findViewById(R.id.llComboProductsId);
        llComboProductsId.setVisibility(View.GONE);
        TextView textViewAddToCart = (TextView)findViewById(R.id.text_action_bottom1);
        textViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                try {
                    count = Integer.parseInt(prnumber.getText().toString());
                    productDetail.setQty(String.valueOf(count));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (count > 0) {
                    if(isVariantSelected()) {
                            hitApiToAddToCartOrWishList(productDetail, getApplicationContext(), true);
                    }else
                    Toast.makeText(ItemDetailsActivity.this, getString(R.string.please_select_product_variant), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(ItemDetailsActivity.this, getString(R.string.please_add_quantity), Toast.LENGTH_SHORT).show();

            }

        });
        TextView textViewBuyNow = (TextView)findViewById(R.id.text_action_bottom2);
        textViewBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ItemDetailsActivity.this, CartListActivity.class);
                i.putExtra("movepage","3");
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
//                finish();
            }
        });
        product_names = (TextView) findViewById(R.id.item_detail_name);
        list_item_price = (TextView) findViewById(R.id.list_item_price);
        list_item_sale_price = (TextView) findViewById(R.id.list_item_sale_price);
        description_part = (TextView) findViewById(R.id.description_part);
        text_ratings = (TextView) findViewById(R.id.text_ratings);
        text_ratings_reviews = (TextView) findViewById(R.id.text_ratings_reviews);
        text_ratings.setVisibility(View.VISIBLE);
        Button back = findViewById(R.id.back);
        viewPagerpop = (ViewPager) findViewById(R.id.pager);

        close_obj = findViewById(R.id.close_obj);
        LinearLayout Desc_Layout = (LinearLayout) findViewById(R.id.text_layout);
        LinearLayout apriori_layout = (LinearLayout) findViewById(R.id.apriori);

        less = (CardView) findViewById(R.id.less);
        more = (CardView) findViewById(R.id.more);
        more.setOnClickListener(this);
        less.setOnClickListener(this);
        prnumber = (EditText) findViewById(R.id.prnumber);
        addcartview = (LinearLayout) findViewById(R.id.addcartview);
        addcartview.setVisibility(View.VISIBLE);
        pagermainpop = (RelativeLayout) findViewById(R.id.pagermainpop);
        pagermainpop.setVisibility(View.GONE);

//        less.setBackgroundResource(R.drawable.rounded_corner);
//        more.setBackgroundResource(R.drawable.rounded_corner);
//        fabOne = (CounterFab) findViewById(R.id.fabOne);
//        fabOne.setCount(GlobalVariables.globalcartcount);
//        fabOne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Getting image uri from previous screen
        if (getIntent() != null) {
            productDetail = (categoryProduct) getIntent().getSerializableExtra(PRODUCT_DETAIL_EXTRA);
//            currentcount = getIntent().getStringExtra("currentcount");
            productArttributeList = productDetail.getProductAttributeList();

            rootcheck = getIntent().getIntExtra("checkroot", -1);
            if (rootcheck == 1)
            {
                productvarientid = getIntent().getIntExtra("productvarientid", -1);
            }
            btnNext = findViewById(R.id.next);
            btnPrev = findViewById(R.id.prev);

            btnPrev.setAlpha(DISABLE_ALFA);
            productPosition = getIntent().getIntExtra("position", -1);
            tabViewPagerId = (ViewPager) findViewById(R.id.view_pager);
            tabViewPagerId.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Utility.log("Position ", position+"");
                }

                @Override
                public void onPageSelected(int position) {
                    Utility.log("Position onPageSelected ", position+"");
                    if(position==ImagesArray.size()-1)
                    btnNext.setAlpha(DISABLE_ALFA);
                    else
                    btnNext.setAlpha(1f);

                    if(position ==0)
                    btnPrev.setAlpha(DISABLE_ALFA);
                    else
                        btnPrev.setAlpha(1f);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            thumbnailsContainer = (LinearLayout) findViewById(R.id.container);

            btnPrev.setOnClickListener(onClickListener(0));
            btnNext.setOnClickListener(onClickListener(1));
            productid = getIntent().getIntExtra("productid",0);
            getProductDetails();

        }

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                finish();
            }

        });
        close_obj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                pagermainpop.setVisibility(View.GONE);
                addcartview.setVisibility(View.VISIBLE);
            }

        });
        ratingLL = findViewById(R.id.llReviewId);
        ratingLL.setVisibility(View.GONE);
        ratingLL.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               RatingActivity.Companion.startActivity(ItemDetailsActivity.this,null,productDetail,"Product Rating");
               overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
           }
       });

    }

    private boolean isVariantSelected() {
        String seperator = "";
        selectedVariant = new StringBuilder();
        boolean isAttribute1Selected = true;
        boolean isAttribute2Selected = true;
        if(productAttribute.size()>0)
            isAttribute1Selected = false;
    if(productAttributeSize.size()>0)
        isAttribute2Selected = false;
        for (ProductAttribute obj:productAttribute) {
            if(obj.isChecked()) {
                selectedVariant.append(seperator).append(obj.getName());
                isAttribute1Selected = true;
                seperator = ",";
            }
        }
        for (ProductAttribute obj:productAttributeSize) {
            if(obj.isChecked()) {
                selectedVariant.append(seperator).append(obj.getName());
                isAttribute2Selected = true;
                seperator = ",";
            }
        }
        return isAttribute1Selected && isAttribute2Selected;
    }

    private void manageDescriptionTab() {
        ListView llSpecificationId = findViewById(R.id.listview);

        llSpecificationId.setVisibility(View.GONE);
        LinearLayout text_layout = findViewById(R.id.text_layout);
        RelativeLayout rlDescriptionId = findViewById(R.id.rlDescriptionId);
        View tabBottomSpeciLine = findViewById(R.id.tabBottomSpeciLine);
        View tabBottomLine = findViewById(R.id.tabBottomLine);
        rlDescriptionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabBottomSpeciLine.setBackgroundColor(getResources().getColor(R.color.divider_gray));
                tabBottomLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llSpecificationId.setVisibility(View.GONE);
                text_layout.setVisibility(View.VISIBLE);
            }
        });
        RelativeLayout rlSpecificationsId = findViewById(R.id.rlSpecificationsId);
        rlSpecificationsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabBottomSpeciLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tabBottomLine.setBackgroundColor(getResources().getColor(R.color.divider_gray));
                llSpecificationId.setVisibility(View.VISIBLE);
                speclist = new ArrayList<>();
                for(int i=0; i<specificationsarray.length(); i++) {
                    JSONObject speDict = null;
                    try {
                        speDict = (JSONObject) specificationsarray.get(i);
                        speclist.add(new specification(speDict.optString("name"), speDict.optString("value")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                specadapter = new listviewAdapter(ItemDetailsActivity.this, speclist);
                llSpecificationId.setAdapter(specadapter);
                setListViewHeightBasedOnItems(llSpecificationId);
//                specadapter.notifyDataSetChanged();
                text_layout.setVisibility(View.GONE);
            }
        });
    }

    private void setUpProductAttributes() {
        final RecyclerView rcyVProductVarients = findViewById(R.id.rcyVProductVarients);
        final RecyclerView rcyVProductVarientsSize = findViewById(R.id.rcyVProductVarientsSize);
        final TextView txtVIdAttributeLabelId = findViewById(R.id.txtVIdAttributeLabelId);
        final TextView txtVIdAttributeSizeLabelId = findViewById(R.id.txtVIdAttributeSizeLabelId);
        if (productDetail.isIs_combo()==true)
        {
            rcyVProductVarients.setVisibility(View.GONE);
            rcyVProductVarientsSize.setVisibility(View.GONE);
            txtVIdAttributeLabelId.setVisibility(View.GONE);
            txtVIdAttributeSizeLabelId.setVisibility(View.GONE);
        }
        else {
            rcyVProductVarients.setVisibility(View.VISIBLE);
            rcyVProductVarientsSize.setVisibility(View.VISIBLE);
            txtVIdAttributeLabelId.setVisibility(View.VISIBLE);
            txtVIdAttributeSizeLabelId.setVisibility(View.VISIBLE);
        }


        for (ProductAttribute adapterPrdAttr: productAttribute) {
            for (ProductAttribute prdAttr: productArttributeList) {
                if(prdAttr.getId() == adapterPrdAttr.getId()) {
                    adapterPrdAttr.setChecked(true);
                }
//                else
//                adapterPrdAttr.setChecked(false);
            }
        }
        for (ProductAttribute adapterPrdAttr: productAttributeSize) {
            for (ProductAttribute prdAttr: productArttributeList) {
                if(prdAttr.getId() == adapterPrdAttr.getId()) {
                    adapterPrdAttr.setChecked(true);
                }
//                else
//                adapterPrdAttr.setChecked(false);
            }
        }

        ProductAttributeAdapter attributeAdpter = new ProductAttributeAdapter(productAttribute, true, new MyListCallbackListner() {
            @Override
            public void onItemClick(int id, int tag) {
                resetAttributs();
                getProductDetails();
            }
        });
        rcyVProductVarients.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcyVProductVarients.setAdapter(attributeAdpter);

        if(productAttribute.size()>0) {
            txtVIdAttributeLabelId.setText(productAttribute.get(0).getLabel());
        }
        else {
            txtVIdAttributeLabelId.setVisibility(View.GONE);
        }

        ProductAttributeAdapter attributeSizeAdpter = new ProductAttributeAdapter(productAttributeSize, true, new MyListCallbackListner() {
            @Override
            public void onItemClick(int id, int tag) {
                resetAttributs();
                getProductDetails();
            }
        });
        rcyVProductVarientsSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcyVProductVarientsSize.setAdapter(attributeSizeAdpter);

        if(productAttributeSize.size()>0) {
            txtVIdAttributeSizeLabelId.setText(productAttributeSize.get(0).getLabel());
        }
        else {
            txtVIdAttributeSizeLabelId.setVisibility(View.GONE);
        }
    }

    private void resetAttributs() {
        productDetail.setQty("0");
        productArttributeList.clear();
        for (ProductAttribute attrObj: productAttribute) {
            if(attrObj.isChecked())
                productArttributeList.add(attrObj);
        }
        for (ProductAttribute attrObj: productAttributeSize) {
            if(attrObj.isChecked())
                productArttributeList.add(attrObj);
        }
    }

    private void initilize() {
        SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", 0);
        shopid =  sharedPrefs.getInt("shopid",0);
        categoryid =  sharedPrefs.getInt("categoryid",0);
        attributeIds = new JSONArray();

        SharedPreferences sharedPrefsLogin = getSharedPreferences("globalloginvalues", MODE_PRIVATE);
        customerId = sharedPrefsLogin.getInt("globalid",0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == more.getId()){
            minteger += 1;
            prnumber.setText(String.valueOf(minteger));

        } else if(view.getId() == less.getId()) {

            if (prnumber.getText().toString().equals("0"))
            {
                return;
            }
            else
            {
                minteger -= 1;
                prnumber.setText(String.valueOf(minteger));
            }
        }
        ProductListingFragment.SELECTED_PRODUCTS_QTY = minteger;
    }
    private void hitApiToAddToCartOrWishList(categoryProduct categoryProduct, Context context, boolean isCart) {
        if(shopid == -1 || categoryid == -1)
            return;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.addToCart;
            HashMap<String,Object> paramSMap = new  HashMap<String,Object>();
            HashMap<String,Object> paramSMapmain = new  HashMap<String,Object>();
            paramSMap.put("clientId",GlobalVariables.clientID);
            paramSMap.put("consumerId",customerId);
            paramSMap.put("productId",categoryProduct.getProductid());
            paramSMap.put("isCombo",categoryProduct.isIs_combo());

            paramSMap.put("productName",categoryProduct.getName());
            if(isCart) {
                paramSMap.put("qty", categoryProduct.getQty());
            }else {
                    paramSMap.put("qty", 0);
            }
            paramSMap.put("productVariantId",productvarientid);
            paramSMap.put("price",categoryProduct.getSale_price());
            if(selectedVariant!=null && selectedVariant.length()>0)
                paramSMap.put("productVariant",selectedVariant.toString());
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
                                if (isCart) {
                                    JSONArray cartValueJsonArray =jsonObject.getJSONArray("cart_value");
                                    if(cartValueJsonArray!=null && cartValueJsonArray.length()>0){
                                        JSONObject cartValueJsonObj = cartValueJsonArray.getJSONObject(0);
//                                       CartManager.getInstance().setTotalQty(cartValueJsonObj.getInt("total_qty"));
                                        Object json =jsonObject.get("result");
                                        Cart cartItem = new Cart();
                                        if (json instanceof String){
                                            Toast.makeText( ItemDetailsActivity.this,json.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        else if (json instanceof JSONArray){
                                            JSONArray result = jsonObject.getJSONArray("result");
                                            if(result.length()>0){
                                                cartItem.setId(result.optInt(0));
                                            }
                                            Toast.makeText( ItemDetailsActivity.this,getString(R.string.added_into_cart) , Toast.LENGTH_SHORT).show();
                                        }
                                        cartItem.setProduct_id(categoryProduct.getProductid());
                                        cartItem.setQty(cartValueJsonObj.getInt("total_qty"));
                                        cartItem.setProduct_variant_id(categoryProduct.getProduct_variant_id());
                                        CartManager.getInstance().addCartItem(cartItem);

                                    }

                                }else if(!isCart){
                                    if(categoryProduct.getWishListAdded()) {
                                        categoryProduct.setWishListAdded(false);
                                        Toast.makeText(ItemDetailsActivity.this, getResources().getString(R.string.item_removed_to_wishlist), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        categoryProduct.setWishListAdded(true);
                                        Toast.makeText(ItemDetailsActivity.this, getResources().getString(R.string.item_added_to_wishlist), Toast.LENGTH_SHORT).show();
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

    public void getProductDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            MyProgressBar.getInstance().showProgressDialog(this);

            String url;

            String productVariants = "&productVariantId=";
            if(productDetail!=null && productDetail.isIs_combo())
                productVariants = productVariants+productDetail.getProduct_variant_id();
            if (rootcheck == 0)
            {
                url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getProductVariant+"?clientId="+GlobalVariables.clientID+"&productId=" +productid+ "&attributeValueIds="+getProductAttributeIds(productArttributeList)+"&index=0&limit=5&consumerId="+customerId+"&isCombo="+productDetail.isIs_combo()+productVariants;
            }
            else {
                url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getProductVariant+"?clientId="+GlobalVariables.clientID+"&productId=" +productid+ "&attributeValueIds=&index=0&limit=5&productVariantId="+productvarientid+"&consumerId="+customerId+"&isCombo="+productDetail.isIs_combo();
            }
            rootcheck = 0;
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utility.log("URL = ",url);




                    responsefinal = null;
                    responsefinal = response.toString();
                    Utility.log("Response = ",responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {
                            jsonObject = new JSONObject(responsefinal);
                            if (jsonObject.optString("status").equalsIgnoreCase("OK"))
                            {
                                JSONArray detailsarray = jsonObject.getJSONArray("result");
                                JSONObject tempDict = (JSONObject) detailsarray.get(0);
                                specificationsarray = new JSONArray();
                                ImagesArray = new ArrayList<String>();
                                thumbnailsContainer.removeAllViews();
//                                tabViewPagerId.setSaveFromParentEnabled(false);
                                name = tempDict.optString("name");
                                productDetail.setName(name);
                                if(!tempDict.optString("shop_id").equalsIgnoreCase(""))
                                productDetail.setShop_id(tempDict.optString("shop_id"));

                                if(!tempDict.optString("shop").equalsIgnoreCase(""))
                                productDetail.setShopName(tempDict.optString("shop"));

                                productDetail.setIs_combo(tempDict.optBoolean("is_combo"));
                                saleprice = tempDict.optInt("sale_price");
                                categoryid = tempDict.optInt("category_id");
                                if(productAttributeSize.isEmpty() || productAttribute.isEmpty())
                                    getFilterBrand();
                                else
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                productDetail.setSale_price(saleprice);
                                baseprice = tempDict.optInt("mrp");
                                productvarientid = tempDict.optInt("product_variant_id");
                               ProductListingFragment.SELECTED_PRODUCTS_VARI_ID = productvarientid;
                                int cartvalue = tempDict.optInt("cart_count");
                                if(cartvalue>0) {
                                    productDetail.setQty(String.valueOf(cartvalue));
                                }
                                minteger = productDetail.getQty();
                                prnumber.setText(String.valueOf(productDetail.getQty()));
                                product_names.setText(name);

                                JSONArray attrValArray = tempDict.getJSONArray("attr_value");
                                productArttributeList.clear();
                                for (int k = 0;k<attrValArray.length();k++){

                                    JSONObject attrJsonOBJ = attrValArray.optJSONObject(k);
                                    ProductAttribute attrObj = new ProductAttribute();
                                    attrObj.setId(attrJsonOBJ.optInt("id"));
                                    attrObj.setLabel(attrJsonOBJ.optString("Name"));
                                    attrObj.setName(attrJsonOBJ.optString("Name"));
                                    productArttributeList.add(attrObj);
                                }

                                text_ratings.setText(format("%d*", tempDict.optInt("overall_rating")));
                                text_ratings_reviews.setText(format("%d Rating", tempDict.optInt("rating_count")));
                                if (tempDict.optInt("overall_rating") == 0)
                                {
                                    ratingLL.setVisibility(View.GONE);
                                }
                                else
                                {
                                    ratingLL.setVisibility(View.VISIBLE);
                                }

                                list_item_price.setText(format("₹ %s", String.valueOf(saleprice)));
                                list_item_sale_price.setText(format("₹ %s", String.valueOf(baseprice)));
                                list_item_sale_price.setPaintFlags(list_item_sale_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                DynamicImageGetter.with(ItemDetailsActivity.this)
                                        .load(tempDict.optString("short_description").toString())
                                        .mode(0)
                                        .into(description_part);

                                JSONArray multi_imagesarray = tempDict.getJSONArray("multi_images");
                                specificationsarray = tempDict.getJSONArray("product_specification");
                                for (int i = 0; i < multi_imagesarray.length(); i++) {
                                    JSONObject imagedict = (JSONObject) multi_imagesarray.get(i);

                                    ImagesArray.add(imagedict.getString("image"));

                                    adapter = new SlidingImage_Adapter(ItemDetailsActivity.this, ImagesArray);
                                    tabViewPagerId.setAdapter(adapter);
                                }

                                JSONArray combo_product_data = tempDict.optJSONArray("combo_product_data");

                                   showComboProducts(combo_product_data);



                                if(ImagesArray.size()<2)
                                    btnNext.setAlpha(DISABLE_ALFA);

                                if (ImagesArray.size()<=1)
                                {
                                    btnNext.setEnabled(false);
                                    btnPrev.setEnabled(false);
                                }
                                else {
                                    btnNext.setEnabled(true);
                                    btnPrev.setEnabled(true);
                                }

                                inflateThumbnails();
                                manageDescriptionTab();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void showComboProducts(JSONArray combo_product_data) {

        ArrayList<categoryProduct>  comboProducts = getComboProducts(combo_product_data);
        if (comboProducts.size()>0) {
            llComboProductsId.setVisibility(View.VISIBLE);
            RecyclerView comboRecycVId = findViewById(R.id.comboRecycVId);
            comboRecycVId.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            comboRecycVId.setAdapter(new ComboAdapter(comboProducts, this));
        }
        else llComboProductsId.setVisibility(View.GONE);
    }

    private ArrayList<categoryProduct> getComboProducts(JSONArray combo_product_data) {
        ArrayList<categoryProduct> comboProducts = new ArrayList<>();
        if (combo_product_data!=null)
        for (int i = 0; i < combo_product_data.length(); i++) {
            JSONObject tempDict = combo_product_data.optJSONObject(i);


            ArrayList<ProdctVariants> productvarient = new ArrayList<ProdctVariants>();
            categoryProduct productObj = new categoryProduct(tempDict.optInt("product_id"),
                    tempDict.optString("product_name"),
                    "",
                    tempDict.optInt("base_price"),
                    tempDict.optInt("sale_price"),
                    tempDict.optString("image"),
                    tempDict.optInt("product_variant_id"),
                    productvarient,
                    tempDict.optString("shop"), tempDict.optString("shop_id"),
                    tempDict.optInt("discount"),
                    tempDict.optBoolean("is_combo")
            );
            productObj.setQty(tempDict.optString("qty"));
            comboProducts.add(productObj);
        }
        return comboProducts;
    }

    private void inflateThumbnails() {
        for (int i = 0; i < ImagesArray.size(); i++) {
            View imageLayout = getLayoutInflater().inflate(R.layout.item_image, null);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_thumb);
            imageView.setOnClickListener(onChagePageClickListener(i));
            imageView.setTag(i);


//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(30, 0, 80, 80);
//            imageView.setLayoutParams(lp);

//            Drawable highlight = getResources().getDrawable( R.drawable.bordertwo);
//            imageView.sets(highlight);

            Picasso.with(ItemDetailsActivity.this).load(ImagesArray.get(i)).into(imageView);
            thumbnailsContainer.addView(imageLayout);
        }
    }

    private View.OnClickListener onClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
                    if (tabViewPagerId.getCurrentItem() < tabViewPagerId.getAdapter().getCount() - 1) {
                        tabViewPagerId.setCurrentItem(tabViewPagerId.getCurrentItem() + 1);
                    }
                } else {
                    //previous page
                    if (tabViewPagerId.getCurrentItem() > 0) {
                        tabViewPagerId.setCurrentItem(tabViewPagerId.getCurrentItem() - 1);
                    }
                }
            }
        };
    }

    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabViewPagerId.setCurrentItem(i);
//                int tag = (Integer)v.getTag();
//                for (int i=0; i<chosenTags.size();i++) {
//                    if (tag==chosenTags.get(i)) {
//                        chosenTags.remove(i);
//                        cancelSelect((ImageView) v);
//                        return;
//                    }
//                }
//                chosenTags.add(tag);
//                select((ImageView) v,i);
            }
        };
    }

    public void popuppager(int currentpossition) {

        imageModelsArray  = new ArrayList<>();
        for (int i = 0; i < ImagesArray.size(); i++) {
            imageModelsArray.add(new ImageModel(ImagesArray.get(i)));
        }
        if (imageModelsArray.size()>0)
        {
            addcartview.setVisibility(View.GONE);
            pagermainpop.setVisibility(View.VISIBLE);
            pager = new Pager(this, imageModelsArray);
            viewPagerpop.setAdapter(pager);
            viewPagerpop.setCurrentItem(currentpossition);
        }
    }

    public void select(ImageView imageView,int i){
        imageView.setBackground(getResources().getDrawable(R.drawable.imageborder));
        Picasso.with(ItemDetailsActivity.this).load(ImagesArray.get(i)).into(imageView);
    }

    public void cancelSelect(ImageView imageView){
        imageView.setImageDrawable(null);
    }

    public void getFilterBrand(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            int value;
//            MyProgressBar.getInstance().showProgressDialog(getApplicationContext());

            SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", MODE_PRIVATE);

            if(!sharedPrefs.contains("shopid"))
            {
                return;
            }
            else
            {
                value =  sharedPrefs.getInt("shopid",0);
            }
//            int categoryId =  getIntent().getIntExtra("categoryid",0);
//            int categoryId =   sharedPrefs.getInt("categoryid",0);;
            JSONArray jsinArray =  new JSONArray();
            jsinArray.put(categoryid);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getProductAttribute+"?clientId="+GlobalVariables.clientID+"&categId="+jsinArray+"&shopId="+value+"&productId="+productid;
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    MyProgressBar.getInstance().dismissProgressDialog();
                    Utility.log("URL = ",url);
                    Utility.log("Response = ",response.toString());
                    String responsefinal = null;
                    responsefinal = response.toString();
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {

                            jsonObject = new JSONObject(responsefinal);
                            JSONArray minMaxArray = jsonObject.getJSONArray("min_max_price");
                            JSONArray result = jsonObject.getJSONArray("result");

                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {

                                if(result.length()>0){
                                    productAttribute.clear();
                                    JSONObject jsonObj = result.optJSONObject(0);
                                    JSONArray attr_value = jsonObj.getJSONArray("attr_value");
                                    String label = jsonObj.optString("attr");
                                    for (int i=0;i<attr_value.length();i++) {
                                        JSONObject atrValueObj = attr_value.getJSONObject(i);
                                        ProductAttribute brandObj = new ProductAttribute();
                                        brandObj.setName(atrValueObj.optString("Name"));
                                        if (atrValueObj.optString("html_color").equals("null"))
                                        {
                                            brandObj.setColor("");
                                        }
                                        else
                                        {
                                            brandObj.setColor(atrValueObj.optString("html_color"));
                                        }

                                        brandObj.setId(atrValueObj.optInt("id"));

                                        brandObj.setLabel(label);
                                        productAttribute.add(brandObj);
                                    }
                                    if(result.length()>1){
                                        productAttributeSize.clear();
                                        JSONObject jsonObj1 = result.optJSONObject(1);
                                        JSONArray attr_value1 = jsonObj1.getJSONArray("attr_value");
                                        String label1 = jsonObj1.optString("attr");
                                        for (int i=0;i<attr_value1.length();i++) {
                                            JSONObject atrValueObj = attr_value1.getJSONObject(i);
                                            ProductAttribute brandObj = new ProductAttribute();
                                            brandObj.setName(atrValueObj.optString("Name"));
                                            if (atrValueObj.optString("html_color").equals("null"))
                                            {
                                                brandObj.setColor("");
                                            }
                                            else
                                            {
                                                brandObj.setColor(atrValueObj.optString("html_color"));
                                            }
                                            brandObj.setId(atrValueObj.optInt("id"));
                                            brandObj.setLabel(label1);
                                            productAttributeSize.add(brandObj);
                                        }
                                    }

                                        setUpProductAttributes();


                                }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
//                        MyProgressBar.getInstance().dismissProgressDialog();
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
            MyProgressBar.getInstance().dismissProgressDialog();
            e.printStackTrace();
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }

    }
    public JSONArray getProductAttributeIds(ArrayList<ProductAttribute> productArttributeList) {
        JSONArray jsoArray = new JSONArray();
            for (ProductAttribute prdAttribute : productArttributeList) {
                jsoArray.put(prdAttribute.getId());
            }

        return jsoArray;
    }

    public void onComboItemClicked(@NotNull categoryProduct productDetailArg) {
        productvarientid = productDetail.getProduct_variant_id();
        productDetail = productDetailArg;
        ItemDetailsActivity.startActivity(this,productDetail.getProductid(),0,"0",productDetail);

//        getProductDetails();

    }
}
