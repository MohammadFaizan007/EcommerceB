package com.inlog.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.inlog.ecommerce.R;
import com.inlog.ecommerce.model.SuggestionItem;

import java.util.ArrayList;

public class AutoFillAdapter extends ArrayAdapter<SuggestionItem> {
    private final String MY_DEBUG_TAG = "AutoFillAdapter";
    private ArrayList<SuggestionItem> items;
    private ArrayList<SuggestionItem> itemsAll;
    private ArrayList<SuggestionItem> suggestions;
    private int viewResourceId;

    public AutoFillAdapter(Context context, int viewResourceId, ArrayList<SuggestionItem> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<SuggestionItem>) items.clone();
        this.suggestions = new ArrayList<SuggestionItem>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_autofil, null);
        }
        SuggestionItem SuggestionItem = items.get(position);
        if (SuggestionItem != null) {
            TextView txtVTile = (TextView) v.findViewById(R.id.txtVTile);
            if (txtVTile != null) {
                txtVTile.setText(SuggestionItem.getName());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((SuggestionItem)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (SuggestionItem SuggestionItem : itemsAll) {
                    if(SuggestionItem.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(SuggestionItem);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<SuggestionItem> filteredList = (ArrayList<SuggestionItem>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (SuggestionItem c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}
