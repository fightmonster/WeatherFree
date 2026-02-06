package com.fightmonster.weatherfree.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightmonster.weatherfree.data.Period
import com.fightmonster.weatherfree.data.USCity
import com.fightmonster.weatherfree.data.USState
import com.fightmonster.weatherfree.data.USCities
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

    private val _selectedState = MutableStateFlow<USState?>(null)
    val selectedState: StateFlow<USState?> = _selectedState.asStateFlow()

    private val _selectedCity = MutableStateFlow<String?>(null)
    val selectedCity: StateFlow<String?> = _selectedCity.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val selectedLocation: StateFlow<Pair<Double, Double>?> = _selectedLocation.asStateFlow()

    fun onStateSelected(state: USState) {
        _selectedState.value = state
        _selectedCity.value = null
        _searchQuery.value = ""
    }

    fun onCitySelected(cityName: String) {
        _selectedCity.value = cityName
        _selectedState.value = null

        // Find the city in the data
        val city = USCities.values.flatten().find { it.name == cityName }
        if (city != null) {
            _selectedLocation.value = Pair(city!!.latitude, city!!.longitude)
            fetchWeather(city!!.latitude, city!!.longitude)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchLocation(query: String) {
        // Search through all cities by name, state, or ZIP code
        val city = USCities.values.flatten().find { city ->
            city.name.equals(query, ignoreCase = true) ||
            city.zip.equals(query, ignoreCase = true) ||
            city.state.equals(query, ignoreCase = true) ||
            query.lowercase().let { lower ->
                city.name.contains(lower) ||
                "${city.state}, ${city.name}".contains(lower) ||
                "${city.name}, ${city.state}".contains(lower)
            }
        }

        if (city != null) {
            _selectedCity.value = city!!.name
            _selectedState.value = USStates.find { it.code == city!!.state }
            _selectedLocation.value = Pair(city!!.latitude, city!!.longitude)
            fetchWeather(city!!.latitude, city!!.longitude)
        } else {
            _uiState.value = WeatherUiState(
                isLoading = false,
                error = "City not found. Try selecting from the dropdown or enter a ZIP code."
            )
        }
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
