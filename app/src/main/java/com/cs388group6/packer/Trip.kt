package com.cs388group6.packer

import java.io.Serializable
class Trip (
    var title: String = "Rocky Mountain National Park",
    var bags: String = "2 bags",
    var weight: String = "21 kg",
    var weather: String?,
    var numItems: String = "51 items",
    var date: String = "June 20, 2023"
) : Serializable
