package com.inlog.ecommerce.utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.RelativeLayout;

import com.inlog.ecommerce.R;


public class MyProgressBar {

	private AnimationDrawable frameAnimation;
	private Dialog dialog;
	private static MyProgressBar instance;
	private RelativeLayout RelProgress;
	private ProgressDialog progressDialog;
//	private SimpleArcDialog mDialog;
	public static MyProgressBar getInstance() {
		if (instance == null) {
			instance = new MyProgressBar();
		}
		return instance;
	}

	public void showProgressDialog(Context context)
	{
		try{
				dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
				dialog.setContentView(R.layout.progress_loading);
				dialog.setCancelable(false);
			    dialog.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public void dismissProgressDialog() {

		if (dialog!=null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
}
