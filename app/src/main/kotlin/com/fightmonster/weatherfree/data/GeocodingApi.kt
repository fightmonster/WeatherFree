package com.fightmonster.weatherfree.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * U.S. Census Geocoding API
 * Best for: Full U.S. street addresses
 * Documentation: https://geocoding.geo.census.gov/geocoder/Geocoding_Services_API.html
 * Note: Only geocodes addresses within United States, Puerto Rico, and U.S. Island Areas
 */
interface CensusGeocodingApi {
    /**
     * Search by one-line address using U.S. Census Geocoding API
     * Supports: Full U.S. street addresses
     * Example: "4600 Silver Hill Rd, Washington, DC 20233"
     */
    @GET("locations/onelineaddress")
    suspend fun searchByAddress(
        @Query("address") address: String,
        @Query("benchmark") benchmark: String = "4",
        @Query("format") format: String = "json"
    ): Response<GeocodingResponse>

    /**
     * Search by address components using U.S. Census Geocoding API
     * At minimum requires: street AND zip OR street, city, AND state
     */
    @GET("locations/address")
    suspend fun searchByComponents(
        @Query("street") street: String,
        @Query("city") city: String? = null,
        @Query("state") state: String? = null,
        @Query("zip") zip: String? = null,
        @Query("benchmark") benchmark: String = "4",
        @Query("format") format: String = "json"
    ): Response<GeocodingResponse>
}

/**
 * OpenStreetMap Nominatim API
 * Best for: City names, ZIP codes, international addresses, partial addresses
 * Documentation: https://nominatim.org/release-docs/latest/api/Search/
 * Note: Requires User-Agent header and obeys usage policy
 * Note: Returns a direct JSON array, not wrapped in an object
 */
interface NominatimGeocodingApi {
    /**
     * Search by query using OpenStreetMap Nominatim API
     * Supports: city names, ZIP codes, full addresses (worldwide)
     * Examples: "Los Angeles, CA", "10001", "1600 Pennsylvania Ave NW, Washington, DC"
     */
    @GET("search")
    suspend fun searchByQuery(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 1
    ): Response<List<NominatimResult>>
}
