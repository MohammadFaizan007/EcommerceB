package com.inlog.ecommerce.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.adapter.AdapterReddeemPoints;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.RedeemPoints;
import com.inlog.ecommerce.myaccount.model.AddressItem;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.adapter.CartRecyclerViewAdapter;
import com.inlog.ecommerce.utility.CartManager;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;

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

import ir.androidexception.andexalertdialog.AndExAlertDialog;
import ir.androidexception.andexalertdialog.AndExAlertDialogListener;

import static android.content.ContentValues.TAG;

public class CartListActivity extends AppCompatActivity {
    Button back;
    String checkroot;
    private static Context mContext;
    String responsefinal;
    JSONArray detailsarray = null;
    JSONArray totalarray = null;
    public static ArrayList<Cart> productlist;
    private RecyclerView recyclerView;
    private static int customerId;
    JSONObject totalDict;
    TextView text_action_bottom2,txvChangeAddressId;
    AddressItem selectedAddress = null;
    private static CartRecyclerViewAdapter adapter;
    boolean isredeemed;
    TextView txtVRedeemPointsId,txtVcancelRedeemPointsId;
    TextView txtVIdRedeemPoints,txtVRedeemWorthId;
    LinearLayout layoutCartItems,layoutCartNoItems;
    NestedScrollView layoutCartPayments;
    RelativeLayout layout_payment;
    TextView totalcount;
    CardView redeemvc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        mContext = CartListActivity.this;
        back = findViewById(R.id.back);
        txvChangeAddressId = findViewById(R.id.txvChangeAddressId);
        txtVIdRedeemPoints = findViewById(R.id.txtVIdRedeemPoints);
        txtVRedeemWorthId = findViewById(R.id.txtVRedeemWorthId);
        txtVRedeemPointsId = findViewById(R.id.txtVRedeemPointsId);
        txtVcancelRedeemPointsId = findViewById(R.id.txtVcancelRedeemPointsId);
        txtVRedeemPointsId.setVisibility(View.VISIBLE);
        txtVcancelRedeemPointsId.setVisibility(View.GONE);

        layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
        layoutCartPayments = (NestedScrollView) findViewById(R.id.nestedScrollView);
        layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);
       layout_payment = (RelativeLayout) findViewById(R.id.layout_payment);
        totalcount = (TextView) findViewById(R.id.text_action_bottom1);

        redeemvc = (CardView) findViewById(R.id.redeemvc);
        layoutCartNoItems.setVisibility(View.GONE);
        txvChangeAddressId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakePaymentActivity.Companion.startActivity(CartListActivity.this,"");
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);

            }
        });

        Intent intent = getIntent();
        checkroot = intent.getStringExtra("movepage");
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(recylerViewLayoutManager);

        text_action_bottom2 = findViewById(R.id.text_action_bottom2);
        text_action_bottom2.setText(R.string.checkout);
        text_action_bottom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent1 = new Intent(CartListActivity.this,CheckoutActivity.class);
                intent1.putExtra(CheckoutActivity.IS_REDEEMED,isredeemed);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);


//                    makeOrder();
            }
        });
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                goToPreviousScreen();
            }

        });

        populateRedeemPointsList(null, "", "");
    }

    private void goToPreviousScreen() {

        Intent i;
       if(checkroot.equals("3")){
           finish();
       }
       else if (checkroot.equals("1")  )
        {
            i = new Intent(CartListActivity.this, ProductListActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            i = new Intent(CartListActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    private void populateRedeemPointsList(ArrayList<RedeemPoints> redeemList, String total_reward_amount, String total_reward_points) {
        LinearLayout llRewardPoints = findViewById(R.id.llRewardPoints);

        if(redeemList!=null && redeemList.size()>0) {
            llRewardPoints.setVisibility(View.VISIBLE);

            if (total_reward_points.equals("0"))
            {
                redeemvc.setVisibility(View.GONE);
            }
            else
            {
                redeemvc.setVisibility(View.VISIBLE);
            }
            txtVIdRedeemPoints.setText("Total Reward Points: "+total_reward_points);
            txtVRedeemWorthId.setText("Points worth: "+Utility.appendCurrencyWithText(total_reward_amount));
            RecyclerView recyclRedeemPoints = findViewById(R.id.recyclRedeemPoints);
            recyclRedeemPoints.setLayoutManager(new LinearLayoutManager(mContext));
            recyclRedeemPoints.setAdapter(new AdapterReddeemPoints(redeemList));

            txtVRedeemPointsId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AndExAlertDialog.Builder(CartListActivity.this)
                            .setTitle("Confirm Redeem Points")
                            .setMessage("Are you sure you want to Redeem Points?")
                            .setPositiveBtnText("Yes")
                            .setNegativeBtnText("No")
                            .setCancelableOnTouchOutside(true)
                            .OnPositiveClicked(new AndExAlertDialogListener() {
                                @Override
                                public void OnClick(String input) {
                                    isredeemed = true;
                                    hitApiTogetRedeemPoints();
                                }
                            })
                            .OnNegativeClicked(new AndExAlertDialogListener() {
                                @Override
                                public void OnClick(String input) {

                                }
                            })
                            .build();

                }
            });

            txtVcancelRedeemPointsId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isredeemed = false;
                    MyProgressBar.getInstance().showProgressDialog(CartListActivity.this);
                    getCartdata();
                    txtVRedeemPointsId.setVisibility(View.VISIBLE);
                    txtVcancelRedeemPointsId.setVisibility(View.GONE);
                    txtVRedeemPointsId.setText("Redeem Points");
                }
            });
        }
        else
            llRewardPoints.setVisibility(View.GONE);
    }

    private void hitApiTogetRedeemPoints() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            SharedPreferences sharedPrefsLogin = getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE);
            int customerId = sharedPrefsLogin.getInt("globalid", 0);

            JSONObject jsonObj = new JSONObject();
            JSONObject mainJsonObj = new JSONObject();
            jsonObj.put("clientId",GlobalVariables.clientID);
            jsonObj.put("consumerId",customerId);
            jsonObj.put("grandTotal", totalDict.optString("grand_total"));
            jsonObj.put("type","Cart");

            mainJsonObj.put("params",jsonObj);
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.redeemConsumerPoints;

            Utility.log("URL ", url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, mainJsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    getCartdata();
                    responsefinal = null;
                    responsefinal = response.toString();
                    Utility.log("Api URL = ", url);
                    Utility.log("Api Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {
                        try {
                            jsonObject = new JSONObject(responsefinal);
                            JSONObject result = jsonObject.getJSONObject("result");
                            if (result.optString("status").equalsIgnoreCase("OK"))
                            {
                                Toast.makeText(mContext,
                                        "Redeemed Points Successfully",
                                        Toast.LENGTH_LONG).show();
                                txtVRedeemPointsId.setVisibility(View.GONE);
                                txtVcancelRedeemPointsId.setVisibility(View.VISIBLE);
                                txtVcancelRedeemPointsId.setText("Cancel");
                            }
                            else
                            {
                                Toast.makeText(mContext,
                                        "Please Try Later",
                                        Toast.LENGTH_LONG).show();
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
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hitApiTogetCustomerAddress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MakePaymentActivity.Companion.getREQUEST_CODE()){
            if(data!=null) {
                selectedAddress = (AddressItem) data.getSerializableExtra(MakePaymentActivity.Companion.getEXTRA_SELECTED_ADDRESS());
                if (selectedAddress != null) {
                    populateDefaultAddres(selectedAddress);
                }
            }
        }
    }



//    https://uat.inlognetwork.co.in/getConsumerCart?clientId=ODOO90bddb3e-cdd8-11ea-8954-48d2245124b6&consumerId=26&type=Cart&index=0&limit=5000

    @SuppressLint("SetTextI18n")
    protected void setCartLayout()
    {
        TextView priceitems = (TextView) findViewById(R.id.priceitems);
        TextView priceitemsval = (TextView) findViewById(R.id.priceitemsval);
        TextView txtVTaxVat = (TextView) findViewById(R.id.txtVTaxVat);
        TextView txtVTotalRedeemPoints = (TextView) findViewById(R.id.txtVTotalRedeemPoints);
        TextView discount = (TextView) findViewById(R.id.discount);
        TextView txtVTotalSavingOnThisCard = (TextView) findViewById(R.id.txtVTotalSavingOnThisCard);
        TextView deliverychanges = (TextView) findViewById(R.id.deliverychanges);
        TextView totalamt = (TextView) findViewById(R.id.totalamt);


        if(CartManager.getInstance().getCartSize() >0){
            layoutCartNoItems.setVisibility(View.GONE);
            layoutCartItems.setVisibility(View.VISIBLE);
            layoutCartPayments.setVisibility(View.VISIBLE);
            layout_payment.setVisibility(View.VISIBLE);
            totalcount.setText(String.format("Total\n₹%s", totalDict.optString("grand_total").toString()));
            priceitems.setText(String.format("Price(%sitems)", totalDict.optString("total_items")));
            priceitemsval.setText(String.format("₹%s", totalDict.optString("total_price").toString()));
            txtVTaxVat.setText(String.format("₹%s", totalDict.optString("total_tax").toString()));
            txtVTotalRedeemPoints.setText(totalDict.optString("total_consume_points"));
            txtVTotalSavingOnThisCard.setText(getString(R.string.your_total_savings_on_this_card) + " "+Utility.appendCurrencyWithText(totalDict.optString("total_consume_amount")));
            discount.setText(String.format("₹%s", totalDict.optString("total_discount").toString()));;
            discount.setTextColor(getResources().getColor(R.color.green_light));
            if (totalDict.optString("delivery_charges").toString().equals("0"))
            {
                deliverychanges.setText("FREE");
                deliverychanges.setTextColor(getResources().getColor(R.color.green_light));
            }
            else
            {
                deliverychanges.setText(totalDict.optString("delivery_charges").toString());
                deliverychanges.setTextColor(Color.RED);
            }
            totalamt.setText(String.format("₹%s", totalDict.optString("grand_total").toString()));

        }
        else {
            layoutCartNoItems.setVisibility(View.VISIBLE);
            layoutCartItems.setVisibility(View.GONE);
            layoutCartPayments.setVisibility(View.GONE);
            layout_payment.setVisibility(View.GONE);

            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkroot.equals("1"))
                    {
                        Intent i = new Intent(CartListActivity.this, ProductListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(CartListActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                        finish();
                    }
                }
            });
        }
    }
    public void getCartdata()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {

            SharedPreferences sharedPrefsLogin = CartListActivity.this.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
            customerId = sharedPrefsLogin.getInt("globalid",0);
            String url = GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.getConsumerCart+"?clientId="+GlobalVariables.clientID+"&consumerId=" +customerId+ "&type=Cart&index=0&limit=100&IsRedeem="+isredeemed;
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
                            CartManager.getInstance().getCartList().clear();
                            jsonObject = new JSONObject(responsefinal);
                            detailsarray = new JSONArray();
                            totalarray =  new JSONArray();
                            JSONObject result = jsonObject.getJSONObject("result");
                            if (jsonObject.optString("status").equalsIgnoreCase("OK"))
                            {
                                detailsarray = result.getJSONArray("cart");
                                totalarray = result.getJSONArray("total");
                                totalDict = totalarray.getJSONObject(0);
//                                CartManager.getInstance().setTotalQty(totalDict.getInt("total_qty"));


                                if (detailsarray.length() > 0)
                                {
                                    productlist =  new ArrayList<>();
                                    for (int i = 0; i < detailsarray.length(); i++) {
                                        JSONObject tempDict = (JSONObject) detailsarray.get(i);

                                        Cart cartItem = new Cart(tempDict.optInt("id"),
                                                tempDict.optInt("product_id"),
                                                tempDict.optInt("product_variant_id"),
                                                tempDict.optString("product_name"),
                                                tempDict.optString("qty"),
                                                tempDict.optString("price"),
                                                tempDict.optString("subtotal"),
                                                tempDict.optString("product_variant"),
                                                tempDict.optInt("shop_id"),
                                                tempDict.optString("shop"), tempDict.optString("type"),
                                                tempDict.optString("image"), productlist,
                                                tempDict.optBoolean("is_combo"));
                                        productlist.add (cartItem);
                                        CartManager.getInstance().addCartItem(cartItem);
                                    }
                                    adapter = new CartRecyclerViewAdapter(recyclerView,productlist,CartListActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                }
                                else
                                {
                                    MyProgressBar.getInstance().dismissProgressDialog();

                                }
                                setCartLayout();

                                JSONObject reward_data = result.optJSONObject("reward_data");
                                JSONObject reward_total = reward_data.optJSONObject("reward_total");
                                JSONArray rewards = reward_data.optJSONArray("rewards");
                                if(rewards!=null && rewards.length()>0){
                                    ArrayList<RedeemPoints> redeemList = new ArrayList<>();
                                    for (int l = 0;l<rewards.length();l++){
                                        JSONObject jsonObj = rewards.getJSONObject(l);
                                        redeemList.add(new RedeemPoints(jsonObj.optString("shop_name"),jsonObj.optString("shop_reward_points"),Utility.appendCurrencyWithText(jsonObj.optString("shop_reward_amount"))));
                                    }
                                    String total_reward_amount = "";
                                    String total_reward_points = "";
                                    if(reward_total!=null){
                                         total_reward_amount = reward_total.optString("total_reward_amount");
                                         total_reward_points = reward_total.optString("total_reward_points");
                                    }
                                    populateRedeemPointsList(redeemList,total_reward_amount,total_reward_points);
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
    public void deleteCartItem(Cart cartItem, int position)
    {
        try {
            MyProgressBar.getInstance().showProgressDialog(CartListActivity.mContext);
            new DeleteCartitem(cartItem.getId(),position,cartItem).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class DeleteCartitem extends AsyncTask<String, Void, String> {
        int cartid,pos;
        Cart Cartitem;

        public DeleteCartitem(int id,int poss,Cart cartItem) {
            cartid = id;
            pos = poss;
            Cartitem = cartItem;
        }

        protected void onPreExecute() {

        }

        @SuppressLint("WrongThread")
        protected String doInBackground(String... arg0) {
            String resultFinal  = null;
            resultFinal = deletedata();
            final JSONObject jsonObj = null;

            if (resultFinal != null) {

                final JSONObject jsonObject;
                if (resultFinal != null) {
                    try {
                        jsonObject = new JSONObject(resultFinal);

                        JSONObject result = jsonObject.getJSONObject("result");
                        if (result.optString("status").equalsIgnoreCase("OK"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,
                                            result.optString("result"),
                                            Toast.LENGTH_LONG).show();
                                    removeFromList(pos);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");

                Toast.makeText(mContext,
                        "Couldn't get json from server. Check LogCat for possible errors!",
                        Toast.LENGTH_LONG).show();
                MyProgressBar.getInstance().dismissProgressDialog();

            }
            return null;
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

    public void removeFromList(int pos) {
        if(productlist.size()>pos) {
            Cart cartItem = productlist.get(pos);
            Cart.removeMyCard(pos);
            CartManager.getInstance().removeCartItem(cartItem);
            adapter.notifyDataSetChanged();
            getCartdata();
        }
        else
        {
            MyProgressBar.getInstance().dismissProgressDialog();
        }
    }

    private void hitApiTogetCustomerAddress() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            MyProgressBar.getInstance().showProgressDialog(this);
            SharedPreferences sharedPrefsLogin = getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE);
            int customerId = sharedPrefsLogin.getInt("globalid", 0);

            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerAddress+"?clientId="+ GlobalVariables.clientID+"&id=&consumerId="+customerId;

            Utility.log("URL ", url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    isredeemed = false;
                    getCartdata();
                    responsefinal = null;
                    responsefinal = response.toString();
                    Utility.log("Response = ", responsefinal);
                    final JSONObject jsonObject;
                    if (responsefinal != null) {

                        try {
                            jsonObject = new JSONObject(responsefinal);
                            detailsarray = new JSONArray();
                            totalarray =  new JSONArray();
                            JSONArray result = jsonObject.getJSONArray("result");
                            ArrayList<AddressItem> addressList = new ArrayList<AddressItem>();
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                               for (int i=0;i<result.length();i++){
                                   JSONObject item = result.getJSONObject(i);
                                   AddressItem addessItem = new AddressItem();

                                   addessItem.setCity(item.optString("city"));
                                   addessItem.setCity_id(item.optString("city_id"));
                                   addessItem.setCountry(item.optString("country"));
                                   addessItem.setEmail(item.optString("email"));
                                   addessItem.setId(item.optString("id"));
                                   addessItem.setLandmark(item.optString("landmark"));
                                   addessItem.setMobile(item.optString("mobile"));
                                   addessItem.setName(item.optString("name"));
                                   addessItem.setState(item.optString("state"));
                                   addessItem.setState_id(item.optString("state_id"));
                                   addessItem.setStreet(item.optString("street"));
                                   addessItem.setStreet2(item.optString("street2"));
                                   addessItem.setZip(item.optString("zip"));
                                   addessItem.setIsdefaultaddress(item.optBoolean("isdefaultaddress"));
                                   addressList.add(addessItem);

                                   if(addessItem.isIsdefaultaddress()){
                                       selectedAddress = addessItem;
                                       break;
                                   }
                               }

                               if(addressList.size()>0 && selectedAddress ==null)
                                   selectedAddress = addressList.get(0);

                               populateDefaultAddres(selectedAddress);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getCartdata();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
    private void populateDefaultAddres(AddressItem selectedAddress) {
       TextView address1 =  findViewById(R.id.address1);
       TextView address2 =  findViewById(R.id.address2);
       TextView address3 =  findViewById(R.id.address3);
       if(selectedAddress!=null){
           address1.setText(selectedAddress.getStreet() +", "+selectedAddress.getStreet2());
           address2.setText(selectedAddress.getLandmark());
           address3.setText(selectedAddress.getCity()+"-"+selectedAddress.getZip());
       }
    }

    @Override
    public void onBackPressed() {
        goToPreviousScreen();
        super.onBackPressed();
    }
}
