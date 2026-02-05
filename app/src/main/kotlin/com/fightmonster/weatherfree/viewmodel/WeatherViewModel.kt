package com.fightmonster.weatherfree.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightmonster.weatherfree.data.Period
import com.fightmonster.weatherfree.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = false,
    val currentWeather: Period? = null,
    val forecast: List<Period> = emptyList(),
    val error: String? = null
)

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val selectedLocation: StateFlow<Pair<Double, Double>?> = _selectedLocation.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchLocation(query: String) {
        // For demo, use a few hardcoded US cities with coordinates
        // In production, integrate with a geocoding API
        val location = when (query.lowercase()) {
            "new york", "nyc", "new york city" -> 40.7128 to -74.0060
            "los angeles", "la" -> 34.0522 to -118.2437
            "chicago" -> 41.8781 to -87.6298
            "houston" -> 29.7604 to -95.3698
            "phoenix" -> 33.4484 to -112.0740
            "philadelphia" -> 39.9526 to -75.1652
            "san antonio" -> 29.4241 to -98.4936
            "san diego" -> 32.7157 to -117.1611
            "dallas" -> 32.7767 to -96.7970
            "san jose" -> 37.3382 to -121.8863
            // ZIP codes for major cities
            "10001" -> 40.7489 to -73.9680  // NYC
            "90210" -> 34.0736 to -118.4004  // Beverly Hills
            "60601" -> 41.8827 to -87.6233  // Chicago
            "77001" -> 29.7604 to -95.3698  // Houston
            else -> return
        }

        _selectedLocation.value = location
        fetchWeather(location.first, location.second)
    }

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getForecast(lat, lon)
                .onSuccess { periods ->
                    _uiState.value = WeatherUiState(
                        isLoading = false,
                        currentWeather = periods.firstOrNull(),
                        forecast = periods
                    )
                }
                .onFailure { e ->
                    _uiState.value = WeatherUiState(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
