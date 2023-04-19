package com.cs388group6.packer

import java.io.Serializable
data class Trip (
    var title: String? = null,
    var weather: String? = null,
    var date: String? = null,
    var location: String? = null,
    var description: String? = null,
    var userID: String? = null,
    var items: MutableList<String>? = null
) : Serializable
