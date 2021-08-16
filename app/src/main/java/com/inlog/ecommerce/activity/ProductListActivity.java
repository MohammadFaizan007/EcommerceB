package com.inlog.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.adapter.ListFilterAdapter;
import com.inlog.ecommerce.adapter.ProductAttributeAdapter;
import com.inlog.ecommerce.fragments.ProductListingFragment;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.ProductAttribute;
import com.inlog.ecommerce.model.searchbrand;
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner;
import com.inlog.ecommerce.rangeslider.RangeSeekBar;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.util.InternetConnectionDetector;
import com.inlog.ecommerce.utility.CartManager;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

public class ProductListActivity extends AppCompatActivity
        implements ProductListingFragment.SimpleStringRecyclerViewAdapter.Oncartcountlistner{
    private Button back;
    static ViewPager viewPager;
    TextView header;
    String newString;
    CounterFab fabOne;
    Button filter_obj;
    LinearLayout mainview;
    Button cancel_obj,apply_obj,reset_obj;
    RangeSeekBar seekBar1;
    AlertDialog dialog;
    boolean isFilterDataLoaded = false;
    private TagContainerLayout mTagContainerLayout1;
    private   ArrayList<searchbrand> brandFilter;
    private   ArrayList<ProductAttribute> productAttribute  = new ArrayList<>() ;
    private   ArrayList<ProductAttribute> productAttributeSize  = new ArrayList<>() ;
    private int maxPrice = -1;
    private int minPrice = -1;
    private int minSelectedPrice = -1;
    private int maxPriceSelectedPrice = -1;
    ProductListingFragment productListingFragment;
    private ListFilterAdapter filterAdapter;
    private ProductAttributeAdapter attributeAdpter;
    private ProductAttributeAdapter attributeSizeAdpter;
    private int localCartItemCount;
    private boolean isFilterApplied;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorydetail);
        back = findViewById(R.id.back);

        filter_obj = findViewById(R.id.filter_obj);
        brandFilter = new ArrayList<>();
        fabOne = (CounterFab) findViewById(R.id.fabOne);
//        fabOne.setImageDrawable(Converter.convertLayoutToImage(CategorydetailActivity.this,2000,R.drawable.ic_shopping_cart_white_24dp));
        localCartItemCount = CartManager.getInstance().getCartSize();
        fabOne.setCount(CartManager.getInstance().getCartSize());
        fabOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(ProductListActivity.this, CartListActivity.class);
                i.putExtra("movepage","1");
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                finish();

            }
        });
        filter_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFilterDataLoaded)
                generatepopup();
                else {
                    isFilterDataLoaded = false;
                    MyProgressBar.getInstance().showProgressDialog(getApplicationContext());
                    getFilterBrand();
                }
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpagernew);
        setupViewPager(viewPager);
        header = (TextView) findViewById(R.id.header);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            newString= null;
        } else {
            newString= extras.getString("subcategoryname");
        }
        header.setText(newString);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                onBackPressed();
            }

        });
        getFilterBrand();
    }
    private  void generatepopup() {

        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.layout_filter, null);
        mainview = (LinearLayout) alertLayout.findViewById(R.id.mainview);
        cancel_obj = (Button) mainview.findViewById(R.id.cancel_obj);
        apply_obj = (Button) mainview.findViewById(R.id.apply_obj);
        reset_obj = (Button) mainview.findViewById(R.id.reset_obj);
        mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);
        final RangeSeekBar seekBar1 = mainview.findViewById(R.id.rangeSeekBar);
        final RecyclerView recyclerViewListItem = mainview.findViewById(R.id.recyclerViewListItem);
        final RecyclerView rcyVProductVarients = mainview.findViewById(R.id.rcyVProductVarients);
        final RecyclerView rcyVProductVarientsSize = mainview.findViewById(R.id.rcyVProductVarientsSize);
        final TextView textView1 = mainview.findViewById(R.id.textView2);
        final TextView txtVIdAttributeLabelId = mainview.findViewById(R.id.txtVIdAttributeLabelId);
        final TextView txtVIdAttributeSizeLabelId = mainview.findViewById(R.id.txtVIdAttributeSizeLabelId);
//        final ScrollView scrView = mainview.findViewById(R.id.scrView);
        final androidx.appcompat.widget.SearchView searchBrandId = mainview.findViewById(R.id.searchBrandId);
        initializeSeekBar(seekBar1, textView1);

        if(maxPrice !=-1)
        seekBar1.setMax(maxPrice);

        if(minSelectedPrice !=-1 && maxPriceSelectedPrice !=-1){
            seekBar1.setProgress(minSelectedPrice, maxPriceSelectedPrice+1);
        }else if(maxPrice !=-1 && minPrice!=-1) {
            seekBar1.setProgress(minPrice, maxPrice);
        }

        if(!isFilterApplied){
            for (ProductAttribute obj: productAttribute) {
                obj.setChecked(false);
            }
            for (ProductAttribute obj: productAttributeSize) {
                obj.setChecked(false);
            }for (searchbrand obj: brandFilter) {
                obj.setChecked(false);
            }
        }
        attributeAdpter = new ProductAttributeAdapter(productAttribute,false, new MyListCallbackListner() {
            @Override
            public void onItemClick(int id, int tag) {
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

        attributeSizeAdpter = new ProductAttributeAdapter(productAttributeSize,false, new MyListCallbackListner() {
            @Override
            public void onItemClick(int id, int tag) {
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
        if(brandFilter.size()<1){
            searchBrandId.setVisibility(View.GONE);
        }else {
            searchBrandId.setVisibility(View.VISIBLE);
        }
        filterAdapter = new ListFilterAdapter(brandFilter);
        recyclerViewListItem.setAdapter(filterAdapter);
        recyclerViewListItem.setLayoutManager(new LinearLayoutManager(this));
        dialog = new AlertDialog.Builder(ProductListActivity.this)
                .setView(alertLayout)
                .setTitle("")
                .create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mainview.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utility.hideSoftKeyboard(ProductListActivity.this);
                return false;
            }
        });
     /*   scrView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utility.hideSoftKeyboard(ProductListActivity.this);
                return false;
            }
        });*/
        apply_obj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                isFilterApplied = true;
                InternetConnectionDetector internetCD = new InternetConnectionDetector(ProductListActivity.this);
                if (internetCD.isConnectingToInternet()) {
                    ArrayList<searchbrand> arrayList = new ArrayList<searchbrand>();
                    ArrayList<ProductAttribute> attributeList = new ArrayList<ProductAttribute>();
                    ArrayList<ProductAttribute> attributeListSize = new ArrayList<ProductAttribute>();
                    for (searchbrand obj : brandFilter) {
                        if(obj.checked)
                        arrayList.add(obj);
                    }
                    for (ProductAttribute obj : productAttribute) {
                        if(obj.isChecked())
                            attributeList.add(obj);
                    }
                    for (ProductAttribute obj : productAttributeSize) {
                        if(obj.isChecked())
                            attributeList.add(obj);
                    }
                    minSelectedPrice = seekBar1.getProgressStart();
                    maxPriceSelectedPrice = seekBar1.getProgressStartMaxValue();
                    if(productListingFragment!=null )
                    productListingFragment.applyFilter(arrayList,maxPriceSelectedPrice,minSelectedPrice,attributeList);
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(ProductListActivity.this, "No network", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reset_obj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                isFilterApplied = false;
                for (searchbrand obj : brandFilter) {
                    obj.setChecked(false);
                }
                if(filterAdapter!=null)
                    filterAdapter.notifyDataSetChanged();
                if(productListingFragment!=null)
                productListingFragment.resetFilter();

                minSelectedPrice= -1;
                maxPriceSelectedPrice= -1;
                dialog.dismiss();
            }
        });
        cancel_obj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                dialog.dismiss();
            }
        });
        searchBrandId.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchBrandId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus)
                    Utility.hideSoftKeyboard(ProductListActivity.this);
            }
        });


    }
    private void initializeSeekBar(RangeSeekBar seekbar, TextView textView) {
        seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(
                    final RangeSeekBar seekBar, final int progressStart, final int progressEnd, final boolean fromUser) {
                   /*if(progressStart<minPrice)
                       seekBar.setProgress(minPrice,progressEnd);*/

                   updateRangeText(textView, seekBar);
//                   maxPriceSelectedPrice = progressEnd;
//                   minSelectedPrice = progressStart;
            }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar seekBar) {
                if(seekBar.getProgressStart()<minPrice)
                seekBar.setProgress(minPrice,seekBar.getProgressEnd());
            }
        });

        updateRangeText(textView, seekbar);
    }
    @SuppressLint("SetTextI18n")
    private void updateRangeText(TextView textView, RangeSeekBar seekBar) {
        textView.setText(seekBar.getProgressStart() + " - " + seekBar.getProgressEnd());

    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        productListingFragment = new ProductListingFragment();
        adapter.addFragment(productListingFragment, getString(R.string.item_1));
        viewPager.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
            getCartdata();
    }
    @Override
    public void Oncartcountlistner(int count) {
        fabOne.setCount(count);
        localCartItemCount = count;
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
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
            int categoryId =   sharedPrefs.getInt("categoryid",0);;
            JSONArray jsinArray =  new JSONArray();
            jsinArray.put(categoryId);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getProductAttribute+"?clientId="+GlobalVariables.clientID+"&categId="+jsinArray+"&shopId="+value+"&productId";
            Utility.log("url = ",url);
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utility.log("Response = ",response.toString());
                    getProductPrice();
                    String responsefinal = null;
                    responsefinal = response.toString();
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {

                            jsonObject = new JSONObject(responsefinal);
                            JSONArray minMaxArray = jsonObject.getJSONArray("min_max_price");
                            JSONArray result = jsonObject.getJSONArray("result");

                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                if(minMaxArray.length()>0){
                                    JSONObject jsonObj = minMaxArray.optJSONObject(0);
                                    maxPrice =jsonObj.optInt("max_sale_price");
                                    minPrice =jsonObj.optInt("min_sale_price");
                                    minSelectedPrice = minPrice;
                                    maxPriceSelectedPrice = maxPrice;
                                }
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

    public void getProductPrice(){
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
           int  categoryId =  sharedPrefs.getInt("categoryid",0);
            JSONArray jsinArray =  new JSONArray();
            jsinArray.put(categoryId);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getProductBrand+"?clientId="+GlobalVariables.clientID+"&categId="+jsinArray+"&shopId="+value+"&productId";
            Utility.log("url = ",url);
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utility.log("Resposne = ",response.toString());
                    isFilterDataLoaded = true;
                    String responsefinal = null;
                    responsefinal = response.toString();
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {

                            jsonObject = new JSONObject(responsefinal);
                            JSONArray detailsarray = new JSONArray();
                            JSONArray  childarray = new JSONArray();
                            detailsarray = jsonObject.optJSONArray("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                if(detailsarray.length()>0){
                                    for (int i=0;i<detailsarray.length();i++){
                                        JSONObject jsonObj = detailsarray.getJSONObject(i);
                                        searchbrand brand =new searchbrand();
                                        brand.setChecked(false);
                                        brand.setBrandid(jsonObj.optInt("id"));
                                        brand.setCount(jsonObj.optInt("product_count"));
                                        brand.setBrandname(jsonObj.optString("attr"));
                                        brandFilter.add(brand);
                                    }
                                }

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
            MyProgressBar.getInstance().dismissProgressDialog();
            e.printStackTrace();
        }
    }
    public void getCartdata()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            SharedPreferences sharedPrefsLogin = ProductListActivity.this.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
            int customerId = sharedPrefsLogin.getInt("globalid", 0);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getConsumerCart+"?clientId="+GlobalVariables.clientID+"&consumerId=" +customerId+ "&type=Cart&index=0&limit=100";
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String responsefinal = response.toString();
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {
                            CartManager.getInstance().getCartList().clear();
                            jsonObject = new JSONObject(responsefinal);
                            JSONObject result = jsonObject.getJSONObject("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                JSONArray totalarray = result.getJSONArray("total");
                                JSONObject totalDict = totalarray.getJSONObject(0);
//                                CartManager.getInstance().setTotalQty(totalDict.getInt("total_qty"));
                                Oncartcountlistner(totalDict.optInt("total_qty"));

                                JSONArray detailsarray = result.getJSONArray("cart");
                                for (int i = 0; i < detailsarray.length(); i++) {
                                    JSONObject tempDict = (JSONObject) detailsarray.get(i);
                                    Cart cartItem = new Cart();
                                    cartItem.setId(tempDict.optInt("id"));
                                    cartItem.setProduct_id(tempDict.optInt("product_id"));
                                    cartItem.setProduct_name(tempDict.optString("product_name"));
                                    cartItem.setQty(tempDict.optString("qty"));
                                    cartItem.setProduct_variant_id(tempDict.optInt("product_variant_id"));
                                    CartManager.getInstance().addCartItem(cartItem);

                                }
                            }
                            fabOne.setCount(CartManager.getInstance().getCartSize());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        int checkroot = getIntent().getIntExtra("movepage",-1);
        if(checkroot == 1){
           goToMainScreen();
        }else
        super.onBackPressed();
    }

    private void goToMainScreen() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}