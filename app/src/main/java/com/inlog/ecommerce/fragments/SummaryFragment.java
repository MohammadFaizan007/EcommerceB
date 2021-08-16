package com.inlog.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.activity.orderdetailsActivity;
import com.inlog.ecommerce.model.myorder;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class SummaryFragment extends Fragment {
    TextView date, orderstatus, terms, name, address, phoneno;
    TextView invoiceno, Orderno, paymentby, orderitems, subtotal, dcharges, wallet, total;
    private JSONObject itemDict;
    private int customerId;
    private static orderdetailsActivity mActivity;
    private String responsefinal;
    private JSONArray detailsarray;
    private JSONArray addressarray;

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (orderdetailsActivity) getActivity();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        date = (TextView) view.findViewById(R.id.date);
//        slot = (TextView) view.findViewById(R.id.slot);
        orderstatus = (TextView) view.findViewById(R.id.orderstatus);
        terms = (TextView) view.findViewById(R.id.terms);
        name = (TextView) view.findViewById(R.id.name);
        address = (TextView) view.findViewById(R.id.address);
        phoneno = (TextView) view.findViewById(R.id.phoneno);

        invoiceno = (TextView) view.findViewById(R.id.invoiceno);
        Orderno = (TextView) view.findViewById(R.id.Orderno);
        paymentby = (TextView) view.findViewById(R.id.paymentby);
        orderitems = (TextView) view.findViewById(R.id.orderitems);
        subtotal = (TextView) view.findViewById(R.id.subtotal);
        dcharges = (TextView) view.findViewById(R.id.dcharges);
        wallet = (TextView) view.findViewById(R.id.wallet);
        total = (TextView) view.findViewById(R.id.total);

        if (getArguments() != null) {
            String getArgument = getArguments().getString("data");
            try {
                itemDict = new JSONObject(getArgument);
                detailsarray = new JSONArray();
                detailsarray = itemDict.getJSONArray("order_lines");

                date.setText(String.format("Order Status %s", itemDict.getString("order_received_date")));
                orderstatus.setText(itemDict.getString("state"));


                name.setText(itemDict.getString("sa_name"));
                address.setText(String.format("%s, \n%s, \n%s, \n%s, \n%s", itemDict.getString("sa_street"), itemDict.getString("sa_city"), itemDict.getString("sa_state"), itemDict.getString("sa_country"), itemDict.getString("sa_zip")));
                phoneno.setText(itemDict.getString("sa_mobile"));

                invoiceno.setText("");
                Orderno.setText(itemDict.getString("name"));
                paymentby.setText(itemDict.getString("payment_method"));
                orderitems.setText(String.format("%d Items", detailsarray.length()));

                subtotal.setText("");
                dcharges.setText("0");
                wallet.setText("0");
                total.setText(itemDict.getString("total_amount"));

//                getaddress();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("", getArgument);
        }
        return view;
    }

    private void getaddress() {
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        try {
            MyProgressBar.getInstance().showProgressDialog(mActivity);
            SharedPreferences sharedPrefsLogin = mActivity.getSharedPreferences("globalloginvalues", MODE_PRIVATE);
            customerId = sharedPrefsLogin.getInt("globalid", 0);
            String url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerProfile + "?clientId=" + GlobalVariables.clientID + "&id=&consumerId=" + customerId;
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
                            addressarray = new JSONArray();
                            if (jsonObject.optString("status").equalsIgnoreCase("OK")) {
                                addressarray = jsonObject.getJSONArray("result");
                                if (addressarray.length() > 0) {

                                    String namestr = "";
                                    String mobilenostr = "";

                                    for (int i = 0; i < addressarray.length(); i++) {
                                        JSONObject tempDict = (JSONObject) addressarray.get(i);
                                        namestr = tempDict.optString("name");
                                        mobilenostr = tempDict.optString("mobile");
                                    }

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

    private void updateaddress(String name, String mobile) {

    }

}