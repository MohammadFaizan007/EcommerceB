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
import com.inlog.ecommerce.myaccount.listner.MyListCallbackListner
import com.inlog.ecommerce.util.GlobalVariables
import com.inlog.ecommerce.utility.MyProgressBar
import com.inlog.ecommerce.utility.ShowAlert
import com.inlog.ecommerce.utility.Utility
import kotlinx.android.synthetic.main.fragment_enter_email_phon_forgot.*
import kotlinx.android.synthetic.main.fragment_enter_email_phon_forgot.btnSubmit
import kotlinx.android.synthetic.main.toolbar_header.*
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [EnterEmailPhonForgotFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterEmailPhonForgotFragment : Fragment() {
    private  var listener: MyListCallbackListner?=null

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_email_phon_forgot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        header.text=getString(R.string.enter_email_or_phone_number)
        header.text= getString(R.string.resetpassword)
        acaddressback.setOnClickListener {
            activity?.onBackPressed()
        }
        btnSubmit.setOnClickListener {
            if(edtPhoneEmail.text.toString().trim().length>1){
                hitApiToSendOTP()
            }else{
                txtILayoutEdtPhoneEmail.error=getString(R.string.enter_email_or_phone)
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                EnterEmailPhonForgotFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
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
    private fun hitApiToSendOTP() {
        val requestQueue = Volley.newRequestQueue(context)
        try {
            MyProgressBar.getInstance().showProgressDialog(context)
            val url = GlobalVariables.COMMON_URL_SERVICE + GlobalVariables.sendResetPasswordOTP
            val jsonObj = JSONObject()

            ForgotPasswordActivity.emailOrPhone  =edtPhoneEmail.text.toString().trim();
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
                            showSuccessMsg(resultObj.optString("result"))
                        }
                        else
                        {
                            showSuccessMsg(resultObj.optString("msg"))
                            listener?.onItemClick(ForgotPasswordActivity.RESET_PASSWORD_FRAGMENT,0)
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
            listener?.onItemClick(ForgotPasswordActivity.RESET_PASSWORD_FRAGMENT,0)
        },cancelabel = false).show()
    }
}