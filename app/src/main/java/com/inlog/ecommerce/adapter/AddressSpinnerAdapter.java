package com.inlog.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.model.SuggestionItem;

import java.util.ArrayList;

public class AddressSpinnerAdapter extends BaseAdapter {
    ArrayList<SuggestionItem>  listOfItem;
    private LayoutInflater mInflater;

    public AddressSpinnerAdapter(Context context, ArrayList<SuggestionItem> listOfItem) {
        this.listOfItem = listOfItem;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listOfItem.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_autofil, null);
            holder = new ViewHolder();
            holder.txtVTile = (TextView) convertView.findViewById(R.id.txtVTile);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtVTile.setText(listOfItem.get(i).getName());
        return convertView;
    }
    static class ViewHolder {
        TextView txtVTile; //spinner name
    }
}
