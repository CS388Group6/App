package com.cs388group6.packer

data class WeatherItem(
    val city: String,
    val region: String,
    val country: String,
    val maxtemp_f: String,
    val mintemp_f: String,
    val avgtemp_f: String,
    val condition: String,
    val image: String
)