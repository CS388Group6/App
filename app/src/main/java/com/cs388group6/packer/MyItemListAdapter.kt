package com.cs388group6.packer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class MyItemListAdapter(private var items: ArrayList<Item>) :
    RecyclerView.Adapter<MyItemListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_list_rv_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.itemsListRVRowTitleLabel)
        private val weight = itemView.findViewById<TextView>(R.id.itemsListRVRowWeightLabel)
        private val image = itemView.findViewById<ImageView>(R.id.itemsListRVRowImageDisplay)
        private val category = itemView.findViewById<TextView>(R.id.itemsListRVRowCategoryLabel)

        fun bind(variable: Item) {
            name.text = variable.name
            weight.text = variable.weight
            // image.text = variable.image
            category.text = variable.category

        }
    }
}