package com.cs388group6.packer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        auth = Firebase.auth

        findViewById<Button>(R.id.signUpButton).setOnClickListener {
            var email = findViewById<EditText>(R.id.signUpEmail).text
            var password = findViewById<EditText>(R.id.signUpPassword).text
            var confPassword = findViewById<EditText>(R.id.confirmPassword).text

            if (password.toString() != confPassword.toString()){
                Toast.makeText(baseContext, "Passwords don't match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else if (password.toString().isEmpty() || email.toString().isEmpty()){
                Toast.makeText(baseContext, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("SignUp", "createUserWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                    else {
                        Log.w("SignUp", "createUserFailure: ", task.exception )
                        Toast.makeText(baseContext, task.exception?.message ,
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}