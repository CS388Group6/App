package com.cs388group6.packer

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TripListAdd : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: String
    private lateinit var tripNameInput: EditText
    private lateinit var tripLocationInput: EditText
    private lateinit var tripDateInput: Button
    private lateinit var tripDescInput: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private val formatDate = SimpleDateFormat("MMM dd YYYY", Locale.US)
    private lateinit var dateTime : Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_add_screen)

        database = FirebaseDatabase.getInstance().reference
        auth = Firebase.auth
        user = auth.currentUser?.uid ?: ""
        tripNameInput = findViewById(R.id.editTripNameInput)
        tripLocationInput = findViewById(R.id.editTripLocationView)
        tripDateInput = findViewById(R.id.editTripDateInput)
        tripDescInput = findViewById(R.id.editTripDescriptionInput)
        saveButton = findViewById(R.id.editTripSaveButton)
        cancelButton = findViewById(R.id.editTripCancelButton)

        tripDateInput.setOnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, android.R.style.ThemeOverlay_Material_Dialog, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, i)
                selectedDate.set(Calendar.MONTH, i2)
                selectedDate.set(Calendar.DAY_OF_MONTH, i3)
                dateTime = selectedDate.time
                val date = formatDate.format(selectedDate.time)
                tripDateInput.text = date

            }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        var key = intent.getStringExtra("trip")

        if (key != null && key != ""){
            database.child("Trips").child(key).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val trip = snapshot.getValue(Trip::class.java)

                        tripNameInput.setText(trip!!.title)
                        tripLocationInput.setText(trip!!.location)
                        tripDescInput.setText(trip!!.description)
                        dateTime = trip!!.date!!
                        tripDateInput.setText(formatDate.format(dateTime))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w( "loadPost:onCancelled", error.toException())
                }

            })
        }

        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener{
            if (tripNameInput.text.isBlank()){
                Toast.makeText(this, "Please Enter A Trip Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tripLocationInput.text.isBlank()){
                Toast.makeText(this, "Please Enter A Trip Location", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tripDateInput.text.isBlank()){
                Toast.makeText(this, "Please Enter A Trip Date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tripDescInput.text.isBlank()){
                Toast.makeText(this, "Please Enter A Trip Description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //TODO: Date Input Validation
            var new = false
            if (key == "") {
                key = database.child("Trips").push().key.toString()
                new = true
            }
            val trip = Trip(title = tripNameInput.text.toString(),
                location = tripLocationInput.text.toString(),
                date = dateTime,
                description = tripDescInput.text.toString(),
                weather = "",
                userID = user,
                items = mutableListOf(String()),
                tripID = key
            )
            if (new){
                database.child("Trips").child(key!!).setValue(trip)
            }
            else{
                val itemget = database.child("Trips").child(trip.tripID.toString()).child("items").get()
                itemget.addOnCompleteListener {
                    var tItems = mutableListOf<String>()
                    for (item in it.result.children){
                        val itemData = item.getValue<String>().toString()
                        tItems.add(itemData)
                    }
                    trip.items = tItems
                    database.child("Trips").child(key!!).setValue(trip)
                }
            }
            finish()
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