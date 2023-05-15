package com.cs388group6.packer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AddItemToTrip : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var adapter: AddItemToTripAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var user: String
    private val items = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_items_list_screen)

        database = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth
        user = auth.currentUser?.uid ?: ""
        val recyclerView = findViewById<RecyclerView>(R.id.myItemsListScreenRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        findViewById<FloatingActionButton>(R.id.floatingActionButton).visibility = View.GONE

        val tripId = intent.getStringExtra("trip").toString()

        adapter = AddItemToTripAdapter(items)
        recyclerView.adapter = adapter

        database.child("items").orderByChild("userID").equalTo(user).addValueEventListener(object :
            ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                if (snapshot.exists()){
                    for (item in snapshot.children){
                        val itemData = item.getValue(Item::class.java)
                        items.add(itemData!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w( "loadPost:onCancelled", error.toException())
            }
        })


        adapter.setOnItemCLickListener(object : AddItemToTripAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val itemget = database.child("Trips").child(tripId).child("items").get()
                itemget.addOnCompleteListener {
                    var newTItems = mutableListOf<String>()
                    for (item in it.result.children){
                        val itemData = item.getValue<String>().toString()
                        newTItems.add(itemData)
                    }
                    newTItems.add(items[position].itemID.toString())
                    database.child("Trips").child(tripId).child("items").setValue(newTItems)
                    finish()
                }
            }
        })


        //Set default selection
        val bottomNavigationView1: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView1.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_login -> { true }
                R.id.nav_logout -> {true }
                R.id.nav_home -> { true }
                R.id.nav_list -> { true }
            }
            true
        }
        bottomNavigationView1.selectedItemId = R.id.nav_list

        // Bottom Navigation Selection
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_login -> { startActivity(Intent(this, MainActivity::class.java)) }
                R.id.nav_logout -> { startActivity(Intent(this, MainActivity::class.java)) }
                R.id.nav_home -> { startActivity(Intent(this, TripList::class.java)) }
                R.id.nav_list -> { startActivity(Intent(this, MyItemList::class.java)) }
            }
            true
        }

    }
}
