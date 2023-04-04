package com.cs388group6.packer

import android.app.Application

class PackerApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this)}
}