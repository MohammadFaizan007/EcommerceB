package com.inlog.ecommerce.utility;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;

public class Utility {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
    public static void log(String tag, String message){
        Log.d(tag,message);
    }

    public static int strToInt(String qty) {
        try {
            return  Integer.parseInt(qty);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }
    public static float strToFloat(String qty) {
        try {
            return  Float.parseFloat(qty);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }
    public static Double strToDouble(String qty) {
        try {
            return  Double.parseDouble(qty);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 0.0;
    }
    public static String appendCurrencyWithText(String arg){
        Double dbl = 0.0;
        try {
            dbl =  Double.parseDouble(arg);
        }catch (Exception ex){

        }
        DecimalFormat formatter = new DecimalFormat("#,###,###.##");
        return "₹ "+formatter.format(dbl);
    }
}