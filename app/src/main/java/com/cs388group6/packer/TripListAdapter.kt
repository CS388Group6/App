package com.cs388group6.packer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.util.*

class TripListAdapter(private var trips: MutableList<Trip>) :
    RecyclerView.Adapter<TripListAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemCLickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trips_list_rv_row, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trips[position])

    }

    override fun getItemCount(): Int {
        return trips.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tripListRVRowTitleLabel)
        private val weather = itemView.findViewById<ImageView>(R.id.itemsListRVRowWeatherDisplay)
        private val numItems = itemView.findViewById<TextView>(R.id.tripsListRVRowNumItemsLabel)
        private val date = itemView.findViewById<TextView>(R.id.tripsListRVRowDateLabel)
        private val weight = itemView.findViewById<TextView>(R.id.tripListRVRowWeightLabel)
        private val location = itemView.findViewById<TextView>(R.id.tripsListRVRowLoacationLabel)
        private val weatherLabel = itemView.findViewById<TextView>(R.id.tripListRVRowWeatherLabel)



        @SuppressLint("SetTextI18n")
        fun bind(variable:Trip) {
            title.text = variable.title
            weight.text = "todo"
//            weather.text = variable.weather
            val itemcount = variable.items?.size?.minus(1)
            numItems.text = itemcount.toString() + " Items"
            date.text = variable.date
            location.text = variable.location

            if(variable.weather!="") {
                val gson = Gson()
                val weatherData = gson.fromJson(variable.weather, WeatherItem::class.java)
                weatherLabel.text = "Average Temperature: " + weatherData?.avgtemp_f + "° F"
                Glide.with(itemView)
                    .load(weatherData?.image)
                    .centerInside()
                    .into(weather)
            }
        }

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}
