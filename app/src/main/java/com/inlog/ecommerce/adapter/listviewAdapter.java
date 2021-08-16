package com.inlog.ecommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.model.specification;

import java.util.ArrayList;

public class listviewAdapter extends BaseAdapter {

     public ArrayList<specification> productList;
     Activity activity;

    public listviewAdapter(Activity activity, ArrayList<specification> productList) {
        super();
        this.activity = activity;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
       return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView name;
        TextView value;

    }

    @Override
     public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.speclist_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        specification item = productList.get(position);
        holder.name.setText(item.getName().toString());
        holder.value.setText(item.getValue().toString());

        return convertView;
    }
}