package com.inlog.ecommerce.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.inlog.ecommerce.R
import com.inlog.ecommerce.activity.MakePaymentActivity.Companion.EXTRA_SELECTED_ADDRESS
import com.inlog.ecommerce.activity.MakePaymentActivity.Companion.REQUEST_CODE
import com.inlog.ecommerce.activity.MakePaymentActivity.Companion.startActivity
import com.inlog.ecommerce.adapter.CheckoutProductAdapter
import com.inlog.ecommerce.fragments.ProductListingFragment
import com.inlog.ecommerce.model.Cart
import com.inlog.ecommerce.model.RedeemPoints
import com.inlog.ecommerce.myaccount.model.AddressItem
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.CartManager
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.default_address.*
import kotlinx.android.synthetic.main.toolbar_header.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    private var delivery_data: JSONObject? = null
    private var shop_redeem_data: JSONObject? = null
    var totalDict : JSONObject?= null
    var selectedAddress: AddressItem? = null
    var productlist = ArrayList<Cart>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        acaddressback.setOnClickListener {
            onBackPressed()
        }
        header.text =resources.getString(R.string.checkout)
        txvChangeAddressId.setOnClickListener(View.OnClickListener {
            startActivity(this@CheckoutActivity, "")
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)

        })
        btnPlaceOrderId.setOnClickListener {
            makeOrder()
        }
    }

    override fun onResume() {
        super.onResume()
        hitApiTogetCustomerAddress()
    }
    fun getCartData() {
        val requestQueue = Volley.newRequestQueue(applicationContext)
        try {
            val sharedPrefsLogin: SharedPreferences = getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin.getInt("globalid", 0)
            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerUpdatedCart + "?clientId=" + GlobalVariables.clientID + "&consumerId=" + customerId + "&type=Cart&index=0&limit=100&consumerAddressId="+selectedAddress?.id+"&IsRedeem="+intent.getBooleanExtra(IS_REDEEMED, false)
            val `object` = JSONObject()
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                Utility.log("Api URL = ", url)
                Utility.log("Api Response = ", responsefinal)
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        CartManager.getInstance().cartList.clear()
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONArray()
                        var totalarray = JSONArray()
                        val result = jsonObject.getJSONObject("result")
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            delivery_data = result.optJSONObject("delivery_data")
                            shop_redeem_data = result.optJSONObject("shop_redeem_data")
                            detailsarray = result.getJSONArray("cart")
                            totalarray = result.getJSONArray("total")
                            totalDict = totalarray.getJSONObject(0)
                            //                                CartManager.getInstance().setTotalQty(totalDict.getInt("total_qty"));
                            if (detailsarray.length() > 0) {
                                productlist.clear()
                                for (i in 0 until detailsarray.length()) {
                                    val tempDict = detailsarray.get(i) as JSONObject
                                    val cartItem = Cart(tempDict.optInt("id"),
                                            tempDict.optInt("product_id"),
                                            tempDict.optInt("product_variant_id"),
                                            tempDict.optString("product_name"),
                                            tempDict.optString("qty"),
                                            tempDict.optString("price"),
                                            tempDict.optString("subtotal"),
                                            tempDict.optString("product_variant"),
                                            tempDict.optInt("shop_id"),
                                            tempDict.optString("shop"), tempDict.optString("type"),
                                            tempDict.optString("image"), productlist,
                                            tempDict.optBoolean("is_combo")
                                    )
                                    productlist.add(cartItem)
                                    cartItem.amountTaxed = tempDict.optString("tax_value")
                                    cartItem.taxPercentage = tempDict.optString("tax_percentage")
                                    cartItem.taxDescription = tempDict.optString("tax_description")
                                    cartItem.taxIds = tempDict.optString("tax_ids")

                                    setupAdapter(productlist)
                                    CartManager.getInstance().addCartItem(cartItem)
                                }
                                MyProgressBar.getInstance().dismissProgressDialog()
                            } else {
                                MyProgressBar.getInstance().dismissProgressDialog()
                            }
                            setCartLayout(totalDict)
                            val reward_data = result.optJSONObject("reward_data")
                            val reward_total = reward_data.optJSONObject("reward_total")
                            val rewards = reward_data.optJSONArray("rewards")
                            if (rewards != null && rewards.length() > 0) {
                                val redeemList = ArrayList<RedeemPoints>()
                                for (l in 0 until rewards.length()) {
                                    val jsonObj = rewards.getJSONObject(l)
                                    redeemList.add(RedeemPoints(jsonObj.optString("shop_name"), jsonObj.optString("shop_reward_points"), Utility.appendCurrencyWithText(jsonObj.optString("shop_reward_amount"))))
                                }
                                var total_reward_amount: String? = ""
                                var total_reward_points: String? = ""
                                if (reward_total != null) {
                                    total_reward_amount = reward_total.optString("total_reward_amount")
                                    total_reward_points = reward_total.optString("total_reward_points")
                                }

                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    MyProgressBar.getInstance().dismissProgressDialog()
                }
            }, { MyProgressBar.getInstance().dismissProgressDialog() })
            requestQueue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupAdapter(productlist: ArrayList<Cart>) {

        txtVTotalCartCountId.text = "Your cart (${productlist.size}items)"

        val checkoutAdapter = CheckoutProductAdapter(productlist)
        recyProductList.layoutManager = LinearLayoutManager(this)
        recyProductList.adapter = checkoutAdapter
    }

    //    https://uat.inlognetwork.co.in/getConsumerCart?clientId=ODOO90bddb3e-cdd8-11ea-8954-48d2245124b6&consumerId=26&type=Cart&index=0&limit=5000
    @SuppressLint("SetTextI18n")
    protected fun setCartLayout(totalDict: JSONObject?) {
        val priceitems = findViewById<View>(R.id.priceitems) as TextView
        val priceitemsval = findViewById<View>(R.id.priceitemsval) as TextView
        val txtVTaxVat = findViewById<View>(R.id.txtVTaxVat) as TextView
        val txtVTotalRedeemPoints = findViewById<View>(R.id.txtVTotalRedeemPoints) as TextView
        val discount = findViewById<View>(R.id.discount) as TextView
        val txtVTotalSavingOnThisCard = findViewById<View>(R.id.txtVTotalSavingOnThisCard) as TextView
        val txtVTotalSavingOnThisCardvalue = findViewById<View>(R.id.txtVTotalSavingOnThisCardvalue) as TextView
        val deliverychanges = findViewById<View>(R.id.deliverychanges) as TextView
        val totalamt = findViewById<View>(R.id.totalamt) as TextView
        if (CartManager.getInstance().cartSize > 0) {

                if (totalDict != null) {
                    priceitems.text = String.format("Price(%sitems)", totalDict.optString("total_items"))
                    priceitemsval.text = Utility.appendCurrencyWithText(totalDict.optString("total_price"))
                    txtVTaxVat.text = Utility.appendCurrencyWithText(totalDict.optString("total_tax"))
                    discount.text = Utility.appendCurrencyWithText(totalDict.optString("total_discount"))
                    txtVTotalRedeemPoints.setText("Total Consumed Points" + " (" + totalDict.optString("total_consume_points") + ")")
//                    txtVTotalSavingOnThisCard.text = getString(R.string.your_total_savings_on_this_card) + " " + Utility.appendCurrencyWithText(totalDict.optString("total_consume_amount"))
                    txtVTotalSavingOnThisCardvalue.text = Utility.appendCurrencyWithText(totalDict.optString("total_consume_amount"))
                    discount.setTextColor(resources.getColor(R.color.green_light))
                    if (totalDict.optString("delivery_charge").toString() == "0") {
                        deliverychanges.text = "FREE"
                        deliverychanges.setTextColor(resources.getColor(R.color.green_light))
                    } else {
                        deliverychanges.text = Utility.appendCurrencyWithText(totalDict.optString("delivery_charge"))
                        deliverychanges.setTextColor(Color.RED)
                    }
                    totalamt.text = Utility.appendCurrencyWithText(totalDict.optString("grand_total"))
                }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                selectedAddress = data.getSerializableExtra(EXTRA_SELECTED_ADDRESS) as AddressItem?
                if (selectedAddress != null) {
                    populateDefaultAddres(selectedAddress)
                }
            }
        }
    }
    private fun populateDefaultAddres(selectedAddress: AddressItem?) {
        val address1 = findViewById<TextView>(R.id.address1)
        val address2 = findViewById<TextView>(R.id.address2)
        val address3 = findViewById<TextView>(R.id.address3)
        if (selectedAddress != null) {
            address1.text = selectedAddress.street + ", " + selectedAddress.street2
            address2.text = selectedAddress.landmark
            address3.text = selectedAddress.city + "-" + selectedAddress.zip
        }
    }
    private fun hitApiTogetCustomerAddress() {
        val requestQueue = Volley.newRequestQueue(this)
        try {
            MyProgressBar.getInstance().showProgressDialog(this)
            val sharedPrefsLogin = getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            val customerId = sharedPrefsLogin.getInt("globalid", 0)
            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerAddress + "?clientId=" + GlobalVariables.clientID + "&id=&consumerId=" + customerId
            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                var responsefinal = response.toString()
                Utility.log("Api URL = ", url)
                Utility.log("Api Response = ", responsefinal)
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONArray()
                        var totalarray = JSONArray()
                        val result = jsonObject.getJSONArray("result")
                        val addressList = ArrayList<AddressItem>()
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            for (i in 0 until result.length()) {
                                val item = result.getJSONObject(i)
                                val addessItem = AddressItem()
                                addessItem.city = item.optString("city")
                                addessItem.city_id = item.optString("city_id")
                                addessItem.country = item.optString("country")
                                addessItem.email = item.optString("email")
                                addessItem.id = item.optString("id")
                                addessItem.landmark = item.optString("landmark")
                                addessItem.mobile = item.optString("mobile")
                                addessItem.name = item.optString("name")
                                addessItem.state = item.optString("state")
                                addessItem.state_id = item.optString("state_id")
                                addessItem.street = item.optString("street")
                                addessItem.street2 = item.optString("street2")
                                addessItem.zip = item.optString("zip")
                                addessItem.isIsdefaultaddress = item.optBoolean("isdefaultaddress")
                                addressList.add(addessItem)
                                if (addessItem.isIsdefaultaddress) {
                                    selectedAddress = addessItem
                                    break
                                }
                            }
                            if (addressList.size > 0 && selectedAddress == null) selectedAddress = addressList[0]
                            populateDefaultAddres(selectedAddress)
                            getCartData()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, Response.ErrorListener {
                getCartData()
            })
            requestQueue.add(jsonObjectRequest)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    private fun makeOrder() {
        val requestQueue = Volley.newRequestQueue(applicationContext)
        try {
            val shopId: Int
            //            MyProgressBar.getInstance().showProgressDialog(getApplicationContext());
            val sharedPrefs = getSharedPreferences("shopidkey", Context.MODE_PRIVATE)
            shopId = if (!sharedPrefs.contains("shopid")) {
                return
            } else {
                sharedPrefs.getInt("shopid", 0)
            }
            MyProgressBar.getInstance().showProgressDialog(this)
            val sharedPrefsLogin: SharedPreferences = getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin.getInt("globalid", 0)
            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.createConsumerOrder
            val paramsMap = HashMap<String, Any>()
            paramsMap["clientId"] = GlobalVariables.clientID
            paramsMap["consumerId"] = customerId
            paramsMap["shopId"] = shopId
            paramsMap["shippingAddressId"] = selectedAddress!!.id
            paramsMap["billingAddressId"] = selectedAddress!!.id /*TODO pass billing address id*/
            paramsMap["amountUntaxed"] = totalDict?.optString("total_price").toString().toFloat()
            paramsMap["amountTaxed"] = totalDict?.optString("total_tax").toString().toFloat()
            paramsMap["discount"] = totalDict?.optString("total_discount").toString().toFloat()
            paramsMap["paymentMethod"] = "COD"
            if(intent.getBooleanExtra(IS_REDEEMED, false)) {
                if (delivery_data != null)
                    paramsMap["shopDeliveryData"] = delivery_data!!
                if (shop_redeem_data != null)
                    paramsMap["shopRewardData"] = shop_redeem_data!!
            }
            val cardIds = JSONArray()
            val orderLines = JSONArray()
            for (i in productlist.indices) {
                val cartObjMap: HashMap<String, Any> = HashMap<String, Any>()
                cartObjMap["productVariantId"] = productlist[i].product_variant_id
                cartObjMap["productId"] = productlist[i].product_id
                cartObjMap["productName"] = productlist[i].product_name
                cartObjMap["productVariantName"] = productlist[i].product_variant
                cartObjMap["qty"] = productlist[i].qty
                cartObjMap["price"] = productlist[i].price.toFloat()
                cartObjMap["discount"] = "0" /*TODO pass discount valeu*/
                cartObjMap["subtotal"] = productlist[i].subtotal.toFloat()
                cartObjMap["shopId"] = productlist[i].shop_id
                cartObjMap["taxPercentage"] = productlist[i].taxPercentage.toFloat()
                cartObjMap["amountTaxed"] = productlist[i].amountTaxed.toFloat()
                cartObjMap["taxDescription"] = productlist[i].taxDescription
                cartObjMap["taxIds"] = productlist[i].taxIds
                orderLines.put(i, JSONObject(cartObjMap as Map<String, Any>))
                cardIds.put(i, productlist[i].id)
            }
            paramsMap["cartIds"] = cardIds
            paramsMap["orderLines"] = orderLines
            val mainJasonObj = HashMap<String?, Any?>()
            mainJasonObj["params"] = paramsMap
            val reqBody = JSONObject(mainJasonObj)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, reqBody, { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                Utility.log("Api url = ", url)
                Utility.log("Api body  = ", reqBody.toString())
                Utility.log("Api Response = ", responsefinal)

                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONArray()
                        var totalarray = JSONArray()
                        val result = jsonObject.getJSONObject("result")
                        if (result.optString("status").equals("OK", ignoreCase = true)) {
                            productlist.clear()
                            CartManager.getInstance().cartList.clear()
                            setCartLayout(totalDict)

                            val i = Intent(this@CheckoutActivity, ordersummeryActivity::class.java)
                            startActivity(i)
                            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)
                            finish()
//                            onBackPressed()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, { MyProgressBar.getInstance().dismissProgressDialog() })
            requestQueue.add(jsonObjectRequest)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmField
        var IS_REDEEMED: String = "IS_REDEEMED"
    }
}