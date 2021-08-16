package com.inlog.ecommerce.myaccount

import android.os.Bundle
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inlog.ecommerce.R
import com.inlog.ecommerce.myaccount.fragment.AddressFragment.Companion.newInstance
import com.inlog.ecommerce.myaccount.fragment.AddressListingFragment.Companion.newInstance
import com.inlog.ecommerce.myaccount.fragment.ChangePasswordFragment.Companion.newInstance
import com.inlog.ecommerce.myaccount.fragment.MyAccountFragment.Companion.newInstance
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import com.labo.kaji.fragmentanimations.*



class AccountActivity : AppCompatActivity(), MyListCallbackListner {

    companion object{
        val FRAGMENT_TAG = "fragment_tag"
        val MY_ORDER = 1
        //        val MY_WALLET = 2
        val MY_PAYMENT = 2
        val MY_RATINGS = 3
        val MY_CUSTOMER = 4
        val MY_GIFT = 5
        val MY_NOTIFICATION = 6
        val MY_ADDRESS = 7
        val LOGOUT = 8
        val ADDRESS_LISTING_FRAGMENT = 9
        val CHANGE_PASSWORD = 10
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        addFragment(newInstance(), false)
    }
    override fun onItemClick(id: Int, tag: Int) {
        when(id){
            MY_ORDER -> {
            }
            MY_PAYMENT -> {

            }
            MY_RATINGS -> {

            }
            MY_CUSTOMER -> {

            }
            MY_GIFT -> {

            }
            MY_NOTIFICATION -> {

            }
            MY_ADDRESS -> {
                addFragment(newInstance(tag))
            }
            LOGOUT -> {

            }
            ADDRESS_LISTING_FRAGMENT -> {
                addFragment(newInstance(false));
            }
            CHANGE_PASSWORD -> {
                addFragment(newInstance(""));
            }
        }

    }

    private fun addFragment(fragment: Fragment, isAddtoBackStack: Boolean = true) {

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        if(isAddtoBackStack)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frmAccountContainer, fragment, FRAGMENT_TAG)
//        fragment.allowEnterTransitionOverlap;
        if(isAddtoBackStack) {
            transaction.addToBackStack(FRAGMENT_TAG)
        }
        transaction .commit()
    }
}
