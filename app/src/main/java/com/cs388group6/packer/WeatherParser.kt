package com.cs388group6.packer

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.json.JSONArray
import org.json.JSONObject
import java.util.Objects

//Used for displaying the location grabbed from the API as the location
//entered by the user could cause the API to grab a wrong location
@Keep
@Serializable
data class Area(
    @SerialName("location")
    val locationInfo: Location
)

@Keep
@Serializable
data class Location(
    @SerialName("name")
    val city: String?,
    @SerialName("region")
    val region: String?,
    @SerialName("country")
    val country: String?
)

//The weather data is parsed using this
@Keep
@Serializable
data class Weather(
    @SerialName("forecast")
    val forecast: Forecast
)

@Keep
@Serializable
data class Forecast(
    @SerialName("forecastday")
    val forecastDays: List<ForecastDay>?
)

@Keep
@Serializable
data class ForecastDay(
    @SerialName("date")
    val date: String?,
    @SerialName("day")
    val day: Day?
)

@Keep
@Serializable
data class Day(
    @SerialName("maxtemp_f")
    val maxTempF: String?,
    @SerialName("mintemp_f")
    val minTempF: String?,
    @SerialName("avgtemp_f")
    val avgTempF: String?,
    @SerialName("condition")
    val condition: Condition?
)

@Keep
@Serializable
data class Condition(
    @SerialName("text")
    val conditionText: String?,
    @SerialName("icon")
    val iconUrl: String?
)
