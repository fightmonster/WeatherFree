package com.fightmonster.weatherfree.data

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    @SerializedName("result")
    val result: GeocodingResult?
)

data class GeocodingResult(
    @SerializedName("addressMatches")
    val addressMatches: List<AddressMatch>?
)

data class AddressMatch(
    @SerializedName("coordinates")
    val coordinates: Coordinates?,
    @SerializedName("matchedAddress")
    val matchedAddress: String?
)

data class Coordinates(
    @SerializedName("x")
    val x: Double?, // Longitude
    @SerializedName("y")
    val y: Double?  // Latitude
)

fun AddressMatch.toLocation(): Location? {
    return Location(
        latitude = coordinates?.y ?: return null,
        longitude = coordinates?.x ?: return null,
        name = matchedAddress ?: "Unknown Location"
    )
}

data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String
)

// OpenStreetMap Nominatim API models
// Nominatim returns a direct array, not wrapped in an object
// Use List<NominatimResult> directly in the API interface

data class NominatimResult(
    val lat: String?,
    val lon: String?,
    val display_name: String?,
    val address: NominatimAddress?
)

data class NominatimAddress(
    val city: String?,
    val state: String?,
    val postcode: String?,
    val country: String?
)

fun NominatimResult.toLocation(): Location? {
    val lat = lat ?: return null
    val lon = lon ?: return null

    val latitude = lat.toDoubleOrNull() ?: return null
    val longitude = lon.toDoubleOrNull() ?: return null

    val name = display_name ?: "Unknown Location"

    return Location(
        latitude = latitude,
        longitude = longitude,
        name = name
    )
}
