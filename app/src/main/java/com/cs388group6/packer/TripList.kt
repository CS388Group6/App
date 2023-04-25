package com.cs388group6.packer

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException
import org.json.JSONObject

private const val SEARCH_API_KEY = BuildConfig.API_KEY

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

class TripList: AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var adapter: TripListAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var user: String
    private val trips = mutableListOf<Trip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trips_list_screen)
        database = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth
        user = auth.currentUser?.uid ?: ""
        val recyclerView = findViewById<RecyclerView>(R.id.tripsListRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val client = AsyncHttpClient()


        adapter = TripListAdapter(trips)
        recyclerView.adapter = adapter

        // getting data from firebase and putting it in recycler view
        database.child("Trips").orderByChild("userID").equalTo(user).addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                trips.clear()
                if (snapshot.exists()){
                    for (trip in snapshot.children){
                        val tripdata = trip.getValue(Trip::class.java)
                        Log.w("TripList", "Location: ${tripdata?.location.toString()}")
                        val url = "https://api.weatherapi.com/v1/forecast.json?key=${SEARCH_API_KEY}&q=${tripdata!!.location.toString()}&days=3&aqi=no&alerts=no"
                        client.get(url, object : JsonHttpResponseHandler(){
                            override fun onFailure(
                                statusCode: Int,
                                headers: Headers?,
                                response: String?,
                                throwable: Throwable?
                            ) {
                                Log.e("TripList", "API Call Failed: $response")
                            }

                            override fun onSuccess(
                                statusCode: Int,
                                headers: Headers?,
                                json: JSON?
                            ) {
                                Log.i("TripList", "API Call Succeeded: $json")
                                try{
                                    val locationJson = createJson().decodeFromString(
                                        Area.serializer(),
                                        json?.jsonObject.toString()
                                    )

                                    val weatherJson = createJson().decodeFromString(
                                        Weather.serializer(),
                                        json?.jsonObject.toString()
                                    )

                                    var validWeather = 0
                                    val weatherData = JSONObject()

                                    //Store the location data from the API so user can know if the location
                                    //entered matches the location in the weather API
                                    weatherData.put("city", locationJson.locationInfo.city)
                                    weatherData.put("region", locationJson.locationInfo.region)
                                    weatherData.put("country", locationJson.locationInfo.country)

                                    //Checks if any days pulled from the forecast match the dates in the api call
                                    for (day in weatherJson.forecast.forecastDays!!){
                                        if(day.date != tripdata.date){
                                            continue
                                        }
                                        else{
                                            validWeather = 1
                                            weatherData.put("maxtemp_f", day.day?.maxTempF)
                                            weatherData.put("mintemp_f", day.day?.minTempF)
                                            weatherData.put("avgtemp_f", day.day?.avgTempF)
                                            weatherData.put("condition", day.day?.condition?.conditionText)
                                            weatherData.put("image", "https:${day.day?.condition?.iconUrl}")
                                            break
                                        }
                                    }

                                    if(validWeather == 1){
                                        database.child("Trips").child(tripdata.tripID!!).child("weather").setValue(weatherData.toString())
                                        Log.w("TripList", "Weather Data Successfully Updated!")
                                    }

                                    else{
                                        //database.child("Trips").child(tripdata.tripID!!).child("weather").setValue("")
                                        Log.w("TripList", "No weather data available for inputted date")
                                    }



                                    //Log.w("TripList", "Weather String: ${weather.image}")
                                    /*Log.w("TripList", "Weather JSON: ${weatherJson.forecast.forecastDays?.get(0)?.day}")
                                    Log.w("TripList", "Trip Data: ${tripdata.weather}")*/
                                }

                                catch(e:JSONException){
                                    Log.e("TripList", "Exception: $e")
                                }
                            }
                        })
                        trips.add(tripdata!!)
                    }

                    //click on trip handling
                    adapter.setOnItemCLickListener(object : TripListAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@TripList, TripOverView::class.java)
                            intent.putExtra("trip", trips[position].tripID)
                            startActivity(intent)
                        }
                    })
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w( "loadPost:onCancelled", error.toException())
            }

        })

        //Add trip button
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val intent = Intent(this, TripListAdd::class.java)
            intent.putExtra("trip", "")
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



