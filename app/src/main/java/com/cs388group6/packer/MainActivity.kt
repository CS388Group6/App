package com.cs388group6.packer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        auth = Firebase.auth

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            var email = findViewById<TextView>(R.id.email).text
            var password = findViewById<TextView>(R.id.password).text

            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("MAINACTIVITY","USER LOGGED IN")
                        val intent = Intent(this, TripList::class.java)
                        startActivity(intent)

                    }
                    else {
                        Log.d("MAINACTIVITY", "user fucked")
                    }
                }
        }

        findViewById<Button>(R.id.signUpScreen).setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
//            setContentView(R.layout.trips_list_screen)
        }

        // Bottom Navigation Selection
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_login -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.nav_logout -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, TripList::class.java))
                }
                R.id.nav_list -> {
                    startActivity(Intent(this, MyItemList::class.java))
                }
            }
            true
        }
        //Set default selection
//        bottomNavigationView.selectedItemId = R.id.nav_login


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