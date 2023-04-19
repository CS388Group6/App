package com.cs388group6.packer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TripListDetail: AppCompatActivity()  {
    private lateinit var database: DatabaseReference
    private lateinit var adapter: MyItemListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_overview_screen)

        database = FirebaseDatabase.getInstance().reference
        val recyclerView = findViewById<RecyclerView>(R.id.tripOverviewItemsRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyItemListAdapter(ArrayList())
        recyclerView.adapter = adapter


        //Trip Edit button
        findViewById<FloatingActionButton>(R.id.tripOverviewEditButton).setOnClickListener {
            val intent = Intent(this, TripListAdd::class.java)
            startActivity(intent)
        }

        //Trip Delete button
        findViewById<FloatingActionButton>(R.id.tripOverviewDeleteButton).setOnClickListener {
            val intent = Intent(this, TripListAdd::class.java)
            startActivity(intent)
        }

        //Trip Item Add button
        findViewById<FloatingActionButton>(R.id.tripOverviewAddItemButton).setOnClickListener {
            val intent = Intent(this, TripListAdd::class.java)
            startActivity(intent)
        }

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
        bottomNavigationView1.selectedItemId = R.id.nav_home
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