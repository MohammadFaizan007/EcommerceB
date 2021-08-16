package com.inlog.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.model.searchbrand
import java.util.*

class ListFilterAdapter(private val searcList: ArrayList<searchbrand>) : RecyclerView.Adapter<ListFilterAdapter.ViewHolder>(), Filterable {
    var countryFilterList = ArrayList<searchbrand>()

    init {
        countryFilterList = searcList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_listing_filetr, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBocFilter.text = countryFilterList[position].brandname
        holder.checkBocFilter.isChecked = countryFilterList[position].isChecked
        holder.checkBocFilter.tag = position
        holder.checkBocFilter.setOnCheckedChangeListener { compoundButton, b ->
           var positioLocal = compoundButton.tag as Int
            if(positioLocal!=-1 && positioLocal<countryFilterList.size) {
                val itemLis = countryFilterList[positioLocal]
                itemLis.checked = b
            }
        }
    }

    override fun getItemCount(): Int {
        return countryFilterList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBocFilter: AppCompatCheckBox

        init {
            checkBocFilter = itemView.findViewById(R.id.checkBocFilter)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = searcList
                } else {
                    val resultList = ArrayList<searchbrand>()
                    for (row in searcList) {
                        if (row.brandname.contains(charSearch,true)) {
                            resultList.add(row)
                        }
                    }
                    countryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }


            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<searchbrand>
                notifyDataSetChanged()
            }

        }
    }


}