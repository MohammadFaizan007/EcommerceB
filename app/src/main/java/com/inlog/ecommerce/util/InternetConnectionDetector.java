package com.inlog.ecommerce.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sakshi.kapoor on 1/31/2018.
 */

public class InternetConnectionDetector {
Context context;

    public InternetConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo!=null && networkInfo.isConnected();
        return isConnected;
    }
}
