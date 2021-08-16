package com.inlog.ecommerce.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.navigation.NavigationView;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.fragments.CategoryFragment;
import com.inlog.ecommerce.fragments.HomeFragment;
import com.inlog.ecommerce.fragments.OffersFragment;
import com.inlog.ecommerce.fragments.SearchFragment;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.Wishlist;
import com.inlog.ecommerce.model.ratingmodel;
import com.inlog.ecommerce.myaccount.AccountActivity;
import com.inlog.ecommerce.myaccount.fragment.AddressFragment;
import com.inlog.ecommerce.myaccount.fragment.AddressListingFragment;
import com.inlog.ecommerce.myaccount.fragment.ChangePasswordFragment;
import com.inlog.ecommerce.myaccount.fragment.MyAccountFragment;
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner;
import com.inlog.ecommerce.notification.BadgeDrawable;
import com.inlog.ecommerce.util.CustomGridViewActivity;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.util.GpsTracker;
import com.inlog.ecommerce.util.bottomnavBar.BottomBarItem;
import com.inlog.ecommerce.util.bottomnavBar.BottomNavigationBar;
import com.inlog.ecommerce.util.commonMethods;
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
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

//https://codeexa.com/android-how-to-communicate-between-activity-and-fragment/
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener {
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    JSONArray shopCarouselarray = null;
    SliderLayout sliderShow;
    HashMap<String, String> url_maps = new HashMap<>();
    private ArrayList<String> Categorynames;
    DrawerLayout drawer;
    public BottomNavigationBar bottomNavigationBar;
    EditText edittext;
    LinearLayout layout;
    Context context;
    private static int customerId;
    String responsefinal;
    JSONArray totalarray = null;
    JSONArray shopbannerarray = null;
    JSONArray shopkeeperdetails = null;
    JSONArray shopdetailsarray = null;
    JSONArray shopresultarray = null;

    View badge;
    BottomBarItem homeitem;
    BottomBarItem categories;
    BottomBarItem offers;
    BottomBarItem basket;
    private int cartItemCount;
    TextView cartBadgeCountTxtv;
    GridView androidGridView;
    LinearLayout mainlv;
    String value;
    Button logo_obj,profile_obj;
    Toolbar toolbar;
    CircleImageView shopkeeper_image;
    TextView shopkeeper_name;
    TextView description;
    TextView shopname,shoprating;
    Button view_obj;
    RatingBar pop_ratingbar,delivery_ratingbar,professional_ratingbar,goodquality_ratingbar,responsive_ratingbar;
    boolean checkviewheight;
    CardView cardView;
    LinearLayout ratinglayout;
    LinearLayout mainview;
    AlertDialog dialog;
    ListView list;
    long lastClickTime = 0;
    ArrayList<ratingmodel> dataModels;
    ScrollView sv_container;
    private Menu menu;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cartItemCount = CartManager.getInstance().getCartSize();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainlv = (LinearLayout) findViewById(R.id.mainlv);
        sv_container = (ScrollView) findViewById(R.id.sv_container);
        sliderShow = findViewById(R.id.slider);
        logo_obj = (Button) findViewById(R.id.logo_obj);
        profile_obj = (Button) findViewById(R.id.profile_obj);
        shopkeeper_image = (CircleImageView) findViewById(R.id.shopkeeper_image);
        shopkeeper_name = (TextView) findViewById(R.id.shopkeeper_name);
        description = (TextView) findViewById(R.id.description);
        shopname = (TextView) findViewById(R.id.shopname);
        shoprating = (TextView) findViewById(R.id.shoprating);
        view_obj = (Button) findViewById(R.id.view_obj);
        cardView = (CardView) findViewById(R.id.cdView);
        ratinglayout = (LinearLayout) findViewById(R.id.ratinglayout);
        pop_ratingbar = (RatingBar) findViewById(R.id.pop_ratingbar);
        delivery_ratingbar = (RatingBar) findViewById(R.id.delivery_ratingbar);
        professional_ratingbar = (RatingBar) findViewById(R.id.professional_ratingbar);
        goodquality_ratingbar = (RatingBar) findViewById(R.id.goodquality_ratingbar);
        responsive_ratingbar = (RatingBar) findViewById(R.id.responsive_ratingbar);

        checkviewheight = false;
        profile_obj.setOnClickListener(v->{
            addMyAccountFragment();
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, HomeFragment.newInstance()).commitAllowingStateLoss();

        setUpBottomNavigationBar();


        edittext = (EditText) findViewById(R.id.edittext);
        layout = (LinearLayout) findViewById(R.id.layout);
        edittext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                bottomNavigationBar.selectTab(2,true);
                drawer.closeDrawer(GravityCompat.START);
                mainlv.setVisibility(View.GONE);
                sv_container.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);

                Fragment fragment = new SearchFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
            }
        });
        findViewById(R.id.rlAddressTop).setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, mapactivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
            finish();
        });
        ratinglayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (checkviewheight == false)
                {
                    expand(cardView, 500, 550);
                    checkviewheight = true;
                }
                else
                {
                    collapse(cardView, 500, 245);
                    checkviewheight = false;
                }
            }
        });
        view_obj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(shopresultarray!=null && shopresultarray.length()>0)
                {
                    RatingActivity.Companion.startActivity(MainActivity.this,shopresultarray.toString(),null,"Shop Rating");
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                }
                else
                    Toast.makeText(MainActivity.this,"rating data is loading..",Toast.LENGTH_LONG).show();
            }
        });

//        LinearLayout headerView =  navigationView.findViewById(R.id.headerView);
        View headerView =  navigationView.getHeaderView(0);
        ImageView imageView =  headerView.findViewById(R.id.imageView);
        TextView txtVHeaderTitle =  headerView.findViewById(R.id.txtVHeaderTitle);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"clcked",Toast.LENGTH_LONG).show();
            }
        });
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"clcked",Toast.LENGTH_LONG).show();
            }
        });

        menu = navigationView.getMenu();

     TextView   txtVcurrentAddress = findViewById(R.id.txtVcurrentAddress);
        GpsTracker gpsTracker = new GpsTracker(this);
        double latdouble;
        double longdouble;
        if (gpsTracker.canGetLocation()) {
            latdouble = gpsTracker.getLatitude();
            longdouble = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
            return;
        }
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latdouble, longdouble, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            txtVcurrentAddress.setText(address1);
            Drawable icon = getResources().getDrawable(R.drawable.edit_icon);
            icon.setBounds(0,0,40,40);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                icon.setTint(getColor(R.color.white));
            }
            txtVcurrentAddress.setCompoundDrawables(null, null, icon, null);
        }catch (Exception ex){
        }

        }

    private void addMyAccountFragment() {
        Intent intent1 =new  Intent(this, AccountActivity.class);
        startActivity(intent1);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }
    public void setUpBottomNavigationBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);

        homeitem = new BottomBarItem(R.drawable.home, R.string.Home);
        categories = new BottomBarItem(R.drawable.categoryic, R.string.Categories);
//        BottomBarItem search = new BottomBarItem(R.drawable.ic_search_white_24dp, R.string.Search);
        offers = new BottomBarItem(R.drawable.offersic, R.string.Offers);
        basket = new BottomBarItem(R.drawable.basketic, R.string.Basket);

        BadgeDrawable badge;
        badge = new BadgeDrawable(context);
        badge.setCount(20);
        badge.setVisible(true,false);
//        bottomNavigationBar.showBadge(3,badge);

        bottomNavigationBar
                .addTab(homeitem)
                .addTab(categories)
//                .addTab(search)
                .addTab(offers)
                .addTab(basket)
                .showBadge(3,badge);

        bottomNavigationBar.setOnSelectListener(new BottomNavigationBar.OnSelectListener() {
            @Override
            public void onSelect(int position) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    showContent(position);
                }
            }
        });
        addCartBadge();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showContent(int position) {

       if (position == 0) {
           drawer.closeDrawer(GravityCompat.START);
           FragmentManager fm = getSupportFragmentManager();
           for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
               fm.popBackStack();
           }
           mainlv.postDelayed(new Runnable() {
               @Override
               public void run() {
                   sv_container.setVisibility(View.VISIBLE);
                   mainlv.setVisibility(View.VISIBLE);
                   toolbar.setVisibility(View.VISIBLE);
                   layout.setVisibility(View.VISIBLE);
                   bottomNavigationBar.selectTab(0,true);
               }
           },100);

           getCartdata();
       }
       if (position == 1) {
           mainlv.postDelayed(new Runnable() {
               @Override
               public void run() {
                   drawer.closeDrawer(GravityCompat.START);
                   mainlv.setVisibility(View.GONE);
                   sv_container.setVisibility(View.GONE);
                   layout.setVisibility(View.VISIBLE);
                   getSupportActionBar().show();
                   Bundle bundle = new Bundle();
                   bundle.putInt("expandlist", 0);
                   Fragment CategoryFragmentvc = new CategoryFragment();
                   CategoryFragmentvc.setArguments(bundle);
                   FragmentManager fragmentManager = getSupportFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.content_main, CategoryFragmentvc,FRAGMENT_TAG)
                           .addToBackStack(null)
                           .commit();
               }
           },100);


       }
       if (position == 2) {
           mainlv.setVisibility(View.GONE);
           sv_container.setVisibility(View.GONE);
           drawer.closeDrawer(GravityCompat.START);
         /*  Fragment OffersFragmentvc = new OffersFragment();
           FragmentManager fragmentManager = getSupportFragmentManager();
           fragmentManager.beginTransaction().replace(R.id.content_main, OffersFragmentvc,FRAGMENT_TAG)
                   .commit();
            getWindow().setAllowReturnTransitionOverlap(false);*/

           int categoryid = 34;

           SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", 0);
           SharedPreferences.Editor editor = sharedPrefs.edit();
           editor.putInt("categoryid",categoryid);
           editor.apply();

           Intent intent = new Intent(this, ProductListActivity.class);
           intent.putExtra("subcategoryname","Combo Stores");
           intent.putExtra("categoryid",categoryid);
           intent.putExtra("movepage",1);
           startActivity(intent);
           overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);


       }
       if (position == 3) {
           drawer.closeDrawer(GravityCompat.START);

           Intent i = new Intent(MainActivity.this, CartListActivity.class);
           i.putExtra("movepage","2");
           startActivity(i);
           overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
           finish();
       }
    }
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
            getCartdata();
            getWishList();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if(fragment instanceof MyAccountFragment){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    showContent(0);
                }
            }
            else if(fragment instanceof AddressFragment || fragment instanceof AddressListingFragment){
                Utility.log("current fragment ","AddressFragment");
                getSupportFragmentManager().popBackStack();
                bottomNavigationBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.GONE);
                        bottomNavigationBar.selectTab(0,true);
                    }
                },100);

            } else if(fragment instanceof CategoryFragment){
                showContent(0);
            }else if(fragment instanceof OffersFragment){
                showContent(0);
            }else if(fragment instanceof ChangePasswordFragment){
                super.onBackPressed();
            }else {

            }
            /*else
                finish();*/

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        this.menu = menu;
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
         if (id == R.id.my_wishlist) {
             Intent i = new Intent(MainActivity.this, WishlistActivity.class);
             startActivity(i);
             overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);

         }else if (id == R.id.my_cart) {
             Intent i = new Intent(MainActivity.this, CartListActivity.class);
             i.putExtra("movepage","2");
             startActivity(i);
             overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
             finish();
        }

        else if(id == R.id.my_account) {
            addMyAccountFragment();
        }
        else if(id == R.id.my_orders)
        {
            Intent i = new Intent(MainActivity.this, MyOrdersActivity.class);
            GlobalVariables.checkorderpageroot = "1";
            startActivity(i);
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
            finish();
        }

        else {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void getCartdata() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            SharedPreferences sharedPrefsLogin = MainActivity.this.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
            customerId = sharedPrefsLogin.getInt("globalid",0);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getConsumerCart+"?clientId="+GlobalVariables.clientID+"&consumerId=" +customerId+ "&type=Cart&index=0&limit=100";
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    responsefinal = null;
                    responsefinal = response.toString();
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {
                            CartManager.getInstance().getCartList().clear();
                            jsonObject = new JSONObject(responsefinal);
                            totalarray =  new JSONArray();
                            JSONObject result = jsonObject.getJSONObject("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK"))
                            {
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

                                totalarray = result.getJSONArray("total");
                                JSONObject totalDict = totalarray.getJSONObject(0);
//                                CartManager.getInstance().setTotalQty(totalDict.getInt("total_qty"));
                                updateCartCount(CartManager.getInstance().getCartSize());
                            }
                            getCMSShopCarousel();
//                            MyProgressBar.getInstance().dismissProgressDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
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
    public void getCMSShopCarousel(){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", 0);
            int shopid =  sharedPrefs.getInt("shopid",0);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getCMSShopCarousel+"?clientId="+GlobalVariables.clientID+"&ShopId=" +shopid;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            shopCarouselarray = new JSONArray();
                            shopCarouselarray = jsonObject.getJSONArray("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {

                                if (shopCarouselarray.length() > 0)
                                {
//                                    url_maps = new HashMap<>();
                                    sliderShow.removeAllSliders();
                                    for (int i = 0; i < shopCarouselarray.length(); i++)
                                    {
                                        JSONObject tempDict = (JSONObject) shopCarouselarray.get(i);
//                                        url_maps.put("url", tempDict.getString("image"));
//                                        url_maps.put("des", String.valueOf(i));

                                        TextSliderView defaultSliderView = new TextSliderView(context);
                                            // initialize a SliderLayout
                                            defaultSliderView
                                                    .image(tempDict.getString("image"))
                                                    .text(tempDict.getString("name"))
                                                    .description(tempDict.getString("text"))
                                                    .desfont(tempDict.getString("text_color"))
                                                    .setOnSliderClickListener(MainActivity.this);


                                            defaultSliderView.bundle(new Bundle());
                                            defaultSliderView.getBundle()
                                                    .putInt("extra",tempDict.getInt("id"));

                                            sliderShow.addSlider(defaultSliderView);

                                    }
                                    sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                    sliderShow.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                                }
                                else {
                                    Toast.makeText( context,getString(R.string.nodatafound) , Toast.LENGTH_SHORT).show();
                                }
                                MyProgressBar.getInstance().dismissProgressDialog();
                            }
                            getCMSShopBanner();
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
                    MyProgressBar.getInstance().dismissProgressDialog();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getCMSShopBanner(){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", 0);
            int shopid =  sharedPrefs.getInt("shopid",0);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getCMSShopBanner+"?clientId="+GlobalVariables.clientID+"&ShopId=" +shopid;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            shopbannerarray = new JSONArray();
                            shopbannerarray = jsonObject.getJSONArray("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {

                                if (shopbannerarray.length() > 0)
                                {
                                    CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(context,shopbannerarray,"2");
                                    androidGridView = (GridView) findViewById(R.id.grid_view_image_text);
                                    androidGridView.setVerticalScrollBarEnabled(false);
                                    androidGridView.setAdapter(adapterViewAndroid);
                                    commonMethods.setGridViewHeightBasedOnChildren(androidGridView,2);                                    androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int i, long id) {


                                        }
                                    });
                                }
                                else {
                                    Toast.makeText( context,getString(R.string.nodatafound) , Toast.LENGTH_SHORT).show();
                                }
                                getSpecificShopDetails();

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
                    MyProgressBar.getInstance().dismissProgressDialog();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void popup(JSONObject tempDict){
        bottomNavigationBar.selectTab(1,true);
        drawer.closeDrawer(GravityCompat.START);
        mainlv.setVisibility(View.GONE);
        sv_container.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        getSupportActionBar().show();
        Bundle bundle = new Bundle();
        bundle.putInt("expandlist", tempDict.optInt("categid"));
        Fragment CategoryFragmentvc = new CategoryFragment();
        CategoryFragmentvc.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, CategoryFragmentvc)
                .addToBackStack(null)
                .commit();

    }
    public void getSpecificShopDetails(){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", 0);
            int shopid =  sharedPrefs.getInt("shopid",0);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getSpecificShopDetails+"?clientId="+GlobalVariables.clientID+"&shopId=" +shopid;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            shopkeeperdetails = new JSONArray();
                            shopkeeperdetails = jsonObject.getJSONArray("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {

                                if (shopkeeperdetails.length() > 0)
                                {
                                    JSONObject tempDict = (JSONObject) shopkeeperdetails.get(0);
                                    Picasso.with(context).load(tempDict.optString("shopkeeper_image")).into(shopkeeper_image);

                                    shopkeeper_name.setText(tempDict.optString("shopkeeper_name"));
                                    description.setText( HtmlCompat.fromHtml(tempDict.optString("description").toString(), 0));
                                    shoprating.setText(String.format(" %s Out of 5", tempDict.optString("overall_rating")));
                                    shopname.setText(String.format("%s - %s (%s)", tempDict.optString("name"), tempDict.optString("shop_category"), tempDict.optString("alias")));

                                }
                                else {
                                    Toast.makeText( context,getString(R.string.nodatafound) , Toast.LENGTH_SHORT).show();
                                }
                                getShopRating();
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
                    MyProgressBar.getInstance().dismissProgressDialog();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getShopRating(){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        try {
            SharedPreferences sharedPrefs = getSharedPreferences("shopidkey", 0);
            int shopid =  sharedPrefs.getInt("shopid",0);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getShopRating+"?clientId="+GlobalVariables.clientID+"&shopId=" +shopid;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            shopdetailsarray = new JSONArray();
                            shopresultarray = new JSONArray();
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                shopdetailsarray = jsonObject.getJSONArray("ShopDetails");
                                shopresultarray = jsonObject.getJSONArray("result");
                                if (shopdetailsarray.length() > 0)
                                {
                                    JSONObject tempDict = (JSONObject) shopdetailsarray.get(0);
                                    pop_ratingbar.setRating(Float.parseFloat(tempDict.optString("overall_rating")));
                                    delivery_ratingbar.setRating(Float.parseFloat(tempDict.optString("delivery_rating")));
                                    professional_ratingbar.setRating(Float.parseFloat(tempDict.optString("professional_rating")));
                                    goodquality_ratingbar.setRating(Float.parseFloat(tempDict.optString("good_quality_rating")));
                                    responsive_ratingbar.setRating(Float.parseFloat(tempDict.optString("responsive_rating")));
                                }
                                else {
                                    Toast.makeText( context,getString(R.string.nodatafound) , Toast.LENGTH_SHORT).show();
                                }
                                MyProgressBar.getInstance().dismissProgressDialog();
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
                    MyProgressBar.getInstance().dismissProgressDialog();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     @Override
    public void onSliderClick(BaseSliderView slider) {

    }
    @SuppressLint("SetTextI18n")
    private void addCartBadge(){
            LinearLayout itemView = (LinearLayout) bottomNavigationBar.getChildAt(3);
            View badge = LayoutInflater.from(this)
                    .inflate(R.layout.badge_text_count, itemView, true);
            cartBadgeCountTxtv = badge.findViewById(R.id.txtvBadgeCount);
            if(CartManager.getInstance().getCartSize()>0)
            cartBadgeCountTxtv.setText(CartManager.getInstance().getCartSize() + "");
            else
                cartBadgeCountTxtv.setText("");
            bottomNavigationBar.removeView(badge);
            bottomNavigationBar.addView(badge);
    }
    public void updateCartCount(int cartCount){
        this.cartItemCount = cartCount;
        if(cartBadgeCountTxtv!=null) {
            if(cartCount>0) {
                cartBadgeCountTxtv.setText(String.valueOf(cartCount));
                cartBadgeCountTxtv.setVisibility(View.VISIBLE);
            }
            else {
                cartBadgeCountTxtv.setVisibility(View.GONE);
            }
        }
    }
    public static void expand(final CardView v, int duration, int targetHeight) {

        int prevHeight  = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
    public static void collapse(final CardView v, int duration, int targetHeight) {
        int prevHeight  = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
    public void getWishList() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            SharedPreferences sharedPrefsLogin = getSharedPreferences("globalloginvalues", MODE_PRIVATE);
            customerId = sharedPrefsLogin.getInt("globalid", 0);
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerCart + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId + "&type=Wishlist&index=0&limit=100";
            Utility.log("URL = ", url);
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    responsefinal = null;
                    responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            JSONArray detailsarray = new JSONArray();
                            JSONObject result = jsonObject.getJSONObject("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                detailsarray = result.getJSONArray("cart");
                                WishlistManager.getInstance().getWishlist().clear();
                                if (detailsarray.length() > 0) {
                                    ArrayList  productlist =  new ArrayList<Wishlist>();
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


                                }
                                showWishlistCount(WishlistManager.getInstance().getSize());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
    private void showWishlistCount(int size) {
        if(menu!=null ){
            String wishlistTile =  getString(R.string.my_wishlist);
            if(size>0)
                wishlistTile = wishlistTile + " ("+size+")";

            if (menu.findItem(R.id.my_wishlist)!=null)
            menu.findItem(R.id.my_wishlist).setTitle(wishlistTile);
        }
    }
}
