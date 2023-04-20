package com.cs388group6.packer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tapadoo.alerter.Alerter

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        auth = Firebase.auth

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val email = findViewById<TextView>(R.id.email).text
            val password = findViewById<TextView>(R.id.password).text

            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, TripList::class.java)
                        startActivity(intent)

                    }
                    else {
                        Log.w("SignUp", "createUserFailure: ", it.exception )
                        Alerter.create(this@MainActivity)
                            .setTitle(it.exception?.message.toString())
                            .setBackgroundColorRes(R.color.red)
                            .setIcon(R.drawable.icon_clear)
                            .show()
//                        Toast.makeText(baseContext, it.exception?.message ,
//                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
        findViewById<Button>(R.id.signUpScreen).setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
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
    }
}