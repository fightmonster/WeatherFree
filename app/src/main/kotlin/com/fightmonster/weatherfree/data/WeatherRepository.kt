package com.fightmonster.weatherfree.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {

    private val weatherApi: WeatherApi

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weather.gov/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    suspend fun getForecast(lat: Double, lon: Double): Result<List<Period>> {
        return try {
            // First, get the point to find forecast URL
            val pointResponse = weatherApi.getPoint(lat, lon)
            if (!pointResponse.isSuccessful || pointResponse.body() == null) {
                return Result.failure(Exception("Failed to get location data"))
            }

            val forecastUrl = pointResponse.body()!!.properties.forecast
                ?: return Result.failure(Exception("Forecast URL not available"))

            // Then get the actual forecast
            val forecastResponse = weatherApi.getForecast(forecastUrl)
            if (!forecastResponse.isSuccessful || forecastResponse.body() == null) {
                return Result.failure(Exception("Failed to get forecast data"))
            }

            val periods = forecastResponse.body()!!.properties.periods
            Result.success(periods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
