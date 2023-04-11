package com.cs388group6.packer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

            auth.signInWithEmailAndPassword(email as String, password as String)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("MAINACTIVITY","USER LOGGED IN")
                    }
                    else {
                        Log.d("MAINACTIVITY", "user fucked")
                    }
                }
        }

        findViewById<Button>(R.id.signUpScreen).setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
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