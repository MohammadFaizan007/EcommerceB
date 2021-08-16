package com.inlog.ecommerce.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.model.ProductAttribute
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import java.util.*

class ProductAttributeAdapter(private val productAttribute: ArrayList<ProductAttribute>, val isSingleChoice: Boolean = false, val myListCallbackListner: MyListCallbackListner? = null) : RecyclerView.Adapter<ProductAttributeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_product_attribute, null))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtVTitle.text = productAttribute[position].name

        if (productAttribute[position].color.equals(""))
        {
            holder.cardcolor.visibility = View.GONE

        }
        else
        {
            holder.cardcolor.visibility = View.VISIBLE
            holder.cardcolor.setBackgroundColor((Color.parseColor(productAttribute[position].color)));
        }


        if (!productAttribute[position].isChecked) holder.layout.background = holder.txtVTitle.context.resources.getDrawable(R.color.white) else
            holder.layout.background = holder.layout.context.resources.getDrawable(R.drawable.rounde_selected_background)

        holder.layout.tag = position
        holder.layout.setOnClickListener { view ->
            val positionTag = view.tag as Int
            if(isSingleChoice){
                for(index1 in 0 until productAttribute.size){
                    if(productAttribute.get(index1).isChecked)
                        productAttribute.get(index1).isChecked = false
                }
            }
            productAttribute[positionTag].isChecked = !productAttribute[positionTag].isChecked

            myListCallbackListner?.let {
                it.onItemClick(positionTag,0)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return productAttribute.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtVTitle: TextView = itemView.findViewById(R.id.txtVTitle)
        var cardcolor: TextView = itemView.findViewById(R.id.cardcolor)
        var layout: LinearLayout = itemView.findViewById(R.id.layout)


    }

}