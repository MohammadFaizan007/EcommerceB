package com.inlog.ecommerce.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.android.volley.Response;

import org.json.JSONObject;

public class commonMethods {

    public static void showMessageOKCancel(String message, Context context) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight+rows*20;
        gridView.setLayoutParams(params);

    }
}
