package com.cs388group6.packer

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TripOverView : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var titleView: TextView
    private lateinit var weightView: TextView
    private lateinit var itemCountView: TextView
    private lateinit var dateView: TextView
    private lateinit var addressView: TextView
    private lateinit var descView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_overview_screen)
        database = FirebaseDatabase.getInstance().reference

        titleView = findViewById(R.id.tripOverviewTitleLabel)
        weightView = findViewById(R.id.tripOverviewWeightLabel)
        itemCountView = findViewById(R.id.tripOverviewNumItemsLabel)
        dateView = findViewById(R.id.tripOverviewDateLabel)
        addressView = findViewById(R.id.tripOverviewAddressLabel)
        descView = findViewById(R.id.tripOverviewDescriptionLabel)


        val tripID = intent.getStringExtra("trip")
        if (tripID != null) {
            database.child("Trips").child(tripID).addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val trip = snapshot.getValue(Trip::class.java)


                        titleView.text = trip!!.title
                        weightView.text = "TODO"
                        itemCountView.text = trip!!.items?.size.toString()
                        dateView.text = trip!!.date
                        addressView.text = trip!!.location
                        descView.text = trip!!.description
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
                }

            })
        }

        //Trip Edit button
        findViewById<FloatingActionButton>(R.id.tripOverviewEditButton).setOnClickListener {
            val intent = Intent(this, TripListAdd::class.java)
            //TODO: Add trip ID to intent and modify triplistadd to support editing
            startActivity(intent)
        }

        //Trip Delete button
        findViewById<FloatingActionButton>(R.id.tripOverviewDeleteButton).setOnClickListener {
            //val intent = Intent(this, TripListAdd::class.java)
            //startActivity(intent)
            //TODO: Send to the delete confirmation page
        }

        //Trip Item Add button
        findViewById<FloatingActionButton>(R.id.tripOverviewAddItemButton).setOnClickListener {
            //TODO: Pull up item list and pick item
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