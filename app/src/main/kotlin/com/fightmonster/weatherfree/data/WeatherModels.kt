package com.fightmonster.weatherfree.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("properties") val properties: Properties
)

data class Properties(
    @SerializedName("periods") val periods: List<Period>
)

data class Period(
    @SerializedName("number") val number: Int,
    @SerializedName("name") val name: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("isDaytime") val isDaytime: Boolean,
    @SerializedName("temperature") val temperature: Int,
    @SerializedName("temperatureUnit") val temperatureUnit: String,
    @SerializedName("windSpeed") val windSpeed: String,
    @SerializedName("windDirection") val windDirection: String,
    @SerializedName("icon") val icon: String?,
    @SerializedName("shortForecast") val shortForecast: String,
    @SerializedName("detailedForecast") val detailedForecast: String,
    @SerializedName("relativeHumidity") val relativeHumidity: ValueInt?
)

data class ValueInt(
    @SerializedName("value") val value: Int?
)

data class PointResponse(
    @SerializedName("properties") val properties: PointProperties
)

data class PointProperties(
    @SerializedName("forecast") val forecast: String?,
    @SerializedName("forecastHourly") val forecastHourly: String?,
    @SerializedName("forecastGridData") val forecastGridData: String?
)

data class LocationData(
    val name: String,
    val lat: Double,
    val lon: Double
)
