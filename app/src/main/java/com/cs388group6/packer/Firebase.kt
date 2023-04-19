package com.cs388group6.packer

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class User(val name: String? = null, val email: String? = null)

fun writeUser(name: String, email: String, reference: DatabaseReference) {
    val user = User(name, email)

    reference
}