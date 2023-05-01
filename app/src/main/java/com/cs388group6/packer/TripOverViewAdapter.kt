package com.cs388group6.packer

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import java.util.ArrayList

class TripOverViewAdapter (private var items: ArrayList<Item>, val tripID: String) :
    RecyclerView.Adapter<TripOverViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_list_rv_row, parent, false)
        return ViewHolder(view, tripID)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View, tripID: String) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.itemsListRVRowTitleLabel)
        private val weight = itemView.findViewById<TextView>(R.id.itemsListRVRowWeightLabel)
        private val image = itemView.findViewById<ImageView>(R.id.itemsListRVRowImageDisplay)
        private val category = itemView.findViewById<TextView>(R.id.itemsListRVRowCategoryLabel)
        private val editButton = itemView.findViewById<Button>(R.id.itemsListRVEditButton)
        private val deleteButton = itemView.findViewById<Button>(R.id.itemsListRVDeleteButton)
        private val  tripID = tripID

        fun bind(variable: Item) {
            if (variable.image != null){
                val decodedString: ByteArray = Base64.decode(variable.image, Base64.DEFAULT)
                var bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                image.setImageBitmap(bitmap)
            }

            name.text = variable.name
            weight.text = variable.weight
            category.text = variable.category
            editButton.visibility = View.GONE
            deleteButton.text = "Remove"

            deleteButton.setOnClickListener {
                val database = FirebaseDatabase.getInstance().reference
                val itemget = database.child("Trips").child(tripID.toString()).child("items").get()
                itemget.addOnCompleteListener {
                    var tItems = mutableListOf<String>()
                    for (item in it.result.children){
                        val itemData = item.getValue<String>().toString()
                        tItems.add(itemData)
                    }
                    tItems.remove(variable.itemID.toString())
                    database.child("Trips").child(tripID.toString()).child("items").setValue(tItems)
                }
            }
        }
    }
}