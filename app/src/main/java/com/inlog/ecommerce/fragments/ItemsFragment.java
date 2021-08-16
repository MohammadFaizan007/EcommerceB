package com.inlog.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.activity.MyOrdersActivity;
import com.inlog.ecommerce.activity.ProductListActivity;
import com.inlog.ecommerce.activity.orderdetailsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.inlog.ecommerce.R.drawable.ic_complete;

public class ItemsFragment extends Fragment {
    private ItemRecyclerViewAdapter itemlistAdapter;
    private JSONObject itemDict;
    JSONArray detailsarray;
    private static orderdetailsActivity mActivity;
    private RecyclerView recyclerView;
    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (orderdetailsActivity) getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        if (getArguments() != null) {
            String getArgument = getArguments().getString("data");
            try {

                itemDict = new JSONObject(getArgument);
                detailsarray = new JSONArray();
                detailsarray = itemDict.getJSONArray("order_lines");
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewitem);
                RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mActivity);
                recyclerView.setLayoutManager(recylerViewLayoutManager);
                itemlistAdapter =  new ItemRecyclerViewAdapter(recyclerView, detailsarray,itemDict);
                recyclerView.setAdapter(itemlistAdapter);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("",getArgument);
        }
        return view;
    }

    public static class ItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ItemsFragment.ItemRecyclerViewAdapter.ViewHolder> {
        private RecyclerView mRecyclerView;
        private JSONArray productdetials;
        private JSONObject tempobject;

        private int lastPosition = -1;
        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            private ImageView itemimg;
            private TextView item_name,item_count,pricevalue,Quantity;
            private final Button shopratingobj;
            RatingBar ratingBar;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                itemimg = (ImageView) view.findViewById(R.id.itemimg);
                item_name = (TextView) view.findViewById(R.id.item_name);
                item_count = (TextView) view.findViewById(R.id.item_count);
                pricevalue = (TextView) view.findViewById(R.id.pricevalue);
                Quantity = (TextView) view.findViewById(R.id.Quantity);
                shopratingobj = (Button) view.findViewById(R.id.shopratingobj);
                ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

            }

            public int getQuantity(final ProductListingFragment.SimpleStringRecyclerViewAdapter.ViewHolder holder) {
                return Integer.parseInt(holder.prnumber.getText().toString());
            }
        }
        public ItemRecyclerViewAdapter(RecyclerView recyclerView, JSONArray itemarray,JSONObject tempobjects) {
            mRecyclerView = recyclerView;
            productdetials = itemarray;
            tempobject = tempobjects;
        }
        @Override
        public ItemsFragment.ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orderlist_item, parent, false);
            return new ItemsFragment.ItemRecyclerViewAdapter.ViewHolder(view);
        }
        @Override
        public void onViewRecycled(ItemsFragment.ItemRecyclerViewAdapter.ViewHolder holder) {

        }
        public interface Oncartcountlistner {
            void Oncartcountlistner(int count);
        }
        @Override
        public void onBindViewHolder(final ItemsFragment.ItemRecyclerViewAdapter.ViewHolder holder, final int position) {
            try {
                JSONObject tempDict = (JSONObject)productdetials.get(position);
                final Uri uri = Uri.parse(String.valueOf(tempDict.getString("image_medium")));
                Picasso.with(mActivity).load(uri).into( holder.itemimg);
                holder.item_name.setText(tempDict.getString("productName"));
                holder.item_count.setText(String.format("%s Item", tempDict.getString("qty")));
                holder.pricevalue.setText(String.format("%s Rs", tempDict.getString("price")));
                holder.Quantity.setText(String.format("Quantity: %s", tempDict.getString("qty")));
                holder.ratingBar.setVisibility(View.GONE);
                holder.shopratingobj.setVisibility(View.GONE);
                if (tempobject.getString("state").equals("Delivered"))
                {
                    if (tempDict.optInt("overall_product_rating") == 0)
                    {
                        holder.ratingBar.setVisibility(View.GONE);
                        holder.shopratingobj.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.ratingBar.setRating(Float.parseFloat(tempDict.optString("overall_product_rating")));
                        holder.ratingBar.setNumStars(5);
                        holder.ratingBar.setVisibility(View.VISIBLE);
//                        holder.ratingBar.setRating(2);
                        holder.shopratingobj.setVisibility(View.GONE);
                    }
                }
                holder.shopratingobj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity instanceof orderdetailsActivity) {
                            ((orderdetailsActivity) mActivity).reviewpopup(tempDict);
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return productdetials.length();
        }

    }

}