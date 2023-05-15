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
import java.text.SimpleDateFormat
import java.util.Locale

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
    private lateinit var tripID: String
    private val formatDate = SimpleDateFormat("MMM dd YYYY", Locale.US)
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
        tripID = intent.getStringExtra("trip").toString()
        if (tripID != null) {
            database.child("Trips").child(tripID).addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val trip = snapshot.getValue(Trip::class.java)


                        titleView.text = trip!!.title
                        weightView.text = "0 Kilograms/ \n0 Pounds"
                        val itemCount = (trip!!.items?.size?.minus(1))
                        itemCountView.text = itemCount.toString() + " Items"
                        dateView.text = formatDate.format(trip.date)
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

                        itemIDs.clear()
                        for (itemID in snapshot.child("items").children){
                            val idCheck = itemID.value.toString()
                            if (idCheck != ""){
                                itemIDs.add(idCheck)
                            }
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

    //works until you add a new item for some reason
    private fun loadItems(itemIDs: MutableList<String>, recyclerView: RecyclerView){
        items.clear()
        val adapter = TripOverViewAdapter(items, tripID)
        recyclerView.adapter = adapter
        if (itemIDs.isNotEmpty()){
            val itemSnap = database.child("items").orderByChild("userID").equalTo(userID).get()
            itemSnap.addOnCompleteListener {
                    if (it.result.exists()){
                        for(item in itemIDs){
                            val itemObj = it.result.child(item).getValue(Item::class.java)
                            if (itemObj != null){
                                items.add(itemObj)
                            }
                        }
                    }
                setWeight()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setWeight(){
        var weightTotal: Float = 0F
        for(item in items){
            val weight = item.weight.toString()
            var weightDetailed = weight.split(" ")
            Log.d("Weight", weightDetailed[1])
            when (weightDetailed[1]) {
                "Grams" -> {
                    weightTotal += weightDetailed[0].toFloat() / 1000F
                }
                "Kilograms" -> {
                    weightTotal += weightDetailed[0].toFloat()
                }
                "Pounds" -> {
                    weightTotal += weightDetailed[0].toFloat() / 2.205F
                }
                "Ounces" -> {
                    weightTotal += weightDetailed[0].toFloat() / 35.274F
                }
            }
        }
        val inPounds = weightTotal * 2.205F
        weightView.text = weightTotal.toString() + " Kilograms/ \n" + inPounds.toString() + " Pounds"
    }
}