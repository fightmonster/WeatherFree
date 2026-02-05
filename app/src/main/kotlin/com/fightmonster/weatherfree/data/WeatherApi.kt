package com.fightmonster.weatherfree.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface WeatherApi {
    @GET("points/{lat},{lon}")
    suspend fun getPoint(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double
    ): Response<PointResponse>

    @GET
    suspend fun getForecast(
        @Url url: String
    ): Response<WeatherResponse>
}
