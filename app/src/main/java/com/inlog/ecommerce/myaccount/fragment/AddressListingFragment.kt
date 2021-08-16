package com.inlog.ecommerce.myaccount.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.inlog.ecommerce.R
import com.inlog.ecommerce.activity.MakePaymentActivity
import com.inlog.ecommerce.activity.MakePaymentActivity.Companion.EXTRA_SELECTED_ADDRESS
import com.inlog.ecommerce.fragments.ProductListingFragment
import com.inlog.ecommerce.myaccount.adapter.AddressListAdapter
import com.inlog.ecommerce.myaccount.fragment.MyAccountFragment.Companion.MY_ADDRESS
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import com.inlog.ecommerce.myaccount.model.AddressItem
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.ShowAlert
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.fragment_address_listing.*
import kotlinx.android.synthetic.main.toolbar_header.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class AddressListingFragment : Fragment() {
    private var extraWhichFlow: Boolean = false
    private  var listener: MyListCallbackListner? = null
    private val addressList = ArrayList<AddressItem>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            extraWhichFlow = it.getBoolean(EXTRA_WHICH_FLOW)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_address_listing, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hitApiTogetCustomerAddress()
        txtVAddNewAddress?.setOnClickListener {
            listener?.onItemClick(MyAccountFragment.Companion.MY_ADDRESS, 0)
        }
        if(extraWhichFlow == FOR_ORDER)
        header?.text = getString(R.string.delivery_addres)
        else
        header?.text = getString(R.string.addresses)

    }
    companion object {
        val EXTRA_WHICH_FLOW = "which_flow"
        val FOR_ORDER = true
        val EDIT_ADDRESS = 1
        val DELETE_ADDRESS = 2
        val MAKE_DEFAULT_ADDRESS = 3
           @JvmStatic
        fun newInstance(isForOrder: Boolean = false) =
                AddressListingFragment().apply {
                    arguments = Bundle().apply {putBoolean(EXTRA_WHICH_FLOW, isForOrder)
                    }
                }
    }
    private fun hitApiTogetCustomerAddress() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)
            val sharedPrefsLogin: SharedPreferences? = activity?.getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin?.getInt("globalid", 0)?:0

            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerAddress+"?clientId="+ GlobalVariables.clientID+"&id=&consumerId="+customerId

            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONArray()
                        var childarray = JSONArray()
                        detailsarray = jsonObject.getJSONArray("result")
                        addressList.clear()
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            for (i in 0 until detailsarray.length()) {
                                val item = detailsarray.getJSONObject(i)
                                val obj = AddressItem()
                                obj.city = item.optString("city")
                                obj.city_id = item.optString("city_id")
                                obj.country = item.optString("country")
                                obj.email = item.optString("email")
                                obj.id = item.optString("id")
                                obj.landmark = item.optString("landmark")
                                obj.mobile = item.optString("mobile")
                                obj.name = item.optString("name")
                                obj.state = item.optString("state")
                                obj.state_id = item.optString("state_id")
                                obj.street = item.optString("street")
                                obj.street2 = item.optString("street2")
                                obj.zip = item.optString("zip")
                                obj.isIsdefaultaddress = item.optBoolean("isdefaultaddress")
                                addressList.add(obj)
                            }
                            setUpAdapter()
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
    private fun hitApiToMakeDefaultAddress(position: Int) {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)
            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.setDefaultAddress

            val hashMap = HashMap<String, Any>()
            val hashMapMain = HashMap<String, Any>()
            val sharedPrefsLogin: SharedPreferences? = activity?.getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin?.getInt("globalid", 0)?:0

            hashMap.put("clientId", GlobalVariables.clientID)
            hashMap.put("consumerId", customerId)
            hashMap.put("AddressId", addressList[position].id)
            hashMapMain.put("params", hashMap)

            val reqBody  = JSONObject(hashMapMain.toString())

            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, reqBody, Response.Listener { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                Utility.log("Response  ", responsefinal)
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal).optJSONObject("result")
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            Toast.makeText(context, "Address updated successfully", Toast.LENGTH_LONG).show()
                            hitApiTogetCustomerAddress()
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
    fun deleteAddressItem(position: Int) {
        try {
            MyProgressBar.getInstance().showProgressDialog(activity)
            DeleteAddresstem(this, addressList[position]).execute()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    class DeleteAddresstem(val fragment: AddressListingFragment, val addressItem: AddressItem) : AsyncTask<String?, Void?, String?>() {
        override fun onPreExecute() {

        }

        @SuppressLint("WrongThread")
        protected override fun doInBackground(vararg p0: String?): String? {
            var resultFinal: String? = null
            resultFinal = deletedata(addressItem, fragment.activity)
            val jsonObj: JSONObject? = null
            if (resultFinal != null) {
                val jsonObject: JSONObject
                if (resultFinal != null) {
                    try {
                        Utility.log("response delete  ", resultFinal)
                        jsonObject = JSONObject(resultFinal)
                        val result = jsonObject.getJSONObject("result")
                        if (result.optString("status").equals("OK", ignoreCase = true)) {
                            return "1"
                        }
                    } catch (e: JSONException) {
                        return "0"
                        e.printStackTrace()
                    }
                }
            } else {
                MyProgressBar.getInstance().dismissProgressDialog()
            }
            return "0"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            MyProgressBar.getInstance().dismissProgressDialog()
            if (result.equals("1")) {
                Toast.makeText(fragment.context, "Address Deleted successfully", Toast.LENGTH_LONG).show()
                fragment.hitApiTogetCustomerAddress()
            }
            else
            Toast.makeText(fragment.context, "Address not Deleted successfully", Toast.LENGTH_LONG).show()
        }

        private fun deletedata(addressItem: AddressItem, activity: FragmentActivity?): String {
            return try {
                MyProgressBar.getInstance().showProgressDialog(activity)
                val url = URL(GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.deleteConsumerAddress)
                val conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = 15000
                conn.connectTimeout = 15000
                conn.requestMethod = "DELETE"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doInput = true
                conn.doOutput = true
                val mainobj = JSONObject()
                val childobj = JSONObject()
                childobj.put("clientId", GlobalVariables.clientID)
                childobj.put("AddressId", addressItem.id)
                mainobj.put("params", childobj)
                Utility.log("Req Body ", mainobj.toString())
                Utility.log("url ", url.toString())
                val bw = BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8"))
                bw.write(mainobj.toString())
                bw.flush()
                bw.close()
                val responseCode = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val `in` = BufferedReader(InputStreamReader(
                            conn.inputStream))
                    val sb = StringBuffer("")
                    var line: String? = ""
                    while (`in`.readLine().also { line = it } != null) {
                        sb.append(line)
                        break
                    }
                    `in`.close()
                    sb.toString()
                } else {
                    ""
                }
            } catch (e: java.lang.Exception) {
                ""
            }
        }

    }
    private fun setUpAdapter() {
        recyAddressList.layoutManager = LinearLayoutManager(activity)
        recyAddressList.adapter = AddressListAdapter(addressList, FOR_ORDER == extraWhichFlow, object : MyListCallbackListner {
            override fun onItemClick(position: Int, tag: Int) {
                if (DELETE_ADDRESS == tag) {
                    activity?.let {
                        ShowAlert(it, it.getString(R.string.app_name), getString(R.string.are_your_suee_want_to_delete), getString(R.string.yes), object : View.OnClickListener {
                            override fun onClick(p0: View?) {
                                deleteAddressItem(position)
                            }
                        }, getString(R.string.no), object : View.OnClickListener {
                            override fun onClick(p0: View?) {

                            }
                        }).show()
                    }

                } else if (EDIT_ADDRESS == tag) {
                    editAddress(position)
                } else if (MAKE_DEFAULT_ADDRESS == tag) {
                    if (FOR_ORDER == extraWhichFlow) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_SELECTED_ADDRESS, addressList[position])
                        activity?.setResult(MakePaymentActivity.REQUEST_CODE, intent)
                        activity?.finish() //finishing activity

                    } else {
                        hitApiToMakeDefaultAddress(position)
                    }
                }
            }
        })
    }
    private fun editAddress(position: Int) {
        if(listener!=null){
            listener?.onItemClick(MY_ADDRESS, addressList[position].id.toInt())
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MyListCallbackListner
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener.  */
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        acaddressback?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
              activity?.onBackPressed()
        }
    }
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
}