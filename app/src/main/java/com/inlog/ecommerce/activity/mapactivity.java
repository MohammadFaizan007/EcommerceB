package com.inlog.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.adapter.ItemAdapter;
import com.inlog.ecommerce.util.CustomGridViewActivity;
import com.inlog.ecommerce.util.DynamicImageGetter;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.util.GpsTracker;
import com.inlog.ecommerce.util.UrlImageParser;
import com.inlog.ecommerce.util.commonMethods;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR;


public class mapactivity extends FragmentActivity implements OnMapReadyCallback,
        ItemAdapter.ItemListener , BaseSliderView.OnSliderClickListener{

    private GoogleMap mMap;
    GpsTracker gpsTracker;
    private Double latdouble,longdouble ;
    Geocoder geocoder;
    EditText edittext;
    String responsefinal;
    JSONArray detailsarray = null;
    Button filter_obj;
    BottomSheetBehavior behavior;
    private ItemAdapter mAdapter;
    RecyclerView recyclerView;
    FrameLayout coordinatorLayout;
    ArrayList<String> items = new ArrayList<>();
    Context context;
    JSONArray shopCarouselarray = null;
    SliderLayout sliderShow;
    JSONArray shopbannerarray = null;
    GridView androidGridView;
    LinearLayout mainview;
    ImageView itemimg;
    TextView itemtitle,itemdes;
    AlertDialog dialog;
    Button cancel_obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;
        Places.initialize(this, "AIzaSyCKXLQLROJbsXLDdCrSVZxP0dqwUNoPq68");
        PlacesClient placesClient = Places.createClient(this);
        gpsTracker = new GpsTracker(this);
        edittext = findViewById(R.id.edittext);
        filter_obj = findViewById(R.id.filter_obj);
        getCMSShopCarousel();
//        coordinatorLayout = (FrameLayout) findViewById(R.id.coordinatorLayout);
        SharedPreferences sharedPrefs = getSharedPreferences("ShopAddress", 0);
        String address =sharedPrefs.getString("shopAddress","");
        String shopLat =sharedPrefs.getString("shopLat","");
        String shopLong =sharedPrefs.getString("shopLong","");

        if(shopLat!=null && shopLat.length()>1 && shopLong!=null && shopLong.length()>0){
            latdouble = Utility.strToDouble(shopLat);
            longdouble = Utility.strToDouble(shopLong);
        }
        else if (gpsTracker.canGetLocation()) {
            latdouble = gpsTracker.getLatitude();
            longdouble = gpsTracker.getLongitude();
        } else {

            gpsTracker.showSettingsAlert();
            return;

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragmentResto);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        /*Disableing Parent Scrolling when map is scrolled using two fingers*/
        final ScrollView svRestoDetail = (ScrollView) findViewById(R.id.sv_container);
        ImageView ivMapTransparent = (ImageView) findViewById(R.id.ivMapTransparent);
        sliderShow = findViewById(R.id.slider);
        ivMapTransparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        svRestoDetail.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        svRestoDetail.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        svRestoDetail.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        View nestedScrollView = (View) findViewById(R.id.layoutscroll);
        behavior = BottomSheetBehavior.from(nestedScrollView);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        filter_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.size() >0)
                {
                    mAdapter = new ItemAdapter(items, detailsarray,mapactivity.this::onItemClick);
                    recyclerView.setAdapter(mAdapter);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else
                {
                    commonMethods.showMessageOKCancel(getString(R.string.nostoresfound),mapactivity.this);

                }

            }
        });

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                callPlaceSearchIntent(202);
            }
        });


    }
    @Override
    public void onItemClick(String item, JSONObject object) throws JSONException {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        SharedPreferences sharedPref = getSharedPreferences("shopidkey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("shopid", object.getInt("id"));
        editor.apply();


        Intent i = new Intent(mapactivity.this, MainActivity.class);
        startActivity(i);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latdouble, longdouble, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            edittext.setText(address1);
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void callPlaceSearchIntent(int code) {
        List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, code);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("", "Place:" + place.toString() + place.getLatLng());
                LatLng latLng = place.getLatLng();

                latdouble = latLng.latitude;
                longdouble = latLng.longitude;
                try {
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String address2 = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        edittext.setText(address1);
                        saveDataInPreference(place.getLatLng().latitude,place.getLatLng().longitude,address1);
                        Log.e("Address1: ", "" + address1);
                        Log.e("Address2: ", "" + address2);
                        Log.e("AddressCity: ", "" + city);
                        Log.e("AddressState: ", "" + state);
                        Log.e("AddressCountry: ", "" + country);
                        Log.e("AddressPostal: ", "" + postalCode);
                        Log.e("AddressLatitude: ", "" + place.getLatLng().latitude);
                        Log.e("AddressLongitude: ", "" + place.getLatLng().longitude);
                        getData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }


            } else if (resultCode == RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("", status.getStatusMessage());
                //  Toast.makeText(context,"Eror",Toast.LENGTH_LONG).show();
            } else if (requestCode == RESULT_CANCELED) {
            }
        }
    }
    private void saveDataInPreference(double latitude, double longitude, String address1) {
        SharedPreferences sharedPrefs = getSharedPreferences("ShopAddress", 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("shopAddress",address1);
        editor.putString("shopLat",String.valueOf(latitude));
        editor.putString("shopLong",String.valueOf(longitude));
        editor.apply();
    }
    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
//            MyProgressBar.getInstance().showProgressDialog(this);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getshopmethod+"?clientId="+GlobalVariables.clientID+"&zip=&cityId=&latitude="+latdouble+"&longitude="+longdouble+"&index=0&limit=100";
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    responsefinal = null;
                    responsefinal = response.toString();
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {

                            jsonObject = new JSONObject(responsefinal);
                            detailsarray = new JSONArray();
                            detailsarray = jsonObject.getJSONArray("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                MyProgressBar.getInstance().dismissProgressDialog();
                                if (detailsarray.length() > 0)
                                {
                                    items = new ArrayList<>();
                                    for (int i = 0; i < detailsarray.length(); i++) {
                                        JSONObject tempDict = (JSONObject) detailsarray.get(i);
                                        items.add(tempDict.getString("name").toString());
                                        LatLng lt = new LatLng(tempDict.optDouble("partner_latitude"), tempDict.optDouble("partner_longitude"));
                                        mMap.addMarker(new MarkerOptions().position(lt).title(tempDict.optString("name")).icon(BitmapDescriptorFactory.fromResource(R.drawable.markeric)));
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(new LatLng(lt.latitude, lt.longitude))
                                                .zoom(12).build();
                                        mMap.animateCamera(CameraUpdateFactory
                                                .newCameraPosition(cameraPosition));
                                    }

                                }
                                else
                                {
                                    commonMethods.showMessageOKCancel(getString(R.string.nostoresfound),mapactivity.this);
                                }


                            }

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
            MyProgressBar.getInstance().showProgressDialog(this);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getCMSSiteCarousel+"?clientId="+GlobalVariables.clientID;
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
                                                .description(tempDict.getString("slider_text"))
                                                .desfont(tempDict.getString("text_color"))
                                                .setOnSliderClickListener(mapactivity.this);


                                        defaultSliderView.bundle(new Bundle());
                                        defaultSliderView.getBundle()
                                                .putInt("extra",tempDict.getInt("id"));

                                        sliderShow.addSlider(defaultSliderView);

                                    }
                                    sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                    sliderShow.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                                }
                                else {
                                    Toast.makeText( context,getString(R.string.noproductsfound) , Toast.LENGTH_SHORT).show();
                                }
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
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getCMSSiteBanner+"?clientId="+GlobalVariables.clientID;
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
                                    CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(context,shopbannerarray,"1");
                                    androidGridView = (GridView) findViewById(R.id.grid_view_image_text);
                                    androidGridView.setVerticalScrollBarEnabled(false);
                                    androidGridView.setAdapter(adapterViewAndroid);
                                    commonMethods.setGridViewHeightBasedOnChildren(androidGridView,2);
                                    androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int i, long id) {

                                        }
                                    });
                                }
                                else {
                                    Toast.makeText( context,getString(R.string.noproductsfound) , Toast.LENGTH_SHORT).show();
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
    public void popup(JSONObject tempDict){
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.layout_pop_dialog, null);
        itemtitle = (TextView) alertLayout.findViewById(R.id.itemtitle);
        itemdes = (TextView) alertLayout.findViewById(R.id.itemdes);
        itemtitle.setText(tempDict.optString("name"));
        cancel_obj = (Button) alertLayout.findViewById(R.id.cancel_obj);
//        String htmlString = "\n<p>In the last post, I showed you how to <a href=\"https://renemorozowich.com/installing-and-configuring-the-astra-theme-for-wordpress/\">install the Astra theme and Astra child theme</a>. Today I want to talk more about Astra Starter Sites. </p>\n\n\n\n<iframe width=\"100%\" height=\"500\" src=\"https://www.youtube.com/embed/-4z6L2nVRhA?rel=0\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen=\"\"></iframe>\n\n\n\n<p>First let me say that you don&#8217;t <em>need</em> to use a starter site with Astra. You can definitely use Astra by itself or with a <a rel=\"noreferrer noopener\" aria-label=\"few block plugins (opens in a new tab)\" href=\"https://wordpress.org/plugins/search/blocks/\" target=\"_blank\">few block plugins</a>, but if you want to start with a pre-made design, check out their <a rel=\"noreferrer noopener\" aria-label=\"starter sites (opens in a new tab)\" href=\"https://wpastra.com/ready-websites/\" target=\"_blank\">starter sites</a>. </p>\n\n\n\n<p>A few notes about the starter sites:</p>\n\n\n\n<ul><li>Some will say Agency &#8212; those are for the paid version of Astra, so click the Free link to filter by free sites. I also like the Elementor page builder, so feel free to choose that as well.</li><li>There may not be a starter site &#8220;in your industry&#8221; and that&#8217;s totally okay. Don&#8217;t look so much at the industry because you&#8217;ll be customizing with your own text and photos &#8212; look by design and what speaks to you!</li><li>Click each of the demos that you like and <em>look through all the pages</em>. Find one that has most of what you want. The pro to using a starter site is that most of it is done for you. The con is when you start to customize TOO much and end up spending a lot of time tweaking.</li></ul>\n\n\n\n<h2>Install the importer plugin</h2>\n\n\n\n<p>Once you choose a demo that you like, install the importer plugin.</p>\n\n\n\n<figure class=\"wp-block-image\"><img src=\"https://renemorozowich.com/wp-content/uploads/2019/10/astra-starter-site.png\" alt=\"Import Starter Site\" class=\"wp-image-3756\" srcset=\"https://renemorozowich.com/wp-content/uploads/2019/10/astra-starter-site.png 602w, https://renemorozowich.com/wp-content/uploads/2019/10/astra-starter-site-216x300.png 216w\" sizes=\"(max-width: 602px) 100vw, 602px\" /><figcaption>Choose &#8220;Install Importer Plugin&#8221; under Astra Options</figcaption></figure>\n\n\n\n<h2>Choose your starter site</h2>\n\n\n\n<p>Once that is installed, go to Appearance, Astra Options, Astra Starter Sites. Choose your page builder (again, I like Elementor) then choose the starter site that you want to install. Then click Import Site.</p>\n\n\n\n<figure class=\"wp-block-image\"><img src=\"https://renemorozowich.com/wp-content/uploads/2019/10/outdoor-adventure.png\" alt=\"Outdoor Adventure starter site options\" class=\"wp-image-3757\" srcset=\"https://renemorozowich.com/wp-content/uploads/2019/10/outdoor-adventure.png 600w, https://renemorozowich.com/wp-content/uploads/2019/10/outdoor-adventure-157x300.png 157w, https://renemorozowich.com/wp-content/uploads/2019/10/outdoor-adventure-534x1024.png 534w\" sizes=\"(max-width: 600px) 100vw, 600px\" /><figcaption>Check the first four options and if you&#8217;ve imported another site already, check the last option too</figcaption></figure>\n\n\n\n<p>Once it&#8217;s done, click View Site. Some hosts have stronger caching, so you might have to refresh the page to see the new design.</p>\n\n\n\n<h2>The back end</h2>\n\n\n\n<p>Take a look around and see what happened.</p>\n\n\n\n<h3>Media</h3>\n\n\n\n<p>If you visit the media library, you&#8217;ll see that all of their images have been added. Add your own images here and swap them out when the time comes.</p>\n\n\n\n<h3>Posts</h3>\n\n\n\n<p>Depending on the starter site you chose, you may see some posts. I recommend adding your own posts and trashing the existing ones. Before removing, you may want to view existing posts to see how they look and/or edit to see the back end. You can also change the posts to draft instead of trashing &#8212; this will keep the posts on your site so that you can refer to them later, but they won&#8217;t show on the front end.</p>\n\n\n\n<h3>Pages</h3>\n\n\n\n<p>Astra also added the appropriate pages and filler content. I like to look at the page in one tab and edit the page (with Elementor) in another. Replace the filler text, customize with your own images and hide or remove sections that you don&#8217;t need or want. Update and see how it looks!</p>\n\n\n\n<p>Again, you don&#8217;t need to use Astra starter sites &#8212; you can use Astra right out of the box and create your own design. However, starter sites may save you time, especially if you&#8217;re not really strong in design.</p>\n";

        DynamicImageGetter.with(this)
                .load(tempDict.optString("banner_text").toString())
                .mode(0)
                .into(itemdes);

        dialog = new AlertDialog.Builder(context)
                .setView(alertLayout)
                .setTitle("")
                .create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        cancel_obj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                dialog.dismiss();
            }
        });
    }

}