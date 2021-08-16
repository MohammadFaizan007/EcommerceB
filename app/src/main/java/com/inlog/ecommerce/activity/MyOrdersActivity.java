package com.inlog.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.adapter.orderRecyclerViewAdapter;
import com.inlog.ecommerce.model.myorder;
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.util.InternetConnectionDetector;
import com.inlog.ecommerce.util.NothingSelectedSpinnerAdapter;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MyOrdersActivity extends AppCompatActivity implements MyListCallbackListner {
    Button back;
    private static int customerId;
    private RecyclerView recyclerView;
    private static Context mContext;
    private String responsefinal;
    JSONArray detailsarray;
    public  ArrayList<myorder> myorderlist;
    private orderRecyclerViewAdapter myorderAdapter;
    TextView txtVNoDataFound;
    private AlertDialog dialog;
    Float ratingvone,ratingvtwo,ratingvthree,ratingvfour;
    TextView reviewTextview;
    ImageView filter_obj;
    RadioGroup toggle;
    private RadioButton orders,cancled;
    String state;
    String orderperiod;
    Spinner spinneraAndp;
    String Selectedvalue;

    public  ArrayList<String> dropdownlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        mContext = this;
        SharedPreferences sharedPrefsLogin = mContext.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
        customerId = sharedPrefsLogin.getInt("globalid", 0);
        initViews();
        setupAdapter();
        Selectedvalue = "";
        state = "";
        MyProgressBar.getInstance().showProgressDialog(this);
        getmyorders();

    }

    private void initViews() {

        txtVNoDataFound = findViewById(R.id.txtVNoDataFound);
        TextView header = findViewById(R.id.header);
        toggle=(RadioGroup)findViewById(R.id.toggle);
        orders=(RadioButton)findViewById(R.id.orders);
        cancled=(RadioButton)findViewById(R.id.cancled);
        filter_obj=(ImageView)findViewById(R.id.filter_obj);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariables.checkorderpageroot.equals("1"))
                {
                    Intent i = new Intent(MyOrdersActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                    finish();
                }
                else
                {
                    onBackPressed();
                }

            }
        });
        filter_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dropdownlist = new ArrayList<>();
                dropdownlist.add("Last 1 Month");
                dropdownlist.add("Last 3 Months");
                dropdownlist.add("Last 6 Months");
                dropdownlist.add(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                dropdownlist.add(String.valueOf(getPreviousYear(-1)));
                dropdownlist.add(String.valueOf(getPreviousYear(-2)));
                dropdownlist.add("Older");

                PopupMenu popup = new PopupMenu(mContext, filter_obj);

                for (int i = 0; i < dropdownlist.size(); i++) {
                    popup.getMenu().add(dropdownlist.get(i).toString());
                }
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        Selectedvalue = item.getTitle().toString();
                        MyProgressBar.getInstance().showProgressDialog(mContext);
                        getmyorders();
                        return true;
                    }
                });

                popup.show();
            }
        });


        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InternetConnectionDetector internetCD = new InternetConnectionDetector(mContext);
                        if (internetCD.isConnectingToInternet()) {
                            state = "";
                            orders.setText("Orders");
                            orders.setTextColor(getResources().getColor(R.color.white));
                            cancled.setTextColor(getResources().getColor(R.color.black));
                            MyProgressBar.getInstance().showProgressDialog(mContext);
                            getmyorders();
                        }
                        else {
                            Toast.makeText(mContext, "No network", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        cancled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InternetConnectionDetector internetCD = new InternetConnectionDetector(mContext);
                        if (internetCD.isConnectingToInternet()) {
                            state = "Cancel";
                            orders.setTextColor(getResources().getColor(R.color.black));
                            cancled.setTextColor(getResources().getColor(R.color.white));
                            cancled.setText("Cancelled Orders");
                            MyProgressBar.getInstance().showProgressDialog(mContext);
                            getmyorders();

                        } else {
                            Toast.makeText(mContext, "No network", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
    private static int getPreviousYear(int value) {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, value);
        return prevYear.get(Calendar.YEAR);
    }
    private void setupAdapter() {
        recyclerView = findViewById(R.id.orderrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myorderlist = new ArrayList<>();
        myorderAdapter = new orderRecyclerViewAdapter(myorderlist,this,this);
        recyclerView.setAdapter(myorderAdapter);
    }
    public void getmyorders() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            String url;
        if (Selectedvalue.equals(""))
        {
            url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerShopOrder + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId +"&id=&lm=&year=&state="+state+"&older=";
        }
        else
        {
            if (Selectedvalue.equals("Last 1 Month"))
            {
                url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerShopOrder + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId +"&id=&lm="+1+"&year=&state="+state+"&older=";
            }
            else if (Selectedvalue.equals("Last 3 Months"))
            {
                url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerShopOrder + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId +"&id=&lm="+3+"&year=&state="+state+"&older=";
            }
            else if (Selectedvalue.equals("Last 6 Months"))
            {
                url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerShopOrder + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId +"&id=&lm="+6+"&year=&state="+state+"&older=";
            }
            else if (Selectedvalue.equals("Older"))
            {
               String Selectedvaluefinal = String.valueOf(dropdownlist.get(dropdownlist.size()-2));
                url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerShopOrder + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId +"&id=&lm=&year="+Selectedvaluefinal+"&state="+state+"&older=true";
            }
            else
            {
                url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerShopOrder + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId +"&id=&lm=&year="+Selectedvalue+"&state="+state+"&older=";
            }
        }
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
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                detailsarray = jsonObject.getJSONArray("result");
                                if (detailsarray.length() > 0) {
                                    myorderlist.clear();
                                    for (int i = 0; i < detailsarray.length(); i++) {
                                        JSONObject tempDict = (JSONObject) detailsarray.get(i);
                                        String order_received_date = "";
                                        String accepted_date = "";
                                        String ready_to_ship_date = "";
                                        String shipped_date = "";
                                        String out_for_delivery_date = "";
                                        String delivered_date= "";
                                        String cancel_date = "";
                                        String total_amount = "";

                                        if (tempDict.optString("order_received_date").equals("null"))
                                        {
                                            order_received_date = "";
                                        }
                                        else
                                        {
                                            order_received_date = tempDict.optString("order_received_date");
                                        }
                                        if (tempDict.optString("accepted_date").equals("null"))
                                        {
                                            accepted_date = "";
                                        }
                                        else
                                        {
                                            accepted_date = tempDict.optString("accepted_date");
                                        }
                                        if (tempDict.optString("ready_to_ship_date").equals("null"))
                                        {
                                            ready_to_ship_date = "";
                                        }
                                        else
                                        {
                                            ready_to_ship_date = tempDict.optString("ready_to_ship_date");
                                        }
                                        if (tempDict.optString("shipped_date").equals("null"))
                                        {
                                            shipped_date = "";
                                        }
                                        else
                                        {
                                            shipped_date = tempDict.optString("shipped_date");
                                        }
                                        if (tempDict.optString("out_for_delivery_date").equals("null"))
                                        {
                                            out_for_delivery_date = "";
                                        }
                                        else
                                        {
                                            out_for_delivery_date = tempDict.optString("out_for_delivery_date");
                                        }
                                        if (tempDict.optString("delivered_date").equals("null"))
                                        {
                                            delivered_date = "";
                                        }
                                        else
                                        {
                                            delivered_date = tempDict.optString("delivered_date");
                                        }
                                        if (tempDict.optString("cancel_date").equals("null"))
                                        {
                                            cancel_date = "";
                                        }
                                        else
                                        {
                                            cancel_date = tempDict.optString("cancel_date");
                                        }
                                        if (tempDict.optString("total_amount").equals("null"))
                                        {
                                            total_amount = "";
                                        }
                                        else
                                        {
                                            total_amount = "₹"+tempDict.optString("total_amount");
                                        }
                                        myorder orderItem = new myorder(tempDict.optInt("shop_order_id"),
                                                tempDict.optInt("order_id"),
                                                tempDict.optString("name"),
                                                tempDict.optString("state"),
                                                order_received_date,
                                                accepted_date,
                                                ready_to_ship_date,
                                                shipped_date,
                                                out_for_delivery_date,
                                                delivered_date,
                                                cancel_date,
                                                total_amount,
                                                tempDict,myorderlist,
                                                tempDict.optInt("overall_shop_rating"),
                                                tempDict.optInt("delivery_rating"),
                                                tempDict.optInt("professional_rating"),
                                                tempDict.optInt("good_quality_rating"),
                                                tempDict.optInt("responsive_rating"));
                                        myorderlist.add(orderItem);
                                    }

                                    myorderAdapter.notifyDataSetChanged();

                                }
                                else {
                                    myorderlist.clear();
                                }
                                checkEmptyList(myorderlist);
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
    private void checkEmptyList(ArrayList<myorder> productlist) {
        if(productlist!=null && productlist.size()>0){
            txtVNoDataFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            txtVNoDataFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position, int tag) {
        myorder order = new myorder();
        myorder orderitem = order.getorderitem(position);

//        SharedPreferences sharedPref = getSharedPreferences("orderdictionary", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("orderDict", orderitem.getTempDict().toString());
//        editor.apply();

        Intent i = new Intent(MyOrdersActivity.this, orderdetailsActivity.class);
        i.putExtra("order_id", orderitem.getOrder_id());
        i.putExtra("getShop_order_id", orderitem.getShop_order_id());
        startActivity(i);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
        finish();
    }
    public void reviewpopup(JSONObject tempDict){
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.layout_pop_shaoprating_dailog, null);
        Button cancel_obj = alertLayout.findViewById(R.id.cancel_obj);
        Button cirsubmitButton = alertLayout.findViewById(R.id.cirsubmitButton);
        RatingBar ratingone = alertLayout.findViewById(R.id.ratingone);
        RatingBar ratingtwo = alertLayout.findViewById(R.id.ratingtwo);
        RatingBar ratingthree = alertLayout.findViewById(R.id.ratingthree);
        RatingBar ratingfour = alertLayout.findViewById(R.id.ratingfour);
        reviewTextview = alertLayout.findViewById(R.id.reviewTextview);
        dialog = new AlertDialog.Builder(mContext)
                .setView(alertLayout)
                .setTitle("")
                .create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ratingone.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingvone = rating;
            }
        });
        ratingtwo.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingvtwo = rating;
            }
        });
        ratingthree.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingvthree = rating;
            }
        });
        ratingfour.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingvfour = rating;
            }
        });
        cancel_obj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                dialog.dismiss();
            }
        });
        cirsubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){

                createShopRating(tempDict);
                dialog.dismiss();
            }
        });
    }
    public void createShopRating(JSONObject shaopdict) {
        MyProgressBar.getInstance().showProgressDialog(this);


        if (ratingvone == null)
        {
            ratingvone = (float) 0.0;
        }
        if (ratingvtwo == null)
        {
            ratingvtwo = (float) 0.0;
        }
        if (ratingvthree == null)
        {
            ratingvthree = (float) 0.0;
        }
        if (ratingvfour == null)
        {
            ratingvfour = (float) 0.0;
        }

        if (ratingvone == null && ratingvtwo == null && ratingvthree == null && ratingvfour == null)
        {
            Toast.makeText(mContext,"Please add Rating", Toast.LENGTH_SHORT).show();

            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject mainobj = new JSONObject();
        try {
            //input your API parameters
            object.put("clientId",GlobalVariables.clientID);
            object.put("consumerId",customerId);
            object.put("consumer",null);
            object.put("shopId",shaopdict.optInt("shop_id"));
            object.put("orderId",shaopdict.optInt("order_id"));
            object.put("shopOrderId",shaopdict.optInt("shop_order_id"));
            object.put("header","");
            object.put("description",reviewTextview.getText().toString());
            object.put("deliveryRating",ratingvone);
            object.put("professionalRating",ratingvtwo);
            object.put("goodQualityRating",ratingvthree);
            object.put("responsiveRating",ratingvfour);
            float averagerating = (ratingvone + ratingvtwo+ratingvthree+ratingvfour)/4;
            object.put("avgRating",averagerating);
            mainobj.put("params", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.createShopRating, mainobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("String Response : ", response.toString());
                        Utility.log("Response = ",response.toString());

                        responsefinal = null;
                        responsefinal = response.toString();
                        final JSONObject jsonObject;
                        if (responsefinal != null) {
                            try {

                                jsonObject = new JSONObject(responsefinal);
                                JSONObject result = jsonObject.getJSONObject("result");
                                if (result.optString("status").equalsIgnoreCase("OK")) {

                                    getmyorders();
                                }
                                else
                                {
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                }
                            });
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressBar.getInstance().dismissProgressDialog();

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}