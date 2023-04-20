package com.cs388group6.packer

import android.content.DialogInterface
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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
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
        private val editButton = itemView.findViewById<Button>(R.id.itemsListRVEditButton)
        private val deleteButton = itemView.findViewById<Button>(R.id.itemsListRVDeleteButton)

        fun bind(variable: Item) {
            name.text = variable.name
            weight.text = variable.weight
            // image.text = variable.image
            category.text = variable.category
            var itemID = variable.itemID.toString()
            deleteButton.setOnClickListener {
                val database = FirebaseDatabase.getInstance().reference
                AlertDialog.Builder(itemView.context)
                    .setTitle("Confirm Deletion")
                    .setMessage("This action Cannot be undone. Are you Sure?")
                    .setPositiveButton("Confirm", DialogInterface.OnClickListener{ _, _ ->
                        val rem = database.child("items").child(itemID.toString()).removeValue()
                        rem.addOnFailureListener{error ->
                            Log.w("DatabaseError", error)
                        }
                    })
                    .setNegativeButton("Cancel"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show();
            }
            editButton.setOnClickListener {

            }
        }
    }
}