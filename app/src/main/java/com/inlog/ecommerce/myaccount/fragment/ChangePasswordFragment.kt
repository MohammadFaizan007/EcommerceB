package com.inlog.ecommerce.myaccount.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.inlog.ecommerce.R
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.ShowAlert
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.layout_change_password.*
import kotlinx.android.synthetic.main.toolbar_header.*
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var maiVuew =  inflater.inflate(R.layout.fragment_change_password, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        return maiVuew
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header?.text = getString(R.string.change_password)
        acaddressback.setOnClickListener {
            activity?.onBackPressed()
        }
        btnSubmit.setOnClickListener {
            if(isValidInputFields()){
                hitApiToChangePassword()
            }else{

            }
        }
    }

    private fun isValidInputFields(): Boolean {
        if(editExisting.text.toString().equals("")){
            txtILayoutExisting.error= getString(R.string.enter_password)
            return false
        }else
            txtILayoutExisting.error= null

        if(editNew.text.toString().equals("")){
            txtILayoutNew.error= getString(R.string.enter_new_pass)
            return false
        }else
            txtILayoutNew.error=null

        if(!editConfirm.text.toString().equals(editNew.text.toString())){
            txtILayoutConfirm.error= getString(R.string.pass_not_matched)
            return false
        }
        else
            txtILayoutConfirm.error=null
        return true

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangePasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                ChangePasswordFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
    private fun hitApiToChangePassword() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)
            val sharedPrefsLogin: SharedPreferences? = activity?.getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin?.getInt("globalid", 0)?:0

            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.changeConsumerPassword
              val jsonObj = JSONObject()
            jsonObj.put("clientId", GlobalVariables.clientID)
            jsonObj.put("consumerId", customerId)
            jsonObj.put("currentPassword", sharedPrefsLogin?.getString("password", "")!!)
            jsonObj.put("newPassword", editNew.text)
            val jsonObjMain = JSONObject()

            jsonObjMain.put("params", jsonObj)


            val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, jsonObjMain, { response ->
                Utility.log("Api Url = ", url)
                Utility.log("Api Reqbody = ", jsonObjMain.toString())
                Utility.log("Api Response  = ", response.toString())
                MyProgressBar.getInstance().dismissProgressDialog()
                var responsefinal = response.toString()
                val jsonObject: JSONObject
                if (responsefinal != null) {
                    try {
                        jsonObject = JSONObject(responsefinal)
                        var resultObj = jsonObject.optJSONObject("result")
                        if (resultObj.optString("status").equals("OK", ignoreCase = true)) {
                            showSuccessMsg(resultObj.optString("result"))
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

    private fun showSuccessMsg(mesage: String) {
        ShowAlert(requireActivity(), requireActivity().getString(R.string.app_name), mesage, requireActivity().getString(R.string.ok), {
           activity?.onBackPressed()
        },cancelabel = false).show()
    }
}