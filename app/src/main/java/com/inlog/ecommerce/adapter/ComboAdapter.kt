package com.inlog.ecommerce.adapter

import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.activity.ItemDetailsActivity
import com.inlog.ecommerce.model.categoryProduct
import com.squareup.picasso.Picasso
import java.util.*

class ComboAdapter(val productdetials: ArrayList<categoryProduct>, val itemDetailsActivity: ItemDetailsActivity) : RecyclerView.Adapter<ComboAdapter.MyViewHolder>() {
    class MyViewHolder(viewItem: View):RecyclerView.ViewHolder(viewItem) {
        var image1: ImageView
         var list_item_name: TextView
         var list_item_price: TextView
         var list_item_sale_price: TextView
         var txtVqty: TextView
        init {
            image1 = viewItem.findViewById(R.id.image1)
            list_item_name = viewItem.findViewById(R.id.list_item_name)
            list_item_price = viewItem.findViewById(R.id.list_item_price)
            list_item_sale_price = viewItem.findViewById(R.id.list_item_sale_price)
            txtVqty = viewItem.findViewById(R.id.txtVqty)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_combo, null))
    }

    override fun onBindViewHolder(holder: ComboAdapter.MyViewHolder, position: Int) {
        val uri = Uri.parse(productdetials.get(position).getImageurl())
        Picasso.with(holder.image1.context).load(uri).into(holder.image1)
        holder.image1.setOnClickListener {
            itemDetailsActivity.onComboItemClicked(productdetials.get(position))
        }
        holder.txtVqty.text = "X ${(productdetials[position].qty)}"
        holder.list_item_name.setText(productdetials[position].name)
        holder.list_item_price.setText("₹ " + productdetials[position].sale_price.toString())
        if(productdetials[position].base_price>0) {
            holder.list_item_sale_price.setText("₹ " + productdetials[position].base_price.toString())
            holder.list_item_sale_price.setPaintFlags(holder.list_item_sale_price.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.list_item_sale_price.visibility = View.VISIBLE
        }
        else{
            holder.list_item_sale_price.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
      return productdetials.size
    }
}