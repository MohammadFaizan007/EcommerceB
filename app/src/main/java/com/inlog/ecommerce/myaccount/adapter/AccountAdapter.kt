package com.inlog.ecommerce.myaccount.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.myaccount.adapter.AccountAdapter.*
import com.inlog.ecommerce.myaccount.fragment.MyAccountFragment
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import com.inlog.ecommerce.myaccount.model.AccountModel

class AccountAdapter(val list: ArrayList<AccountModel>, val onClickListner: MyListCallbackListner) : RecyclerView.Adapter<MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var txtVTitle : TextView? =null
        var txtValue : TextView? =null
        var imgIcon : ImageView? =null
        var rlRowId : RelativeLayout? =null
        init {
            txtVTitle = view.findViewById(R.id.txtVTitle)
            txtValue = view.findViewById(R.id.txtValue)
            imgIcon = view.findViewById(R.id.imgIcon)
            rlRowId = view.findViewById(R.id.rlRowId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_account_list,null,false))

    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var obj = list[position]
        holder.txtVTitle?.text = obj.title
        if(obj.id == MyAccountFragment.MY_ORDER)
        holder.imgIcon?.setImageResource(R.drawable.myorder_icon)
//       else if(obj.id == MyAccountFragment.MY_WALLET)
//            holder.imgIcon?.setImageResource(R.drawable.wallet_icon)
         else if(obj.id == MyAccountFragment.MY_PAYMENT)
            holder.imgIcon?.setImageResource(R.drawable.mypaments_icon)
         else if(obj.id == MyAccountFragment.MY_RATINGS)
            holder.imgIcon?.setImageResource(R.drawable.ratings_icon)
         else if(obj.id == MyAccountFragment.MY_CUSTOMER)
            holder.imgIcon?.setImageResource(R.drawable.customerservice)
         else if(obj.id == MyAccountFragment.MY_GIFT)
            holder.imgIcon?.setImageResource(R.drawable.gifts_icon)
        else if(obj.id == MyAccountFragment.MY_NOTIFICATION)
            holder.imgIcon?.setImageResource(R.drawable.notification_icon)
        else if(obj.id == MyAccountFragment.ADDRESS_LISTING_FRAGMENT)
            holder.imgIcon?.setImageResource(R.drawable.mydeliveryaddress)
        else if(obj.id == MyAccountFragment.CHANGE_PASSWORD)
            holder.imgIcon?.setImageResource(R.drawable.ic_edit)
        else if(obj.id == MyAccountFragment.LOGOUT)
            holder.imgIcon?.setImageResource(R.drawable.logout_icon)

        if(obj.value.isNotEmpty())
        holder.txtValue?.text = "Rs ${obj.value}"


        holder.rlRowId?.setOnClickListener {it->onClickListner.onItemClick(obj.id,0)

        }

    }

}