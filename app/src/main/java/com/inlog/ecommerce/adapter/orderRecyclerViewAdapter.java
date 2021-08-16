package com.inlog.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.activity.MainActivity;
import com.inlog.ecommerce.activity.MyOrdersActivity;
import com.inlog.ecommerce.model.Cart;
import com.inlog.ecommerce.model.Wishlist;
import com.inlog.ecommerce.model.myorder;
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.util.rating.AndRatingBar;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.ShowAlert;
import com.inlog.ecommerce.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.inlog.ecommerce.R.drawable.ic_accepted;
import static com.inlog.ecommerce.R.drawable.ic_cancel;
import static com.inlog.ecommerce.R.drawable.ic_complete;
import static com.inlog.ecommerce.R.drawable.ic_schedule;
import static com.inlog.ecommerce.R.drawable.ic_shipped;
import static com.inlog.ecommerce.activity.WishlistActivity.ADD_CART;

public class orderRecyclerViewAdapter
        extends RecyclerView.Adapter<orderRecyclerViewAdapter.ViewHolder> {
    private  MyListCallbackListner listnerCallBack;
    long lastClickTime = 0;
    public  ArrayList<myorder> productDetails;
    private Activity activityContext;

    public orderRecyclerViewAdapter(ArrayList<myorder> productlist, Activity mcontext, MyListCallbackListner listnerCallBack) {
        this.productDetails = productlist;
        this.listnerCallBack = listnerCallBack;
        activityContext = mcontext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView image;
        public final TextView orderreceiveddate,date, orderno, amount,noitems,orderstate;
        private final LinearLayout mainlayout;
        private final Button shopratingobj;
        RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            image = (ImageView) view.findViewById(R.id.image);
            orderreceiveddate =  (TextView) view.findViewById(R.id.orderreceiveddate);
            date = (TextView) view.findViewById(R.id.date);
            orderno = (TextView) view.findViewById(R.id.orderno);
            amount = (TextView) view.findViewById(R.id.amount);
            noitems = (TextView) view.findViewById(R.id.noitems);
            orderstate = (TextView) view.findViewById(R.id.orderstate);
            mainlayout = (LinearLayout) view.findViewById(R.id.mainlayout);
            shopratingobj = (Button) view.findViewById(R.id.shopratingobj);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }


    @Override
    public orderRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_myorder_item, parent, false);
        return new orderRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onViewRecycled(orderRecyclerViewAdapter.ViewHolder holder) {

    }
    @SuppressLint({"UseCompatLoadingForDrawables", "DefaultLocale"})
    @Override
    public void onBindViewHolder(final orderRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.orderreceiveddate.setText(productDetails.get(position).getOrder_received_date());
        holder.ratingBar.setVisibility(View.GONE);
        holder.shopratingobj.setVisibility(View.GONE);
        if (productDetails.get(position).getState().equals("Order Received"))
        {
            holder.image.setImageDrawable(activityContext.getResources().getDrawable(ic_schedule));
            holder.date.setText(productDetails.get(position).getOrder_received_date());
        }
        else if (productDetails.get(position).getState().equals("Delivered"))
        {
            holder.image.setImageDrawable(activityContext.getResources().getDrawable(ic_complete));
            holder.date.setText(productDetails.get(position).getDelivered_date());


            if (productDetails.get(position).getOverall_shop_rating() == 0)
            {
                holder.ratingBar.setVisibility(View.GONE);
                holder.shopratingobj.setVisibility(View.VISIBLE);
            }
            else
            {
//
                holder.ratingBar.setRating(productDetails.get(position).getOverall_shop_rating());
                holder.ratingBar.setNumStars(5);
                holder.ratingBar.setVisibility(View.VISIBLE);
                holder.shopratingobj.setVisibility(View.GONE);
            }

        }
        else  if (productDetails.get(position).getState().equals("Accepted"))
        {
            holder.image.setImageDrawable(activityContext.getResources().getDrawable(ic_accepted));
            holder.date.setText(productDetails.get(position).getAccepted_date());
        }
        else  if (productDetails.get(position).getState().equals("Shipped"))
        {
            holder.image.setImageDrawable(activityContext.getResources().getDrawable(ic_shipped));
            holder.date.setText(productDetails.get(position).getShipped_date());
        }

        else
        {
            holder.image.setImageDrawable(activityContext.getResources().getDrawable(ic_cancel));
            holder.date.setText(productDetails.get(position).getCancel_date());
        }
        holder.orderno.setText(productDetails.get(position).getName());
        holder.amount.setText(productDetails.get(position).getTotal_amount());

        try {
            JSONArray detailsarray = productDetails.get(position).getTempDict().getJSONArray("order_lines");
            holder.noitems.setText(String.format("%d Items", detailsarray.length()));
            holder.orderstate.setText(productDetails.get(position).getState());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listnerCallBack.onItemClick(position,ADD_CART);
            }
        });

        holder.shopratingobj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityContext instanceof MyOrdersActivity) {
                    ((MyOrdersActivity) activityContext).reviewpopup(productDetails.get(position).getTempDict());
                }
            }
        });
    }    @Override
    public int getItemCount() {
        return productDetails.size();
    }
}