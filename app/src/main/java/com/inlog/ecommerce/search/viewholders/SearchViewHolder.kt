package com.inlog.ecommerce.search.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.model.SearchData
import kotlinx.android.synthetic.main.listitem_search.view.*

class SearchViewHolder  (itemView: View) : RecyclerView.ViewHolder(itemView)  {
    fun bindValues(data: SearchData){
        itemView.itemName.text = data.name
    }
}