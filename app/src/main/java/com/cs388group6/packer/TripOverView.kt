package com.cs388group6.packer

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
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
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class TripOverView : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var titleView: TextView
    private lateinit var weightView: TextView
    private lateinit var itemCountView: TextView
    private lateinit var dateView: TextView
    private lateinit var addressView: TextView
    private lateinit var descView: TextView
    private lateinit var editButton: FloatingActionButton
    private lateinit var deleteButton: FloatingActionButton
    private lateinit var addItemButton: FloatingActionButton
    private lateinit var weatherLocationView: TextView
    private lateinit var weatherConditionView: TextView
    private lateinit var weatherHighView: TextView
    private lateinit var weatherLowView: TextView
    private lateinit var weatherAvgView: TextView
    private lateinit var weatherIconView: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var userID: String
    private lateinit var items: ArrayList<Item>

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
        editButton = findViewById(R.id.tripOverviewEditButton)
        deleteButton = findViewById(R.id.tripOverviewDeleteButton)
        addItemButton = findViewById(R.id.tripOverviewAddItemButton)

        weatherIconView = findViewById(R.id.tripOverviewWeatherIcon)
        weatherLocationView = findViewById(R.id.tripOverviewWeatherLocation)
        weatherConditionView = findViewById(R.id.tripOverviewConditionLabel)
        weatherHighView = findViewById(R.id.tripOverviewHighTemperatureLabel)
        weatherLowView = findViewById(R.id.tripOverviewLowTemperatureLabel)
        weatherAvgView = findViewById(R.id.tripOverviewAvgTemperatureLabel)



        auth = Firebase.auth
        userID = auth.currentUser?.uid ?: ""

        val itemIDs = mutableListOf<String>()
        val recyclerView = findViewById<RecyclerView>(R.id.tripOverviewItemsRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        items = ArrayList()


        //Get Trip Information and fill Fields
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

                        if(trip!!.weather!="") {
                            val gson = Gson()
                            val weatherData = gson.fromJson(trip!!.weather, WeatherItem::class.java)
                            weatherAvgView.text = "Avg. Temperature: " + weatherData?.avgtemp_f + "° F"
                            weatherHighView.text = "High Temperature: " + weatherData?.maxtemp_f + "° F"
                            weatherLowView.text = "Low Temperature: " + weatherData?.mintemp_f + "° F"
                            weatherLocationView.text = weatherData?.city + ", " + weatherData?.region + ", " + weatherData?.country
                            weatherConditionView.text = weatherData?.condition
                            Glide.with(this@TripOverView)
                                .load(weatherData?.image)
                                .centerInside()
                                .into(weatherIconView)
                        }

                        for (itemID in snapshot.child("items").children){
                            itemIDs.add(itemID.value.toString())
                        }
                        loadItems(itemIDs, recyclerView)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w( "loadPost:onCancelled", error.toException())
                }

            })
        }

        //Edit Trip Button
        editButton.setOnClickListener {
            var intent = Intent(this, TripListAdd::class.java)
            intent.putExtra("trip", tripID)
            startActivity(intent)
        }


        //Delete Trip Button
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("This action Cannot be undone. Are you Sure?")
                .setPositiveButton("Confirm", DialogInterface.OnClickListener{ _, _ ->
                    val rem = database.child("Trips").child(tripID.toString()).removeValue()
                    rem.addOnSuccessListener {
                        finish()
                    }.addOnFailureListener{error ->
                        Log.w("DatabaseError", error)
                    }
                })
                .setNegativeButton("Cancel"
                ) { dialog, _ ->
                    dialog.cancel()
                }
                .show();
        }

        addItemButton.setOnClickListener {
            var intent = Intent(this, AddItemToTrip::class.java)
            intent.putExtra("trip", tripID)
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

    //THIS DOES NOT WORK
    private fun loadItems(itemIDs: MutableList<String>, recyclerView: RecyclerView){
        if (itemIDs.isNotEmpty()){

            database.child("items").orderByChild("userid").equalTo(userID).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        Log.d("snapshot", snapshot.toString())
                        for(item in itemIDs){
                            val itemObj = snapshot.child(item).getValue(Item::class.java)
                            Log.d("Item", itemObj.toString())
                            if (itemObj != null){
                                items.add(itemObj)
                            }
                        }
                        val adapter = MyItemListAdapter(items)
                        recyclerView.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w( "loadPost:onCancelled", error.toException())
                }
            })

        }
    }
}