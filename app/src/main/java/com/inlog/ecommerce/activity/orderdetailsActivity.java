package com.inlog.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.fragments.SummaryFragment;
import com.inlog.ecommerce.fragments.ItemsFragment;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.myorder;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.CartManager;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.zjy.actionsheet.ActionSheet;
import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

public class orderdetailsActivity extends AppCompatActivity {
    Button back,doted_obj;
    JSONObject orderDict;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String responsefinal;
    private JSONArray detailsarray;
    private static int customerId;
    private static Context mContext;
    int orderid;
    int shoporderid;
    private AlertDialog dialog;
    Float ratingvone,ratingvtwo,ratingvthree,ratingvfour;
    TextView reviewTextview;
    boolean checkrating= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        mContext = this;
        SharedPreferences sharedPrefsLogin = mContext.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
        customerId = sharedPrefsLogin.getInt("globalid", 0);
        initViews();
    }
    private void initViews() {

        back = findViewById(R.id.back);
        doted_obj = findViewById(R.id.doted_obj);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(orderdetailsActivity.this, MyOrdersActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                finish();
            }
        });

        doted_obj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ActionSheet actionSheet = new ActionSheet.Builder()
                            .setTitle("Select Option", Color.GRAY)
                            .setOtherBtn(new String[]{"Cancel Order","Report"}, new int[]{Color.BLACK,Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK})
                            .setOtherBtnTextSize(16)
                            .setOtherBtnSub(new String[]{null, null, null, null}, new int[]{Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK})
                            .setCancelBtn("Cancel", Color.RED)
                            .setCancelableOnTouchOutside(true)
                            .setActionSheetListener(new ActionSheet.ActionSheetListener() {
                                @Override
                                public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {

                                }
                                @Override
                                public void onButtonClicked(ActionSheet actionSheet, int index) {
                                    if (index == 0)
                                    {
                                        new AndExAlertDialog.Builder(orderdetailsActivity.this)
                                                .setTitle("Confirm delete")
                                                .setMessage("Are you sure you want to delete this file?")
                                                .setPositiveBtnText("Yes")
                                                .setNegativeBtnText("No")
                                                .setImage(R.drawable.delete,15)
                                                .setCancelableOnTouchOutside(true)
                                                .OnPositiveClicked(new AndExAlertDialogListener() {
                                                    @Override
                                                    public void OnClick(String input) {
                                                        cancelorder();
                                                    }
                                                })
                                                .OnNegativeClicked(new AndExAlertDialogListener() {
                                                    @Override
                                                    public void OnClick(String input) {

                                                    }
                                                })
                                                .build();
                                    }
                                }
                            }).build();

                    actionSheet.show(getFragmentManager());
                }
        });
        Intent intent = getIntent();
        orderid = intent.getIntExtra("order_id",0);
        shoporderid = intent.getIntExtra("getShop_order_id",0);
        MyProgressBar.getInstance().showProgressDialog(this);
        getConsumerShopOrderDetails();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

    }
    private void cancelorder() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.cancelShopOrder;
            HashMap<String,Object> paramSMap = new  HashMap<String,Object>();
            HashMap<String,Object> paramSMapmain = new  HashMap<String,Object>();
            paramSMap.put("clientId",GlobalVariables.clientID);
            paramSMap.put("shopOrderId",orderDict.optInt("shop_order_id"));
            paramSMap.put("orderId",orderDict.optInt("order_id"));
            paramSMapmain.put("params",paramSMap);

            JSONObject requetBody = new JSONObject(paramSMapmain);
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
                                Toast.makeText(orderdetailsActivity.this, jsonObject.optString("result"), Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(orderdetailsActivity.this, MyOrdersActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                                finish();
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
    public void getConsumerShopOrderDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerShopOrderDetails + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId +"&shopOrderId="+shoporderid+"&orderId="+orderid;
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
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                detailsarray = jsonObject.getJSONArray("result");

                                orderDict = detailsarray.getJSONObject(0);

                                if (orderDict.optString("state").equals("Delivered") || orderDict.optString("state").equals("Cancel"))
                                {
                                    doted_obj.setVisibility(View.GONE);
                                }
                                else {
                                    doted_obj.setVisibility(View.VISIBLE);
                                }
                                setupViewPager(viewPager);
                                tabLayout.setupWithViewPager(viewPager);
                                if (checkrating == true)
                                {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                    checkrating = false;
                                }
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
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SummaryFragment(), "Summery");
        adapter.addFragment(new ItemsFragment(), "Items");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            Bundle data = new Bundle();
            data.putString("data", orderDict.toString());
            fragment.setArguments(data);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void reviewpopup(JSONObject tempDict){
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.layout_pop_productrating_dailog, null);
        Button cancel_obj = alertLayout.findViewById(R.id.cancel_obj);
        Button cirsubmitButton = alertLayout.findViewById(R.id.cirsubmitButton);
        RatingBar ratingone = alertLayout.findViewById(R.id.ratingone);
        RatingBar ratingtwo = alertLayout.findViewById(R.id.ratingtwo);
        RatingBar ratingthree = alertLayout.findViewById(R.id.ratingthree);
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

                createproductrating(tempDict);
                dialog.dismiss();
            }
        });
    }
    public void createproductrating(JSONObject shaopdict) {
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

        if (ratingvone == null && ratingvtwo == null && ratingvthree == null)
        {
            Toast.makeText(mContext,"Please add Rating", Toast.LENGTH_SHORT).show();

            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject mainobj = new JSONObject();
        try {

//            tempDict
            //input your API parameters
            object.put("clientId",GlobalVariables.clientID);
            object.put("consumerId",customerId);
            object.put("consumer",null);
            object.put("shopId",shaopdict.optInt("shop_id"));
            object.put("productId",shaopdict.optInt("product_id"));
            object.put("productVariantId",shaopdict.optInt("productVariantId"));
            object.put("orderId",orderDict.optInt("order_id"));
            object.put("shopOrderId",orderDict.optInt("shop_order_id"));
            object.put("header","");
            object.put("description",reviewTextview.getText().toString());
            object.put("qualityRating",ratingvone);
            object.put("meetExpectationRating",ratingvtwo);
            object.put("valueForMoneyRating",ratingvthree);
            float averagerating = (ratingvone + ratingvtwo+ratingvthree)/3;
            object.put("avgRating",averagerating);
            mainobj.put("params", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.createProductRating, mainobj,
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
                                    Toast.makeText(orderdetailsActivity.this, "Thanks For Your Feedback", Toast.LENGTH_SHORT).show();
                                    checkrating = true;
                                    getConsumerShopOrderDetails();
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
