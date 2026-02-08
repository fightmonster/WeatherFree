package com.fightmonster.weatherfree.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fightmonster.weatherfree.data.Period
import com.fightmonster.weatherfree.data.USCity
import com.fightmonster.weatherfree.data.USLocations
import com.fightmonster.weatherfree.viewmodel.WeatherViewModel
import com.fightmonster.weatherfree.viewmodel.SearchMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedState by viewModel.selectedState.collectAsState()
    val searchMode by viewModel.searchMode.collectAsState()
    val cities: List<USCity> = remember(selectedState) {
        val state = selectedState
        if (state != null) {
            USLocations.USCities[state.code] ?: emptyList()
        } else {
            emptyList()
        }
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
            // Search Mode Selector
            TabRow(
                selectedTabIndex = searchMode.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = searchMode == SearchMode.STATE_CITY,
                    onClick = { viewModel.setSearchMode(SearchMode.STATE_CITY) },
                    text = { Text("State & City") }
                )
                Tab(
                    selected = searchMode == SearchMode.SEARCH,
                    onClick = { viewModel.setSearchMode(SearchMode.SEARCH) },
                    text = { Text("Search") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search UI based on mode
            when (searchMode) {
                SearchMode.STATE_CITY -> {
                    StateCitySearchContent(
                        selectedState = selectedState,
                        cities = cities,
                        selectedCity = viewModel.selectedCity.collectAsState().value,
                        searchQuery = viewModel.searchQuery.collectAsState().value,
                        onStateSelected = { viewModel.onStateSelected(it) },
                        onCitySelected = { viewModel.onCitySelected(it) },
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) }
                    )
                }
                SearchMode.SEARCH -> {
                    SmartSearchContent(
                        searchQuery = viewModel.searchQuery.collectAsState().value,
                        onSearch = { viewModel.search(it) },
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weather Display
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateSelector(
    states: List<com.fightmonster.weatherfree.data.USState>,
    selectedState: com.fightmonster.weatherfree.data.USState?,
    onStateSelected: (com.fightmonster.weatherfree.data.USState) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Sync searchText with selectedState
    androidx.compose.runtime.LaunchedEffect(selectedState) {
        searchText = selectedState?.name ?: ""
    }

    val filteredStates: List<com.fightmonster.weatherfree.data.USState> = remember(searchText) {
        if (searchText.isEmpty()) {
            states
        } else {
            states.filter { it.name.contains(searchText, ignoreCase = true) }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Select State",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                    if (it) {
                        // Clear search when opening menu to show all items
                        searchText = ""
                    }
                }
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search states...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    singleLine = true,
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    filteredStates.forEach { state ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelector(
    cities: List<USCity>,
    selectedCity: String?,
    searchQuery: String,
    onCitySelected: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredCities: List<USCity> = remember(searchQuery, cities) {
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Select City",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                }
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search cities...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.LocationCity, contentDescription = null)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    singleLine = true,
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .exposedDropdownSize(true)
                ) {
                    filteredCities.forEach { city ->
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

@Composable
fun WeatherContent(
    currentWeather: Period,
    forecast: List<Period>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        CurrentWeatherCard(weather = currentWeather)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "7-Day Forecast",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            forecast.take(7).forEach { period ->
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
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${weather.temperature}°${weather.temperatureUnit}",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weather.shortForecast,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherDetail(
                    label = "Wind",
                    value = weather.windSpeed,
                    icon = "Wind"
                )
                WeatherDetail(
                    label = "Humidity",
                    value = "${weather.relativeHumidity?.value ?: 0}%",
                    icon = "Drop"
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
            containerColor = MaterialTheme.colorScheme.surface
        )
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

                Text(
                    text = period.shortForecast,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${period.temperature}°${period.temperatureUnit}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
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
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
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
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Retry")
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
            modifier = Modifier.padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Select State and City",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Step 1: Select a state",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Step 2: Select a city from that state",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Supports all 50 US states and 200+ major cities",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// New search mode components
@Composable
fun StateCitySearchContent(
    selectedState: com.fightmonster.weatherfree.data.USState?,
    cities: List<USCity>,
    selectedCity: String?,
    searchQuery: String,
    onStateSelected: (com.fightmonster.weatherfree.data.USState) -> Unit,
    onCitySelected: (String?) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StateSelector(
            states = USLocations.USStates,
            selectedState = selectedState,
            onStateSelected = { state ->
                onStateSelected(state)
                onCitySelected(null)
                onSearchQueryChange("")
            }
        )

        if (selectedState != null) {
            CitySelector(
                cities = cities,
                selectedCity = selectedCity,
                searchQuery = searchQuery,
                onCitySelected = onCitySelected,
                onSearchQueryChange = onSearchQueryChange
            )
        }
    }
}

/**
 * Smart search component - automatically detects input type
 * Supports: addresses, city names, ZIP codes (U.S. and international)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSearchContent(
    searchQuery: String,
    onSearch: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Smart Search",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text("Enter city, address, or ZIP code")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { onSearch(searchQuery) }) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Search")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onSearch(searchQuery) },
                modifier = Modifier.fillMaxWidth(),
                enabled = searchQuery.isNotBlank()
            ) {
                Text("Get Weather")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Example searches
            Text(
                text = "Examples:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "• New York, NY",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = "• 10001",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = "• 1600 Pennsylvania Ave, Washington, DC",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Deprecated("Use SmartSearchContent instead")
@Composable
fun ZipCodeSearchContent(
    searchQuery: String,
    onSearch: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit
) = SmartSearchContent(searchQuery, onSearch, onSearchQueryChange)
