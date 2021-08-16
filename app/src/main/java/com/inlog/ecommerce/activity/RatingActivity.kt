package com.inlog.ecommerce.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inlog.ecommerce.R
import com.inlog.ecommerce.fragments.ProductListingFragment
import com.inlog.ecommerce.fragments.RatingListFragment
import com.inlog.ecommerce.model.categoryProduct
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.toolbar_header.*

class RatingActivity : AppCompatActivity() {

    var titlestr: String? = null

    companion object{
        val EXTRA_PARAMS = "extra_params"
        val FRAGMENT_TAG = "fragment_tag"
        fun startActivity(context :Context,data: String? = null,productDetail : categoryProduct?,title:String?)
        {
            val intent = Intent(context,RatingActivity::class.java)
            intent.putExtra(EXTRA_PARAMS,data)
            intent.putExtra("headertitle",title)
            intent.putExtra(ProductListingFragment.PRODUCT_DETAIL_EXTRA,productDetail)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raiting)
        addInitialFragment()
        acaddressback.setOnClickListener {
            onBackPressed()
        }

    }

    private fun addInitialFragment() {
        var extraData = intent.getStringExtra(EXTRA_PARAMS)
        header.text = intent.getStringExtra("headertitle")
        var productDetail = intent.getSerializableExtra(ProductListingFragment.PRODUCT_DETAIL_EXTRA).let {  it as categoryProduct?}
//        Utility.log("Raiting data ", extraData)
        val ratingFragment: Fragment = RatingListFragment.newInstance(extraData,productDetail)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container, ratingFragment, FRAGMENT_TAG)
//                .addToBackStack(fragmentTag)
                .commit()

    }
}