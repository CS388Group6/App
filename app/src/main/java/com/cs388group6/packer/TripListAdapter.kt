package com.cs388group6.packer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TripListAdapter(private var trips: ArrayList<Trip>) :
    RecyclerView.Adapter<TripListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trips_list_rv_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tripListRVRowTitleLabel)
        private val weather = itemView.findViewById<ImageView>(R.id.itemsListRVRowWeatherDisplay)
        private val numItems = itemView.findViewById<TextView>(R.id.tripsListRVRowNumItemsLabel)
        private val date = itemView.findViewById<TextView>(R.id.tripsListRVRowDateLabel)



        fun bind(variable:Trip) {
            title.text = variable.title
//            bags.text = variable.bags
//            weight.text = variable.weight
//            weather.text = text.weather
//            numItems.text = variable.numItems
            date.text = variable.date
        }
    }
}
