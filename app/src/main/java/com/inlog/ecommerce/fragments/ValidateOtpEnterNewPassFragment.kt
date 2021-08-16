package com.inlog.ecommerce.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.inlog.ecommerce.R
import com.inlog.ecommerce.activity.ForgotPasswordActivity
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.ShowAlert
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.fragment_validate_otp_enter_new_pass.*
import kotlinx.android.synthetic.main.layout_change_password.*
import kotlinx.android.synthetic.main.toolbar_header.*
import org.json.JSONException
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"

class ValidateOtpEnterNewPassFragment : Fragment() {
    private var param1: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validate_otp_enter_new_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header.text=getString(R.string.reset_password)
        btnSubmit.text=getString(R.string.reset_)
        txtILayoutExisting.visibility = View.GONE
        acaddressback.setOnClickListener {
            activity?.onBackPressed()
        }
        btnSubmit.setOnClickListener {
            if(isValidInputFields()){
                hitApiToResetPassword()
            }else{

            }
        }
    }


    private fun hitApiToResetPassword() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)
            val sharedPrefsLogin: SharedPreferences? = activity?.getSharedPreferences("globalloginvalues", Context.MODE_PRIVATE)
            var customerId = sharedPrefsLogin?.getInt("globalid", 0)?:0



            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.resetConsumerPassword
            val jsonObj = JSONObject()

            var numeric = false

            numeric = ForgotPasswordActivity.emailOrPhone .matches("-?\\d+(\\.\\d+)?".toRegex())

            if (numeric) {
                jsonObj.put("mobile", ForgotPasswordActivity.emailOrPhone )
                jsonObj.put("email", "")
            }
            else {
                jsonObj.put("email", ForgotPasswordActivity.emailOrPhone )
                jsonObj.put("mobile", "")
            }

            jsonObj.put("clientId", GlobalVariables.clientID)
            jsonObj.put("otp", customerId)
            jsonObj.put("newPassword", editNew.text)
            val jsonObjMain = JSONObject()

            jsonObjMain.put("params", jsonObj)


            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObjMain, { response ->
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
                            showSuccessMsg(resultObj.optString("result"),"0")
                            }
                        else{
                            showSuccessMsg(resultObj.optString("msg"),"1")
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

    companion object {
              @JvmStatic
        fun newInstance(param1: String) =
                ValidateOtpEnterNewPassFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
    private fun isValidInputFields(): Boolean {

        if(pin_view.text.toString().equals("")){
            txtVOtpErrorId.text= getString(R.string.please_enter_otp)
            txtVOtpErrorId.visibility = View.VISIBLE
            return false
        }else if(pin_view.text.toString().length<6){
            txtVOtpErrorId.text= getString(R.string.please_enter_valid_otp)
            txtVOtpErrorId.visibility = View.VISIBLE
            return false
        }else {
            txtVOtpErrorId.text = ""
            txtVOtpErrorId.visibility = View.GONE
        }

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
    private fun showSuccessMsg(mesage: String,flag:String) {
        ShowAlert(requireActivity(), requireActivity().getString(R.string.app_name), mesage, requireActivity().getString(R.string.ok), {
            activity?.finish()
            if (flag.equals("0"))
                {
                    activity?.finish()
               }

        },cancelabel = false).show()
    }
}