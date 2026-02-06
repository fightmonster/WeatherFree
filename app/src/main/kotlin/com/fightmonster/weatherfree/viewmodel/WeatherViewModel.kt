package com.fightmonster.weatherfree.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightmonster.weatherfree.data.Period
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

    fun onStateSelected(state: USState) {
        _selectedState.value = state
        _selectedCity.value = null
    }

    fun onCitySelected(city: String?) {
        _selectedCity.value = city
        city?.let {
            val cityData = USCities.values.flatten().find { it.name == city }
            if (cityData != null) {
                fetchWeather(cityData!!.latitude, cityData!!.longitude)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearError() {
        _uiState.value = WeatherUiState()
    }

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState(
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
}
