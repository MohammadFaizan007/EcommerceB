package com.inlog.ecommerce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.model.Cart
import com.inlog.ecommerce.utility.Utility
import java.util.ArrayList

class CheckoutProductAdapter(val productlist: ArrayList<Cart>) : RecyclerView.Adapter<CheckoutProductAdapter.MyViewHolder>() {

    class MyViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        var txtVPrdName : TextView?= null
        var txtVPrdPriceId : TextView?= null
        var txtVPrdAttributes : TextView?= null
        init {
            txtVPrdName = item.findViewById<TextView>(R.id.txtVPrdName)
            txtVPrdPriceId = item.findViewById<TextView>(R.id.txtVPrdPriceId)
            txtVPrdAttributes = item.findViewById<TextView>(R.id.txtVPrdAttributes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_checkout_product,null))

    }

    override fun getItemCount(): Int {
        return productlist.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var prodItem = productlist.get(position)
        holder.txtVPrdName?.text = "${prodItem.product_name} x ${prodItem.qty}"
        holder.txtVPrdPriceId?.text = Utility.appendCurrencyWithText(prodItem.subtotal)
        holder.txtVPrdAttributes?.text = prodItem.product_variant

    }
}