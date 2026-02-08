package com.fightmonster.weatherfree.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {

    private val weatherApi: WeatherApi
    private val censusApi: CensusGeocodingApi
    private val nominatimApi: NominatimGeocodingApi

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Add User-Agent for Nominatim (required by their usage policy)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("User-Agent", "WeatherFree App (geocoding)")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        val weatherRetrofit = Retrofit.Builder()
            .baseUrl("https://api.weather.gov/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = weatherRetrofit.create(WeatherApi::class.java)

        // U.S. Census Geocoding API - for full U.S. street addresses
        val censusRetrofit = Retrofit.Builder()
            .baseUrl("https://geocoding.geo.census.gov/geocoder/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        censusApi = censusRetrofit.create(CensusGeocodingApi::class.java)

        // OpenStreetMap Nominatim API - for cities, ZIP codes, international addresses
        val nominatimRetrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        nominatimApi = nominatimRetrofit.create(NominatimGeocodingApi::class.java)
    }

    suspend fun getForecast(lat: Double, lon: Double): Result<List<Period>> {
        return try {
            // First, get the point to find forecast URL
            val pointResponse = weatherApi.getPoint(lat, lon)
            if (!pointResponse.isSuccessful) {
                return Result.failure(Exception("Failed to get location data: HTTP ${pointResponse.code()}"))
            }

            val pointBody = pointResponse.body()
                ?: return Result.failure(Exception("Failed to get location data: null response"))

            val forecastUrl = pointBody.properties.forecast
                ?: return Result.failure(Exception("Forecast URL not available for this location"))

            // Then get the actual forecast
            val forecastResponse = weatherApi.getForecast(forecastUrl)
            if (!forecastResponse.isSuccessful) {
                return Result.failure(Exception("Failed to get forecast data: HTTP ${forecastResponse.code()}"))
            }

            val forecastBody = forecastResponse.body()
                ?: return Result.failure(Exception("Failed to get forecast data: null response"))

            val periods = forecastBody.properties.periods
            Result.success(periods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Unified search method for all location types
     * Supports: addresses, city names, ZIP codes (U.S. and international)
     *
     * Strategy:
     * 1. First tries U.S. Census API (best for full U.S. street addresses)
     * 2. Falls back to Nominatim (for cities, ZIP codes, international addresses)
     *
     * @param query Search query - can be address, city, or ZIP code
     * @return Location with coordinates and name
     */
    suspend fun search(query: String): Result<Location> {
        // Try Census API first (for U.S. street addresses)
        val censusResult = try {
            val response = censusApi.searchByAddress(query)
            if (response.isSuccessful && response.body() != null) {
                val addressMatches = response.body()!!.result?.addressMatches
                if (!addressMatches.isNullOrEmpty()) {
                    val location = addressMatches[0].toLocation()
                    if (location != null) {
                        return Result.success(location)
                    }
                }
            }
            null
        } catch (e: Exception) {
            null
        }

        // Fall back to Nominatim (for cities, ZIP codes, international)
        // For US ZIP codes, add country suffix to get better results
        val nominatimQuery = if (query.matches(Regex("^\\d{5}(-\\d{4})?$"))) {
            "$query, USA"
        } else {
            query
        }
        return searchWithNominatim(nominatimQuery)
    }

    /**
     * Search using OpenStreetMap Nominatim API
     * Supports: city names, ZIP codes, international addresses
     */
    private suspend fun searchWithNominatim(query: String): Result<Location> {
        return try {
            val response = nominatimApi.searchByQuery(query)
            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception("Failed to search location"))
            }

            // Nominatim returns a direct array, not wrapped in an object
            val results = response.body()!!
            if (results.isEmpty()) {
                return Result.failure(Exception("No results found for: $query"))
            }

            val location = results[0].toLocation()
                ?: return Result.failure(Exception("Failed to parse location"))
            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Legacy methods for backward compatibility
    @Deprecated("Use search() instead", ReplaceWith("search(query)"))
    suspend fun searchByAddress(address: String): Result<Location> = search(address)

    @Deprecated("Use search() instead", ReplaceWith("search(query)"))
    suspend fun searchByZipCode(zipCode: String): Result<Location> = search(zipCode)
}
