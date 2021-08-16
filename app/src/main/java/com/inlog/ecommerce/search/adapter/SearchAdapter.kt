package com.inlog.ecommerce.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.model.SearchData
import com.inlog.ecommerce.search.viewholders.SearchViewHolder

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {
    private var dataList: ArrayList<SearchData> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.listitem_search, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindValues(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addData(data: ArrayList<SearchData>?){


        data?.let {
            dataList.addAll(it)
        }
        notifyDataSetChanged()
    }
}