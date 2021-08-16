package com.inlog.ecommerce.utility

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.inlog.ecommerce.R

class ShowAlert(val context : Context,val title : String,val message : String,val possitiveBtnText : String,val onPossitiveClck :View.OnClickListener? = null ,val negativeBtnText : String = "",val onNegativeClick :View.OnClickListener? = null,val cancelabel : Boolean = true) {

    fun show(){
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle(title)
        //set message for alert dialog
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(possitiveBtnText){dialogInterface, which ->
            onPossitiveClck?.onClick(alertDialog?.getButton(which))
            dialogInterface?.cancel()
        }
        builder.setNegativeButton(negativeBtnText){dialogInterface, which ->
            onNegativeClick?.onClick(alertDialog?.getButton(which))
            dialogInterface?.cancel()
        }
        // Create the AlertDialog
        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(cancelabel)
        alertDialog.setIcon(R.mipmap.ic_launcher_round)
        alertDialog.show()
    }
}
