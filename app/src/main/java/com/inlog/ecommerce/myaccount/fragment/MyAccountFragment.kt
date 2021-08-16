package com.inlog.ecommerce.myaccount.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.inlog.ecommerce.R
import com.inlog.ecommerce.activity.LoginActivity
import com.inlog.ecommerce.activity.MainActivity
import com.inlog.ecommerce.activity.MyOrdersActivity
import com.inlog.ecommerce.myaccount.adapter.AccountAdapter
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import com.inlog.ecommerce.myaccount.model.AccountModel
import com.inlog.ecommerce.myaccount.model.AddressItem
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.toolbar_header.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MyAccountFragment : Fragment() {
    private lateinit var listener: MyListCallbackListner
    private val addressList = ArrayList<AddressItem>()
    var selectedAddress: AddressItem? = null
    var maiVuew : View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        maiVuew = inflater.inflate(R.layout.fragment_my_account, container, false)
       /* val layout = container?.rootView?.findViewById(R.id.layout) as LinearLayout
        layout.setVisibility(View.GONE)*/
        return maiVuew
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header?.text = getString(R.string.my_account)
        scrollViewId.scrollTo(0, scrollViewId.top)
        acaddressback.setOnClickListener {
//            activity?.onBackPressed()
            val intent1 = Intent(activity, MainActivity::class.java)
            startActivity(intent1)
            activity?.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var list = ArrayList<AccountModel>()
        list = getMenueList();
        rcyAccountList.layoutManager = LinearLayoutManager(activity)
        rcyAccountList.isNestedScrollingEnabled = false
        rcyAccountList.adapter = AccountAdapter(list, object : MyListCallbackListner {
            override fun onItemClick(id: Int, tag: Int) {
                if (id == 1) {
                    val i = Intent(getActivity(), MyOrdersActivity::class.java)
                    GlobalVariables.checkorderpageroot = "0"
                    startActivity(i)
                    activity?.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)

                } else if (id == 8) {
                    val preferences: SharedPreferences = requireActivity().getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.commit()

                    val preferences1: SharedPreferences = requireActivity().getSharedPreferences("shopidkey", Context.MODE_PRIVATE)
                    val editor1 = preferences1.edit()
                    editor1.clear()
                    editor1.commit()
                    val i = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(i)
                    activity?.finish()
                } else {
                    listener.onItemClick(id, tag)
                }
            }
        })
        hitApiTogetCustomerProfile()
        scrollViewId.scrollTo(0, scrollViewId.top)
    }

    private fun getMenueList(): java.util.ArrayList<AccountModel> {
        var list = ArrayList<AccountModel>()
        list.add(AccountModel(MY_ORDER, "My Order", ""))
//        list.add(AccountModel(MY_WALLET, "My Wallet", "7,764"))
        list.add(AccountModel(MY_PAYMENT, "My Payment", ""))
        list.add(AccountModel(MY_RATINGS, "My Rating & Review", ""))
        list.add(AccountModel(MY_CUSTOMER, "Customer Service", ""))
        list.add(AccountModel(MY_GIFT, "My Gift Cards", ""))
        list.add(AccountModel(MY_NOTIFICATION, "Notifications", ""))
        list.add(AccountModel(ADDRESS_LISTING_FRAGMENT, "My Delivery Address", ""))
        list.add(AccountModel(CHANGE_PASSWORD, "Change Password", ""))
        list.add(AccountModel(LOGOUT, "Logout", ""))

        return list;
    }

    companion object {

        val fragmentTAg = "MyAccountFragment"
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
        @JvmStatic
        fun newInstance() =
             MyAccountFragment().apply {
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
    private fun hitApiTogetCustomerProfile() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)
            val sharedPrefsLogin: SharedPreferences? = activity?.getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin?.getInt("globalid", 0)?:0

            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerProfile+"?clientId="+ GlobalVariables.clientID+"&id=&consumerId="+customerId

            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                var responsefinal = response.toString()
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = jsonObject.getJSONArray("result")
                        var image = ""
                        var name = ""
                        var mobile = ""
                        var email = ""
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            for (i in 0 until detailsarray.length()) {
                                val item = detailsarray.getJSONObject(i)
                                val obj = AddressItem()
                                name = item.optString("name")
                                email = item.optString("email")
                                mobile = item.optString("mobile")
                                image = item.optString("image")

                            }
                            updateData(name, email, mobile, image)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, Response.ErrorListener { MyProgressBar.getInstance().dismissProgressDialog() })
            requestQueue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun updateData(name: String, email: String, mobile: String, image: String) {
        txtVName?.text =name
        txtVEmail?.text =email
        txtVPhone?.text =mobile
        hitApiTogetCustomerAddress1()
    }
    override fun onResume() {
        super.onResume()
        /*(activity as AppCompatActivity?)?.supportActionBar?.hide()
        acaddressback?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                (activity as MainActivity?)?.showContent(0)
        }*/
    }
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
    private fun hitApiTogetCustomerAddress1() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
//            MyProgressBar.getInstance().showProgressDialog(context)
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
                                if (obj.isIsdefaultaddress()) {
                                    selectedAddress = obj
                                    break
                                }
                            }
                            if (addressList.size > 0 && selectedAddress == null) selectedAddress = addressList[0]

                            populateDefaultAddres(selectedAddress)

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

    private fun populateDefaultAddres(selectedAddress: AddressItem?) {
        val txvChangeAddressId: TextView? = maiVuew?.findViewById<TextView>(R.id.txvChangeAddressId)
        val address1: TextView? = maiVuew?.findViewById<TextView>(R.id.address1)
        val address2: TextView? = maiVuew?.findViewById<TextView>(R.id.address2)
        val address3: TextView? = maiVuew?.findViewById<TextView>(R.id.address3)
        if (selectedAddress != null) {
            address1?.text = selectedAddress.street + ", " + selectedAddress.street2
            address2?.text = selectedAddress.landmark
            address3?.text = selectedAddress.city + "-" + selectedAddress.zip
        }
        txvChangeAddressId?.setOnClickListener {
            listener.onItemClick(MyAccountFragment.ADDRESS_LISTING_FRAGMENT, 0)
        }
    }
}