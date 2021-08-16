package com.inlog.ecommerce.myaccount.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.myaccount.fragment.AddressListingFragment
import com.inlog.ecommerce.myaccount.fragment.AddressListingFragment.Companion.MAKE_DEFAULT_ADDRESS
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import com.inlog.ecommerce.myaccount.model.AddressItem

class AddressListAdapter(val addressList: ArrayList<AddressItem>, val isForOrder: Boolean, val listner: MyListCallbackListner) : RecyclerView.Adapter<AddressListAdapter.MyViewHolder>(){
    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
         var imgDelete: ImageView?
         var imgEdit: ImageView?
         var txtAddressDetails: TextView?
         var txtDefaultId: TextView?
         var txtVMakeOrSelectId: TextView?
         var radBtn: RadioButton?
         var llmakeDefault: LinearLayout?
         var llDelete: LinearLayout?

        init {
            radBtn = view.findViewById<RadioButton>(R.id.radBtn)
            txtDefaultId = view.findViewById<TextView>(R.id.txtDefaultId)
            txtVMakeOrSelectId = view.findViewById<TextView>(R.id.txtVMakeOrSelectId)
            txtAddressDetails = view.findViewById<TextView>(R.id.txtAddressDetails)
            imgEdit = view.findViewById<ImageView>(R.id.imgEdit)
            imgDelete = view.findViewById<ImageView>(R.id.imgDelete)
            llmakeDefault = view.findViewById<LinearLayout>(R.id.llmakeDefault)
            llDelete = view.findViewById<LinearLayout>(R.id.llDelete)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_addess_listing, null, false))

    }

    override fun getItemCount(): Int {
      return addressList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var addressObj = addressList.get(position)
       var addressDetail = "${addressObj.name}\n" +
               "${addressObj.email}\n" +
               "Phone:${addressObj.mobile}\n${addressObj.street}\n${addressObj.street2}\n${addressObj.landmark}\n" +
               "${addressObj.city}\n${addressObj.state}-${addressObj.zip}"

        holder.txtAddressDetails?.text=addressDetail
        holder.radBtn?.isChecked = addressObj.isIsdefaultaddress
        if(holder.radBtn?.isChecked == true){
            holder.txtDefaultId?.text = "Default Address"
            holder.txtDefaultId?.visibility = View.VISIBLE
        }else  holder.txtDefaultId?.visibility = View.GONE


        holder.radBtn?.setOnClickListener {
            val radioBtn = it as RadioButton
            if(!addressObj.isIsdefaultaddress){
                listner.onItemClick(position,MAKE_DEFAULT_ADDRESS)
            }
        }



        holder.llmakeDefault?.setOnClickListener {
                listner.onItemClick(position,MAKE_DEFAULT_ADDRESS)
        }

        if(isForOrder)/*if user comer for order flow*/{
            holder.llDelete?.visibility = View.GONE
            holder.imgEdit?.visibility = View.GONE
            holder.txtVMakeOrSelectId?.text = holder.txtVMakeOrSelectId?.context?.getString(R.string.select_)
            holder.llmakeDefault?.visibility =  View.VISIBLE
        }else{
            holder.llDelete?.visibility =  if(addressObj.isIsdefaultaddress) View.GONE else View.VISIBLE
            holder.llDelete?.setOnClickListener {
                listner.onItemClick(position,AddressListingFragment.DELETE_ADDRESS)
            }
            holder.imgEdit?.setOnClickListener {
                listner.onItemClick(position,AddressListingFragment.EDIT_ADDRESS)
            }
            holder.llmakeDefault?.visibility =  if(addressObj.isIsdefaultaddress) View.GONE else View.VISIBLE
        }
    }

}