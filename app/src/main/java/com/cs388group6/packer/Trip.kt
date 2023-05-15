package com.cs388group6.packer

import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.io.Serializable
import java.util.Date

data class Trip (
    var tripID: String? = null,
    var title: String? = null,
    var weather: String? = null,
    var date: Date? = null,
    var location: String? = null,
    var description: String? = null,
    var userID: String? = null,
    var items: MutableList<String>? = null
) : Serializable
