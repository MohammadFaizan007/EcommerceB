package com.inlog.ecommerce.fragments

import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.inlog.ecommerce.R
import com.inlog.ecommerce.adapter.RatingAdapter
import com.inlog.ecommerce.fragments.ProductListingFragment.PRODUCT_DETAIL_EXTRA
import com.inlog.ecommerce.model.RatingViewModel
import com.inlog.ecommerce.model.categoryProduct
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.fragment_rating_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 * Use the [RatingListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RatingListFragment : Fragment() {
    private var isForPaging: Boolean = false
    private var pageIndex = 0
    private var ratingData: String? = null
    private var productDetail : categoryProduct? = null
    var loading = true
     var ratingList  = ArrayList<RatingViewModel>()

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ratingData = it.getString(EXTRA_RAITING_DATA)
            productDetail = it.getSerializable(PRODUCT_DETAIL_EXTRA).let {  it as categoryProduct?}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        ratingData.let { Utility.log("Raiting data fragment ", ratingData)}
        ratingList = ArrayList<RatingViewModel>()
//        ratingData?.let { RatingViewModel.mapData(it) }?.let { ratingList.addAll(it) }
        var layoutManager = LinearLayoutManager(activity)
        recyclVRaitinId.layoutManager = layoutManager
        recyclVRaitinId.adapter = RatingAdapter(ratingList, productDetail!=null,this.activity)
        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        recyclVRaitinId.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount()
                    totalItemCount = layoutManager.getItemCount()
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            pageIndex = pageIndex + 1 + 20
                            isForPaging = true
                           getProductRating()
                        }
                    }
                }
            }
        })
        getProductRating()

        if(productDetail !=null)
            llTotalRating.visibility =View.VISIBLE
        else
            llTotalRating.visibility =View.GONE
    }

    companion object {
       val EXTRA_RAITING_DATA = "raiting_data"
        @JvmStatic
        fun newInstance(param1: String?,productDetail : categoryProduct?) =
                RatingListFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_RAITING_DATA, param1)
                        putSerializable(PRODUCT_DETAIL_EXTRA,productDetail)

                    }
                }
    }

    fun getProductRating() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            val sharedPrefs: SharedPreferences? = activity?.getSharedPreferences("shopidkey", 0)
            val shopid = sharedPrefs?.getInt("shopid", 0)
            var url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getProductRating + "?clientId=" + GlobalVariables.clientID + "&productId=" + productDetail?.productid+"&productVariantId="+ productDetail?.product_variant_id+"&orderId=&shopOrderId=&consumerId=&id=&index="+pageIndex+"&limit=20"
           if(productDetail == null)
            url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getShopRating + "?clientId=" + GlobalVariables.clientID + "&orderId=&shopOrderId=&consumerId=&id=&index="+pageIndex+"&limit=20"

            MyProgressBar.getInstance().showProgressDialog(activity)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                val responsefinal = response.toString()
                Utility.log("Response = ", responsefinal)
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var shopresultarray = JSONArray()
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            shopresultarray = jsonObject.getJSONArray("result")
                            var  productDetailstarray = jsonObject.optJSONArray("ProductDetails")
                            if(productDetailstarray!=null && productDetailstarray.length()>0){
                                var totalObj = productDetailstarray.optJSONObject(0)
                                showTotalDetail(totalObj)

                            }

                            if (shopresultarray.length() > 0) {
                                if (!isForPaging)
                                ratingList.clear()
                                ratingList.addAll(RatingViewModel.mapData(shopresultarray.toString()))
                                loading = true
                                recyclVRaitinId.adapter?.notifyDataSetChanged()

                            } else {
                                Toast.makeText(context, getString(R.string.nodatafound), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    MyProgressBar.getInstance().dismissProgressDialog()
                }
            }, Response.ErrorListener { MyProgressBar.getInstance().dismissProgressDialog() })
            requestQueue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showTotalDetail(totalObj: JSONObject?) {
        var overall_rating = totalObj?.optString("overall_rating")
        var quality_rating = totalObj?.optString("quality_rating")
        var rating_count = totalObj?.optString("rating_count")
        var value_for_money_rating = totalObj?.optString("value_for_money_rating")
        var meet_expectation_rating = totalObj?.optString("meet_expectation_rating")
        txtVtotalRatingCount.text = overall_rating


       var txtVStarCount1 :TextView = rl1.findViewById<TextView>(R.id.txtVStarCount)
       var p_Bar1 :ProgressBar = rl1.findViewById<ProgressBar>(R.id.p_Bar)
       var txtVEndTotal1 :TextView = rl1.findViewById<TextView>(R.id.txtVEndTotal)
       var txtVEndTotal2 :TextView = rl2.findViewById<TextView>(R.id.txtVEndTotal)
       var txtVEndTotal3 :TextView = rl3.findViewById<TextView>(R.id.txtVEndTotal)
       var p_Bar2 :ProgressBar = rl2.findViewById<ProgressBar>(R.id.p_Bar)
       var p_Bar3 :ProgressBar = rl3.findViewById<ProgressBar>(R.id.p_Bar)
        setProgressBarColor(p_Bar1,quality_rating)
        txtVEndTotal1.text =quality_rating
        setProgressBarColor(p_Bar2,meet_expectation_rating)
        txtVEndTotal2.text = meet_expectation_rating
        setProgressBarColor(p_Bar3,value_for_money_rating)
        txtVEndTotal3.text = value_for_money_rating
        txtVStarCount1.text = "Quality"
       var txtVStarCount2 :TextView = rl2.findViewById<TextView>(R.id.txtVStarCount)
        txtVStarCount2.text = "Meet expectation"
       var txtVStarCount3 :TextView = rl3.findViewById<TextView>(R.id.txtVStarCount)
        txtVStarCount3.text = "Value for money"
      /* var txtVStarCount4 :TextView = rl4.findViewById<TextView>(R.id.txtVStarCount)
        txtVStarCount4.text ="2"
       var txtVStarCount5 :TextView = rl5.findViewById<TextView>(R.id.txtVStarCount)
        txtVStarCount5.text = "1"*/
    }

    private fun setProgressBarColor(progressbar: ProgressBar, qualityRating: String?) {
        var color = resources.getColor(R.color.colorAccent)
        if(Utility.strToInt(qualityRating)<=3){
            color = resources.getColor(R.color.red_color)
        }
        else if(Utility.strToInt(qualityRating) in 4..4){
            color = resources.getColor(R.color.yellow)
        }
        progressbar.setProgress(Utility.strToInt(qualityRating))
        progressbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

    }
}