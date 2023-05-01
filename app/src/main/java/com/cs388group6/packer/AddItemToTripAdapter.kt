package com.cs388group6.packer



import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class AddItemToTripAdapter(private var items: ArrayList<Item>) :
    RecyclerView.Adapter<AddItemToTripAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemCLickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_list_rv_row, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.itemsListRVRowTitleLabel)
        private val weight = itemView.findViewById<TextView>(R.id.itemsListRVRowWeightLabel)
        private val image = itemView.findViewById<ImageView>(R.id.itemsListRVRowImageDisplay)
        private val category = itemView.findViewById<TextView>(R.id.itemsListRVRowCategoryLabel)
        private val editButton = itemView.findViewById<Button>(R.id.itemsListRVEditButton)
        private val deleteButton = itemView.findViewById<Button>(R.id.itemsListRVDeleteButton)

        fun bind(variable: Item) {
            // Get image
            val decodedString: ByteArray = Base64.decode(variable.image, Base64.DEFAULT)
            var bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            name.text = variable.name
            weight.text = variable.weight
            image.setImageBitmap(bitmap)
            category.text = variable.category
            val itemID = variable.itemID.toString()
            editButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
        }

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}