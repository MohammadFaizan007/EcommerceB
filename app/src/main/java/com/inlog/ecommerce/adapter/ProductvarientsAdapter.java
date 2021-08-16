package com.inlog.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.model.ProdctVariants;

import java.util.List;

public class ProductvarientsAdapter extends BaseAdapter {
    private final LayoutInflater inflter;
    private final Context context;
    List<ProdctVariants> products ;

    public ProductvarientsAdapter(List<ProdctVariants> products, Context applicationContext) {
        this.products = products;
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.adapter_prodcts_varients, null);
        TextView txtVTitleId = view.findViewById(R.id.txtVTitleId);
        txtVTitleId.setText(products.get(i).getName());
        return view;
    }
}
