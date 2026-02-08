package com.fightmonster.weatherfree.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightmonster.weatherfree.data.Period
import com.fightmonster.weatherfree.data.USState
import com.fightmonster.weatherfree.data.USLocations
import com.fightmonster.weatherfree.data.WeatherRepository
import com.fightmonster.weatherfree.data.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SearchMode {
    STATE_CITY,  // 选择州和城市
    SEARCH       // 智能搜索（地址、城市名、ZIP码）
}

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

    private val _searchMode = MutableStateFlow(SearchMode.STATE_CITY)
    val searchMode: StateFlow<SearchMode> = _searchMode.asStateFlow()

    fun setSearchMode(mode: SearchMode) {
        _searchMode.value = mode
        _selectedState.value = null
        _selectedCity.value = null
        _searchQuery.value = ""
        _uiState.value = WeatherUiState()
    }

    fun onStateSelected(state: USState) {
        _selectedState.value = state
        _selectedCity.value = null
    }

    fun onCitySelected(city: String?) {
        _selectedCity.value = city
        city?.let { cityName ->
            // Try to find city in selected state first, then fall back to all cities
            val cityData = _selectedState.value?.code?.let { stateCode ->
                USLocations.USCities[stateCode]?.find { it.name == cityName }
            } ?: USLocations.USCities.values.flatten().find { it.name == cityName }

            cityData?.let {
                fetchWeather(it.latitude, it.longitude)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearError() {
        _uiState.value = WeatherUiState()
    }

    /**
     * Smart search - automatically detects input type
     * Supports: addresses, city names, ZIP codes (U.S. and international)
     */
    fun search(query: String) {
        if (query.isBlank()) return
        searchLocation(query)
    }

    /**
     * Legacy method for backward compatibility
     */
    @Deprecated("Use search() instead", ReplaceWith("search(query)"))
    fun searchByAddress(address: String) = search(address)

    @Deprecated("Use search() instead", ReplaceWith("search(query)"))
    fun searchByZipCode(zipCode: String) = search(zipCode)

    /**
     * Unified search method - fetches weather for any location
     */
    private fun searchLocation(query: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState(
                isLoading = true,
                error = null
            )

            repository.search(query)
                .onSuccess { location ->
                    fetchWeather(location.latitude, location.longitude)
                }
                .onFailure { e ->
                    _uiState.value = WeatherUiState(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
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
