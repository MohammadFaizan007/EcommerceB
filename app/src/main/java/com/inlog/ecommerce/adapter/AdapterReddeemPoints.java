package com.inlog.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.model.RedeemPoints;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterReddeemPoints extends RecyclerView.Adapter<AdapterReddeemPoints.MyViewHolder> {
    private final ArrayList<RedeemPoints> redeemList;

    public AdapterReddeemPoints(ArrayList<RedeemPoints> redeemList) {
        this.redeemList = redeemList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_reed,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RedeemPoints item = redeemList.get(position);

        holder.txtVShopnameId.setText(item.getShopname());
        holder.txtVWorthvalue.setText(item.getWorth());
        holder.txtvPoints.setText(item.getPoints());
    }

    @Override
    public int getItemCount() {
        return redeemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtVShopnameId,txtvPoints,txtVWorthvalue;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVShopnameId = itemView.findViewById(R.id.txtVShopnameId);
            txtvPoints = itemView.findViewById(R.id.txtvPoints);
            txtVWorthvalue = itemView.findViewById(R.id.txtVWorthvalue);
        }
    }
}
