package com.inlog.ecommerce.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import com.inlog.ecommerce.R
import com.inlog.ecommerce.myaccount.fragment.AddressFragment.Companion.fragmentTag
import com.inlog.ecommerce.myaccount.fragment.AddressFragment.Companion.newInstance
import com.inlog.ecommerce.myaccount.fragment.AddressListingFragment
import com.inlog.ecommerce.myaccount.fragment.AddressListingFragment.Companion.newInstance
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner

class MakePaymentActivity : AppCompatActivity() , MyListCallbackListner {
    companion object{
        val EXTRA_PARAMS = "extra_params"
        val FRAGMENT_TAG = "fragment_tag"
        val REQUEST_CODE = 2
        val EXTRA_SELECTED_ADDRESS = "extra_selected_address"
        fun startActivity(context : Activity, data: String? = null){
            val intent = Intent(context,MakePaymentActivity::class.java)
            intent.putExtra(EXTRA_PARAMS,data)
            startActivityForResult(context,intent, REQUEST_CODE,Bundle());
//            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_payment)
        addAddressListingFragment()
    }
    private fun addAddressListingFragment() {
        val myAccountFragment: Fragment = AddressListingFragment.newInstance(true)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.paymentFragmentContainer, myAccountFragment, FRAGMENT_TAG)
                .commit()
    }
    private fun addAddressFragment() {
        val myAccountFragment: Fragment = newInstance(0)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.paymentFragmentContainer, myAccountFragment, FRAGMENT_TAG)
                .addToBackStack(fragmentTag)
                .commit()
    }

    override fun onItemClick(id: Int, tag: Int) {
        addAddressFragment()

    }
}