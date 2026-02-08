package com.fightmonster.weatherfree.data

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

data class USState(
    val name: String,
    val code: String
)

data class USCity(
    val name: String,
    val state: String,
    val zip: String,
    val latitude: Double,
    val longitude: Double
)

private data class USLocationsData(
    val states: List<USState>,
    val cities: Map<String, List<USCity>>
)

object USLocations {
    private var data: USLocationsData? = null
    private val gson = Gson()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    val USStates: List<USState>
        get() = data?.states ?: emptyList()

    val USCities: Map<String, List<USCity>>
        get() = data?.cities ?: emptyMap()

    /**
     * Load city data from assets JSON file
     * Should be called once during app initialization
     */
    suspend fun load(context: Context) = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("us_cities.json").bufferedReader().use { it.readText() }
            data = gson.fromJson(json, USLocationsData::class.java)
            _isLoaded.value = true
        } catch (e: Exception) {
            // If JSON loading fails, use hardcoded fallback
            loadFallbackData()
            _isLoaded.value = true
        }
    }

    /**
     * Fallback hardcoded data in case JSON loading fails
     */
    private fun loadFallbackData() {
        data = USLocationsData(
            states = listOf(
                USState("Alabama", "AL"),
                USState("Alaska", "AK"),
                USState("Arizona", "AZ"),
                USState("Arkansas", "AR"),
                USState("California", "CA"),
                USState("Colorado", "CO"),
                USState("Connecticut", "CT"),
                USState("Delaware", "DE"),
                USState("Florida", "FL"),
                USState("Georgia", "GA"),
                USState("Hawaii", "HI"),
                USState("Idaho", "ID"),
                USState("Illinois", "IL"),
                USState("Indiana", "IN"),
                USState("Iowa", "IA"),
                USState("Kansas", "KS"),
                USState("Kentucky", "KY"),
                USState("Louisiana", "LA"),
                USState("Maine", "ME"),
                USState("Maryland", "MD"),
                USState("Massachusetts", "MA"),
                USState("Michigan", "MI"),
                USState("Minnesota", "MN"),
                USState("Mississippi", "MS"),
                USState("Missouri", "MO"),
                USState("Montana", "MT"),
                USState("Nebraska", "NE"),
                USState("Nevada", "NV"),
                USState("New Hampshire", "NH"),
                USState("New Jersey", "NJ"),
                USState("New Mexico", "NM"),
                USState("New York", "NY"),
                USState("North Carolina", "NC"),
                USState("North Dakota", "ND"),
                USState("Ohio", "OH"),
                USState("Oklahoma", "OK"),
                USState("Oregon", "OR"),
                USState("Pennsylvania", "PA"),
                USState("Rhode Island", "RI"),
                USState("South Carolina", "SC"),
                USState("South Dakota", "SD"),
                USState("Tennessee", "TN"),
                USState("Texas", "TX"),
                USState("Utah", "UT"),
                USState("Vermont", "VT"),
                USState("Virginia", "VA"),
                USState("Washington", "WA"),
                USState("West Virginia", "WV"),
                USState("Wisconsin", "WI"),
                USState("Wyoming", "WY"),
                USState("District of Columbia", "DC")
            ),
            cities = mapOf(
                "CA" to listOf(
                    USCity("Los Angeles", "CA", "90210", 34.0522, -118.2437),
                    USCity("San Francisco", "CA", "94102", 37.7749, -122.4194),
                    USCity("San Diego", "CA", "92101", 32.7157, -117.1611)
                ),
                "NY" to listOf(
                    USCity("New York", "NY", "10001", 40.7128, -74.006),
                    USCity("Buffalo", "NY", "14201", 42.8864, -78.8784)
                )
                // Add more fallback cities as needed
            )
        )
    }
}
