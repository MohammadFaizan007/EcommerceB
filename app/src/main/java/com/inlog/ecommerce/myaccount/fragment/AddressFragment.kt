package com.inlog.ecommerce.myaccount.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.inlog.ecommerce.R
import com.inlog.ecommerce.activity.MainActivity
import com.inlog.ecommerce.adapter.AddressSpinnerAdapter
import com.inlog.ecommerce.adapter.AutoFillAdapter
import com.inlog.ecommerce.model.SuggestionItem
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import com.inlog.ecommerce.myaccount.model.AddressItem
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.toolbar_header.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
class AddressFragment : Fragment() {
    private var isForUpdate: Boolean = false
    private var cityDefaultSelected = -1
    private lateinit var zipAdapter: AutoFillAdapter
    private lateinit var zipCodeAdapter: AddressSpinnerAdapter
    private lateinit var cityAdapter: AddressSpinnerAdapter
    private lateinit var stateAdapter: AddressSpinnerAdapter
    private var addressId: Int = -1
    private  var listener: MyListCallbackListner? = null
    private var stateId =  "-1"
    private val countryId = "104"
    private var citySelectedItem: SuggestionItem? = null
    private var zipSelectedItem: SuggestionItem? = null
    var cityList = ArrayList<SuggestionItem>()
    var stateList = ArrayList<SuggestionItem>()
    var zipList = ArrayList<SuggestionItem>()
    private val addressList = ArrayList<AddressItem>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            addressId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpStateListAdapter()
        setUpCityListAdapter()
//        setUpZipListAdapter()
        if (addressId>0) {
            isForUpdate = true
            btnAddAddressId.text = getString(R.string.update_address)
            header.text = getString(R.string.update_address)
            hitApiTogetCustomerAddress()
        }
        else
        hitApiTogetState()

        rlNamePhoneDetailId?.setOnClickListener {
            rlNamePhoneDetailId.visibility = View.GONE
            llEditNamePhoneId.visibility = View.VISIBLE
        }

        btnAddAddressId.setOnClickListener {
            if(isValidInputs())
            hitApiTosaveAddress()
        }

        if(addressId<1)
            rlNamePhoneDetailId.performClick()
        else{
            rlNamePhoneDetailId.visibility = View.VISIBLE
            llEditNamePhoneId.visibility = View.GONE
        }

        txtVHome?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                txtVHome.background = activity?.resources?.getDrawable(R.drawable.rounde_selected_background,activity?.theme)
                txtVOffice.background = activity?.resources?.getDrawable(R.drawable.rectangle_corner_grey,activity?.theme)
                txtVOther.background = activity?.resources?.getDrawable(R.drawable.rectangle_corner_grey,activity?.theme)
            }

        }
        txtVOffice?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                txtVOffice.background = activity?.resources?.getDrawable(R.drawable.rounde_selected_background,activity?.theme)
                txtVHome.background = activity?.resources?.getDrawable(R.drawable.rectangle_corner_grey,activity?.theme)
                txtVOther.background = activity?.resources?.getDrawable(R.drawable.rectangle_corner_grey,activity?.theme)
            }


        }
        txtVOther?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                txtVOther.background = activity?.resources?.getDrawable(R.drawable.rounde_selected_background,activity?.theme)
                txtVHome.background = activity?.resources?.getDrawable(R.drawable.rectangle_corner_grey,activity?.theme)
                txtVOffice.background = activity?.resources?.getDrawable(R.drawable.rectangle_corner_grey,activity?.theme)
            }


        }
        edtVPincode.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, arg1: View?, position: Int, arg3: Long) {
                val item: Any = parent.getItemAtPosition(position)
                if (item is SuggestionItem) {
                    zipSelectedItem = item as SuggestionItem

                }
            }
        })
        edtVPincode.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edtVPincode.text.toString().length < 6)
                    zipSelectedItem = null
                else
                    zipSelectedItem = SuggestionItem(edtVPincode.text.toString(),edtVPincode.text.toString())

            }

        })

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        acaddressback?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                activity?.supportFragmentManager?.popBackStack()
        }
    }
    private fun hitApiTogetState() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)

            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getCountryState+"?clientId="+GlobalVariables.clientID+"&countryId="+countryId

            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,null , Response.Listener { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()

                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONArray()
                        var childarray = JSONArray()
                        detailsarray = jsonObject.getJSONArray("result")

                        stateList.clear()
                        stateList.add(SuggestionItem("Select State","-1"))
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            for (i in 0 until   detailsarray.length()){
                                val item = detailsarray.getJSONObject(i)
                                stateList.add(SuggestionItem(item.optString("name"),item.optString("id")))
                            }
                            updateSateList()

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

    private fun updateSateList() {
        stateAdapter.notifyDataSetChanged()
        if(isForUpdate) {
            var selectedIndex = -1
            for (i in 0 until stateList.size) {
                if (addressList[0].state_id.equals(stateList[i].id)) {
                    selectedIndex = i
                    break
                }
            }
            if (selectedIndex != -1)
                spinnerStateId.setSelection(selectedIndex)
        }
    }

    private fun setUpStateListAdapter() {
        stateList.add(SuggestionItem("Select State","-1"))
        stateAdapter = AddressSpinnerAdapter(context,stateList)
        spinnerStateId.adapter = stateAdapter
        spinnerStateId.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(!stateList.get(position).id.equals("-1")) {
                    stateId = stateList.get(position).id
                    hitApiTogetCities()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun hitApiTogetCities() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)

            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getCity+"?clientId="+GlobalVariables.clientID+"&countryId="+countryId+"&stateId="+stateId

            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,null , Response.Listener { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONArray()
                        var childarray = JSONArray()
                        detailsarray = jsonObject.getJSONArray("result")
                        cityList.clear()
                        cityList.add(SuggestionItem("Select City","-1"))
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            for (i in 0 until   detailsarray.length()){
                                val item = detailsarray.getJSONObject(i)
                                cityList.add(SuggestionItem(item.optString("name"),item.optString("id")))
                            }
                            updateCityList()
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

    private fun updateCityList() {
        cityAdapter.notifyDataSetChanged()
        if(isForUpdate && cityDefaultSelected ==-1){
            for (i in 0 until cityList.size){
                if(addressList[0].city_id.equals(cityList[i].id)){
                    cityDefaultSelected = i
                    break
                }
            }

        }else {
            cityDefaultSelected = 0
            edtVPincode?.setText("")
        }
        spinnerCity.setSelection(cityDefaultSelected)

    }

    private fun hitApiToGetZipCode() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)

            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getPinCode+"?clientId="+GlobalVariables.clientID+"&countryId="+countryId+"&stateId="+stateId

            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,null , Response.Listener { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONArray()
                        var childarray = JSONArray()
                        detailsarray = jsonObject.getJSONArray("result")
                        zipList.clear()
                        zipList.add(SuggestionItem("Select Zip","-1"))
                        if (jsonObject.optString("status").equals("OK", ignoreCase = true)) {
                            for (i in 0 until   detailsarray.length()){
                                val item = detailsarray.getJSONObject(i)
                                zipList.add(SuggestionItem(item.optString("pincode"),item.optString("pincode")))
                            }
//                            zipAdapter.notifyDataSetChanged()
                            setUpZipListAdapterAuto()
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

    private fun setUpCityListAdapter() {
        cityList.add(SuggestionItem("Select City","-1"))
        cityAdapter= AddressSpinnerAdapter(context,cityList)
        spinnerCity.adapter = cityAdapter
        spinnerCity.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(!cityList.get(position).id.equals("-1")) {
                    hitApiToGetZipCode()
                }
                citySelectedItem = cityList.get(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
    private fun setUpZipListAdapter() {
        zipList.add(SuggestionItem("Select Zip","-1"))
        setUpZipListAdapterAuto()
//        zipCodeAdapter = AddressSpinnerAdapter(context,zipList)
      /*  spinnerZipId.adapter = zipCodeAdapter
        spinnerZipId.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                zipSelectedItem = zipList.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }*/
    }
    private fun setUpZipListAdapterAuto() {
        zipAdapter = AutoFillAdapter(context, 0, zipList)
        edtVPincode?.setAdapter(zipAdapter)
        edtVPincode.setThreshold(2)

        if(isForUpdate && edtVPincode.text.toString().equals(addressList[0].zip)){

        }else edtVPincode.setText("")
    }
    private fun isValidInputs(): Boolean {
        if (edtFirstName?.text.toString().isEmpty()) {
            showMessage(activity?.getString(R.string.please_enter_first_name))
            return false
        }
        if (edtLastName?.text.toString().isEmpty()) {
            showMessage(activity?.getString(R.string.please_enter_last_name))
            return false
        }
        if (edtPhoineNumber?.text.toString().isEmpty()) {
            showMessage(activity?.getString(R.string.please_enter_phone))
            return false
        }
        if (edtPhoineNumber?.text.toString().length<10) {
            showMessage(activity?.getString(R.string.please_enter_valid_phone))
            return false
        }
        if (edtVEmailId?.text.toString().isEmpty()) {
            showMessage(activity?.getString(R.string.please_enter_email_id))
            return false
        }
        if (!isValidEmail(edtVEmailId?.text)){
            showMessage(activity?.getString(R.string.please_enter_valid_email_id))
            return false
        }
        if (edtVStreetDetailsToLocateYou?.text.toString().isEmpty()) {
            showMessage(activity?.getString(R.string.please_enter_street_details))
            return false
        }
        if (edtVLandmark?.text.toString().isEmpty()) {
            showMessage(activity?.getString(R.string.please_enter_street_land_mark))
            return false
        }
        if (edtVAreaDetails?.text.toString().isEmpty()) {
                    showMessage(activity?.getString(R.string.please_enter_area_details))
                    return false
        }

        if(stateId == null || stateId?.equals("-1")){
            showMessage(activity?.getString(R.string.please_select_state))
            return false
        }
        if(citySelectedItem == null || citySelectedItem?.id.equals("-1")){
            showMessage(activity?.getString(R.string.please_select_city))
            return false
        }
        if(zipSelectedItem == null || zipSelectedItem?.id.equals("-1")){
            showMessage(activity?.getString(R.string.please_select_zipcode))
            return false
        }

        return true
    }

    private fun showMessage(string: String?) {
        Toast.makeText(activity,string,Toast.LENGTH_LONG).show()
    }

    companion object {
        val fragmentTag = "AddressFragment"
        @JvmStatic
        fun newInstance(addressId : Int) =
                AddressFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, addressId)
                    }
                }
    }

    fun hitApiTosaveAddress() {
        val requestQueue = Volley.newRequestQueue(context)
        try {

            MyProgressBar.getInstance().showProgressDialog(context)
            val sharedPrefsLogin: SharedPreferences? = activity?.getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
             var customerId = sharedPrefsLogin?.getInt("globalid", 0)?:0

            var  url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.addMultipleAddress
            if(isForUpdate)
             url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.editConsumerAddress
            var name = "${edtFirstName?.text.toString()} ${edtLastName?.text.toString()}"
            val params = HashMap<String,Any> ()
            val mapParams = HashMap<String,Any> ()
            if(isForUpdate)
            params.put("AddressId",addressId)
            params.put("clientId",GlobalVariables.clientID)
            params.put("consumerId",customerId)
            params.put("name",name)
            params.put("email",edtVEmailId.text.toString())
            params.put("mobile",edtPhoineNumber?.text.toString())
            params.put("isDefaultAddress",checkSetAsDefaultAddress.isChecked)
            params.put("street",edtVStreetDetailsToLocateYou?.text.toString())
            params.put("street2",edtVAreaDetails.text.toString())
            params.put("cityId",if(citySelectedItem!=null ) citySelectedItem?.id?:"" else "")
            params.put("zip",if(zipSelectedItem!=null ) zipSelectedItem?.id?:"" else "")
            params.put("landmark",edtVLandmark?.text.toString())
            params.put("stateId",stateId)
            params.put("countryId",countryId)
            params.put("latitude",0)/*TODO pending*/
            params.put("longitude",0)/*TODO pending*/

            mapParams.put("params",params)
            var objectParams = JSONObject(mapParams as Map<*, *>)
            Utility.log("Req body ", objectParams.toString())
            Utility.log("URL ", url)
            var method = Request.Method.POST
            if(isForUpdate)
                method = Request.Method.PUT
            val jsonObjectRequest = JsonObjectRequest(method, url, objectParams, Response.Listener { response ->
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var detailsarray = JSONObject()
                        detailsarray = jsonObject.optJSONObject("result")
                        if (detailsarray.optString("status").equals("OK", ignoreCase = true)) {
                           showMessage(activity?.getString(R.string.address_added_successfully))
                            activity?.onBackPressed()
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
    private fun hitApiTogetCustomerAddress() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)
            val sharedPrefsLogin: SharedPreferences? = activity?.getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin?.getInt("globalid", 0)?:0

            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.getConsumerAddress+"?clientId="+ GlobalVariables.clientID+"&consumerId="+customerId+"&id="+addressId

            Utility.log("URL ", url)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,null , Response.Listener { response ->
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
                            for (i in 0 until   detailsarray.length()){
                                val item = detailsarray.getJSONObject(i)
                                val obj = AddressItem()
                                obj.city =item.optString("city")
                                obj.city_id =item.optString("city_id")
                                obj.country =item.optString("country")
                                obj.email =item.optString("email")
                                obj.id =item.optString("id")
                                obj.landmark =item.optString("landmark")
                                obj.mobile =item.optString("mobile")
                                obj.name =item.optString("name")
                                obj.state =item.optString("state")
                                obj.state_id =item.optString("state_id")
                                obj.street =item.optString("street")
                                obj.street2 =item.optString("street2")
                                obj.zip =item.optString("zip")
                                obj.isIsdefaultaddress =item.optBoolean("isdefaultaddress")
                                addressList.add(obj)
                            }
                            if(!addressList.isNullOrEmpty())
                            updateUI(addressList[0])
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

    private fun updateUI(addressItem: AddressItem) {
        addressItem.let {
            txtVName.text = "Name : "+it.name
            txtVPhone.text = "Phone Number : "+it.mobile
            edtPhoineNumber.setText(it.mobile)
            edtVEmailId.setText(it.email)
            edtVStreetDetailsToLocateYou.setText(it.street)
            edtVLandmark.setText(it.landmark)
            edtVAreaDetails.setText(it.street2)
            edtVPincode?.setText(it.zip)
            checkSetAsDefaultAddress.isChecked = it.isIsdefaultaddress
            checkSetAsDefaultAddress.isEnabled = !it.isIsdefaultaddress

            var nameList = it.name.split(" ")
            if(!nameList.isNullOrEmpty()) {
                edtFirstName.setText(nameList[0])
                if(nameList.size>1)
                    edtLastName.setText(nameList[1])

            }
        }
        hitApiTogetState()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MyListCallbackListner
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener.  */
        }
    }
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}