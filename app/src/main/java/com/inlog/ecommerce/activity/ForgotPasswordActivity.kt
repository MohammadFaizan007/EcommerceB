package com.inlog.ecommerce.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inlog.ecommerce.R
import com.inlog.ecommerce.fragments.EnterEmailPhonForgotFragment
import com.inlog.ecommerce.fragments.ValidateOtpEnterNewPassFragment
import com.inlog.ecommerce.myaccount.fragment.AddressFragment.Companion.fragmentTag
import com.inlog.ecommerce.myaccount.fragment.AddressListingFragment.Companion.newInstance
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner

class ForgotPasswordActivity : AppCompatActivity(), MyListCallbackListner {
    private val FRAGMENT_TAG = "FRAGMENT_TAG"
    companion object{
        val ENTER_EMAIL_PHONE_FRAGMENT = 10
        val RESET_PASSWORD_FRAGMENT = 11
        var emailOrPhone = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        emailOrPhone =""
        onItemClick(ENTER_EMAIL_PHONE_FRAGMENT,0)
    }

    override fun onItemClick(id: Int, tag: Int) {
        if(id == ENTER_EMAIL_PHONE_FRAGMENT){
            addFragment(EnterEmailPhonForgotFragment.newInstance(""),false)
        }else if(id == RESET_PASSWORD_FRAGMENT){
            addFragment(ValidateOtpEnterNewPassFragment.newInstance(""),true)
        }

    }
    private fun addFragment(fragment: Fragment,isAddtoBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        var transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frmContainerId, fragment, FRAGMENT_TAG)
        if(isAddtoBackStack)
        transaction.addToBackStack(fragmentTag)
        transaction .commit()
    }
}