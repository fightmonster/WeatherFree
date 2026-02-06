package com.fightmonster.weatherfree.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fightmonster.weatherfree.data.Period
import com.fightmonster.weatherfree.data.USCity
import com.fightmonster.weatherfree.data.USStates
import com.fightmonster.weatherfree.data.USCities
import com.fightmonster.weatherfree.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedState by viewModel.selectedState.collectAsState()
    val cities = remember(selectedState) { state ->
        state?.let { USCities[it.code] } ?: emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WeatherFree") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Two-column layout for cascading selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First column: State selection
                StateSelector(
                    states = USStates,
                    selectedState = selectedState,
                    onStateSelected = { state ->
                        viewModel.onStateSelected(state)
                    // Clear city selection when state changes
                        viewModel.onCitySelected(null)
                    }
                )

                // Second column: City selection (only visible when state is selected)
                if (selectedState != null) {
                    CitySelector(
                        cities = cities,
                        selectedCity = viewModel.selectedCity.collectAsState().value,
                        searchQuery = viewModel.searchQuery.collectAsState().value,
                        onCitySelected = { viewModel.onCitySelected(it) },
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    ErrorCard(
                        message = uiState.error ?: "Unknown error",
                        onRetry = { viewModel.clearError() }
                    )
                }
                uiState.currentWeather != null -> {
                    WeatherContent(
                        currentWeather = uiState.currentWeather!!,
                        forecast = uiState.forecast
                    )
                }
                else -> {
                    EmptyState()
                }
            }
        }
    }
}

@Composable
fun StateSelector(
    states: List<com.fightmonster.weatherfree.data.USState>,
    selectedState: com.fightmonster.weatherfree.data.USState?,
    onStateSelected: (com.fightmonster.weatherfree.data.USState) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredStates = remember(searchText) {
        if (searchText.isEmpty()) {
            states
        } else {
            states.filter { it.name.contains(searchText, ignoreCase = true) }
        }
    }

    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "🌍 选择州",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
                modifier = Modifier
                    .fillMaxWidth()
            ) { exposedBox ->
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("搜索或选择州...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search States",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .exposedDropdownSize(true)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 300.dp)
                    ) {
                        items(filteredStates) { state ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = state.name,
                                        color = if (selectedState?.code == state.code) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                },
                                leadingIcon = if (selectedState?.code == state.code) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                } else null,
                                onClick = {
                                    onStateSelected(state)
                                    expanded = false
                                    searchText = state.name
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CitySelector(
    cities: List<USCity>,
    selectedCity: String?,
    searchQuery: String,
    onCitySelected: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredCities = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            cities
        } else {
            cities.filter { city ->
                city.name.contains(searchQuery, ignoreCase = true) ||
                city.zip.contains(searchQuery) ||
                city.state.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "🏙️ 选择城市",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier
                    .fillMaxWidth()
            ) { exposedBox ->
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("搜索或选择城市...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = "Search Cities",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchQuery.isNotBlank()) {
                                val city = filteredCities.firstOrNull()
                                city?.let { onCitySelected(it.name) }
                            }
                        }
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .exposedDropdownSize(true)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 400.dp)
                    ) {
                        items(filteredCities) { city ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                                text = city.name,
                                                color = if (selectedCity == city.name) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.onSurface
                                                }
                                                )
                                        Text(
                                                text = "${city.state} - ${city.zip}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (selectedCity == city.name) {
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                                } else {
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                                }
                                                )
                                    }
                                },
                                leadingIcon = if (selectedCity == city.name) {
                                    {
                                        Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                } else null,
                                onClick = {
                                    onCitySelected(city.name)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherContent(
    currentWeather: Period,
    forecast: List<Period>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Current Weather
        CurrentWeatherCard(weather = currentWeather)

        Spacer(modifier = Modifier.height(16.dp))

        // Forecast
        Text(
            text = "7-Day Forecast",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(forecast.take(7)) { period ->
                ForecastItem(period = period)
            }
        }
    }
}

@Composable
fun CurrentWeatherCard(weather: Period) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(24.dp)
        border = if (weather.isDaytime) {
            CardDefaults.outlinedCardBorderBrush()
        } else null
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Location name
            Text(
                text = weather.name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Temperature with icon
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weather icon
                Text(
                    text = when {
                        weather.shortForecast.contains("Sunny", ignoreCase = true) -> "☀️"
                        weather.shortForecast.contains("Cloud", ignoreCase = true) -> "☁️"
                        weather.shortForecast.contains("Rain", ignoreCase = true) -> "🌧"
                        weather.shortForecast.contains("Snow", ignoreCase = true) -> "❄️"
                        weather.shortForecast.contains("Thunder", ignoreCase = true) -> "⛈"
                        else -> "🌤️"
                    },
                    style = MaterialTheme.typography.displayLarge
                )

                // Temperature
                Text(
                    text = "${weather.temperature}°${weather.temperatureUnit}",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Short forecast
            Text(
                text = weather.shortForecast,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Details row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherDetail(
                    label = "Wind",
                    value = weather.windSpeed,
                    icon = "💨"
                )
                WeatherDetail(
                    label = "Humidity",
                    value = "${weather.relativeHumidity?.value ?: 0}%",
                    icon = "💧"
                )
            }
        }
    }
}

@Composable
fun ForecastItem(period: Period) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (period.isDaytime) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = period.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            period.shortForecast.contains("Sunny", ignoreCase = true) -> "☀️"
                            period.shortForecast.contains("Cloud", ignoreCase = true) -> "☁️"
                            period.shortForecast.contains("Rain", ignoreCase = true) -> "🌧"
                            period.shortForecast.contains("Snow", ignoreCase = true) -> "❄️"
                            period.shortForecast.contains("Thunder", ignoreCase = true) -> "⛈"
                            else -> "🌤️"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "${period.temperature}°${period.temperatureUnit}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = period.shortForecast,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Date
            Text(
                text = period.startTime.substring(0, 10),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeatherDetail(label: String, value: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "❌",
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRetry
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Retry", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}

@Composable
fun EmptyState() {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "🌤️",
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "选择您的州和城市",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "第一步：选择州",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "第二步：选择该州的城市",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "📍 支持所有50个美国州和200+主要城市",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
