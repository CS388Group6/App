package com.cs388group6.packer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        Log.d("MAINACTIVITY", currentUser.toString())

        if (currentUser != null) {
            Log.d("MAINACTIVITY", "USER IS LOGGED IN")
        }
        else {
            Log.d("MAINACTIVITY", "NO USER")
        }
    }
}