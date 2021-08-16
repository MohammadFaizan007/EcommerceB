package com.inlog.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.inlog.ecommerce.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String> mItems;
    private ItemListener mListener;
    private JSONArray marray = null;
    public ItemAdapter(List<String> items, JSONArray array, ItemListener listener) {
        mItems = items;
        marray = array;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.setData(mItems.get(position), (JSONObject) marray.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        String item;
        JSONObject object;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

        void setData(String item, JSONObject object) {
            this.item = item;
            this.object = object;
            textView.setText(item);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                try {
                    mListener.onItemClick(item,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface ItemListener {
        void onItemClick(String item,JSONObject object) throws JSONException;
    }
}
