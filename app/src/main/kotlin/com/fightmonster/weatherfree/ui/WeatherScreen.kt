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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fightmonster.weatherfree.data.Period
import com.fightmonster.weatherfree.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedState by viewModel.selectedState.collectAsState()

    val states = remember { USStates }
    val cities = remember(selectedState) { state ->
        state?.let { USCities[it] } ?: emptyList()
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
                .padding(16.dp)
        ) {
            // State Selection
            StateSelector(
                states = states,
                selectedState = selectedState,
                onStateSelected = { viewModel.onStateSelected(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // City Selection (only visible when state is selected)
            if (selectedState != null) {
                CitySelector(
                    cities = cities,
                    searchQuery = searchQuery,
                    selectedCity = viewModel.selectedCity.collectAsState().value,
                    onCitySelected = { viewModel.onCitySelected(it) },
                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

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
    states: List<USState>,
    selectedState: USState?,
    onStateSelected: (USState) -> Unit
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

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) { expandedBox ->
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            placeholder = { Text("Select State") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location"
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize(true)
        ) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 300.dp)
            ) {
                items(filteredStates) { state ->
                    DropdownMenuItem(
                        text = { Text(state.name) },
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

@Composable
fun CitySelector(
    cities: List<USCity>,
    searchQuery: String,
    selectedCity: String?,
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

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) { expandedBox ->
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            placeholder = { Text("Select or search city") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationCity,
                    contentDescription = "City"
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchQuery.isNotBlank()) {
                        // Find first matching city and select it
                        filteredCities.firstOrNull()?.let { onCitySelected(it.name) }
                    }
                }
            ),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize(true)
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
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${city.state} - ${city.zip}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
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
        modifier = Modifier
            .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${weather.temperature}°${weather.temperatureUnit}",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weather.shortForecast,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
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
fun WeatherDetail(label: String, value: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                Text(
                    text = period.shortForecast,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${period.temperature}°",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
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
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Dismiss")
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌤️",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Select a state and city",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose from the dropdown or search for a location",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data Classes
data class USState(
    val name: String,
    val code: String
)

data class USCity(
    val name: String,
    val state: String,
    val zip: String
)

// US States Data
val USStates = listOf(
    USState("Alabama", "AL"),
    USState("Alaska", "AK"),
    USState("Arizona", "AZ"),
    USState("Arkansas", "AR"),
    USState("California", "CA"),
    USState("Colorado", "CO"),
    USState("Connecticut", "CT"),
    USState("Delaware", "DE"),
    USState("Florida", "FL"),
    USState("Georgia", "GA"),
    USState("Hawaii", "HI"),
    USState("Idaho", "ID"),
    USState("Illinois", "IL"),
    USState("Indiana", "IN"),
    USState("Iowa", "IA"),
    USState("Kansas", "KS"),
    USState("Kentucky", "KY"),
    USState("Louisiana", "LA"),
    USState("Maine", "ME"),
    USState("Maryland", "MD"),
    USState("Massachusetts", "MA"),
    USState("Michigan", "MI"),
    USState("Minnesota", "MN"),
    USState("Mississippi", "MS"),
    USState("Missouri", "MO"),
    USState("Montana", "MT"),
    USState("Nebraska", "NE"),
    USState("Nevada", "NV"),
    USState("New Hampshire", "NH"),
    USState("New Jersey", "NJ"),
    USState("New Mexico", "NM"),
    USState("New York", "NY"),
    USState("North Carolina", "NC"),
    USState("North Dakota", "ND"),
    USState("Ohio", "OH"),
    USState("Oklahoma", "OK"),
    USState("Oregon", "OR"),
    USState("Pennsylvania", "PA"),
    USState("Rhode Island", "RI"),
    USState("South Carolina", "SC"),
    USState("South Dakota", "SD"),
    USState("Tennessee", "TN"),
    USState("Texas", "TX"),
    USState("Utah", "UT"),
    USState("Vermont", "VT"),
    USState("Virginia", "VA"),
    USState("Washington", "WA"),
    USState("West Virginia", "WV"),
    USState("Wisconsin", "WI"),
    USState("Wyoming", "WY"),
    USState("District of Columbia", "DC"),
    USState("Puerto Rico", "PR"),
    USState("Guam", "GU"),
    USState("American Samoa", "AS"),
    USState("Northern Mariana Islands", "MP"),
    USState("Virgin Islands", "VI")
)

// Major US Cities Data by State
val USCities = mapOf(
    "AL" to listOf(
        USCity("Birmingham", "AL", "35203"),
        USCity("Montgomery", "AL", "36101"),
        USCity("Huntsville", "AL", "35801"),
        USCity("Mobile", "AL", "36601")
    ),
    "AK" to listOf(
        USCity("Anchorage", "AK", "99501"),
        USCity("Fairbanks", "AK", "99701"),
        USCity("Juneau", "AK", "99801")
    ),
    "AZ" to listOf(
        USCity("Phoenix", "AZ", "85001"),
        USCity("Tucson", "AZ", "85701"),
        USCity("Mesa", "AZ", "85201"),
        USCity("Scottsdale", "AZ", "85251")
    ),
    "AR" to listOf(
        USCity("Little Rock", "AR", "72201"),
        USCity("Fort Smith", "AR", "72901"),
        USCity("Fayetteville", "AR", "72701")
    ),
    "CA" to listOf(
        USCity("Los Angeles", "CA", "90210"),
        USCity("San Francisco", "CA", "94102"),
        USCity("San Diego", "CA", "92101"),
        USCity("San Jose", "CA", "95101"),
        USCity("Sacramento", "CA", "94203"),
        USCity("Oakland", "CA", "94601"),
        USCity("Fresno", "CA", "93701")
    ),
    "CO" to listOf(
        USCity("Denver", "CO", "80201"),
        USCity("Colorado Springs", "CO", "80901"),
        USCity("Aurora", "CO", "80011"),
        USCity("Fort Collins", "CO", "80521")
    ),
    "CT" to listOf(
        USCity("Hartford", "CT", "06101"),
        USCity("New Haven", "CT", "06510"),
        USCity("Stamford", "CT", "06901")
    ),
    "DE" to listOf(
        USCity("Wilmington", "DE", "19801"),
        USCity("Dover", "DE", "19901")
    ),
    "FL" to listOf(
        USCity("Miami", "FL", "33101"),
        USCity("Jacksonville", "FL", "32201"),
        USCity("Tampa", "FL", "33601"),
        USCity("Orlando", "FL", "32801"),
        USCity("Tallahassee", "FL", "32301")
    ),
    "GA" to listOf(
        USCity("Atlanta", "GA", "30301"),
        USCity("Savannah", "GA", "31401"),
        USCity("Augusta", "GA", "30901")
    ),
    "HI" to listOf(
        USCity("Honolulu", "HI", "96801"),
        USCity("Pearl City", "HI", "96782")
    ),
    "ID" to listOf(
        USCity("Boise", "ID", "83701"),
        USCity("Coeur d'Alene", "ID", "83814")
    ),
    "IL" to listOf(
        USCity("Chicago", "IL", "60601"),
        USCity("Springfield", "IL", "62701"),
        USCity("Peoria", "IL", "61601"),
        USCity("Rockford", "IL", "61101")
    ),
    "IN" to listOf(
        USCity("Indianapolis", "IN", "46201"),
        USCity("Fort Wayne", "IN", "46801"),
        USCity("South Bend", "IN", "46601")
    ),
    "IA" to listOf(
        USCity("Des Moines", "IA", "50301"),
        USCity("Cedar Rapids", "IA", "52401"),
        USCity("Davenport", "IA", "52801")
    ),
    "KS" to listOf(
        USCity("Wichita", "KS", "67201"),
        USCity("Topeka", "KS", "66601"),
        USCity("Kansas City", "KS", "66101")
    ),
    "KY" to listOf(
        USCity("Louisville", "KY", "40201"),
        USCity("Lexington", "KY", "40501"),
        USCity("Bowling Green", "KY", "42101")
    ),
    "LA" to listOf(
        USCity("New Orleans", "LA", "70101"),
        USCity("Baton Rouge", "LA", "70801"),
        USCity("Shreveport", "LA", "71101")
    ),
    "MA" to listOf(
        USCity("Boston", "MA", "02101"),
        USCity("Worcester", "MA", "01601"),
        USCity("Springfield", "MA", "01101"),
        USCity("Cambridge", "MA", "02139")
    ),
    "MD" to listOf(
        USCity("Baltimore", "MD", "21201"),
        USCity("Annapolis", "MD", "21401"),
        USCity("Silver Spring", "MD", "20901")
    ),
    "ME" to listOf(
        USCity("Portland", "ME", "04101"),
        USCity("Augusta", "ME", "04330"),
        USCity("Bangor", "ME", "04401")
    ),
    "MI" to listOf(
        USCity("Detroit", "MI", "48201"),
        USCity("Grand Rapids", "MI", "49501"),
        USCity("Lansing", "MI", "48901"),
        USCity("Ann Arbor", "MI", "48103")
    ),
    "MN" to listOf(
        USCity("Minneapolis", "MN", "55401"),
        USCity("Saint Paul", "MN", "55101"),
        USCity("Rochester", "MN", "55901")
    ),
    "MO" to listOf(
        USCity("Kansas City", "MO", "64101"),
        USCity("St. Louis", "MO", "63101"),
        USCity("Springfield", "MO", "65801")
    ),
    "MS" to listOf(
        USCity("Jackson", "MS", "39201"),
        USCity("Gulfport", "MS", "39501"),
        USCity("Biloxi", "MS", "39530")
    ),
    "MT" to listOf(
        USCity("Billings", "MT", "59101"),
        USCity("Missoula", "MT", "59801"),
        USCity("Great Falls", "MT", "59401")
    ),
    "NC" to listOf(
        USCity("Charlotte", "NC", "28201"),
        USCity("Raleigh", "NC", "27601"),
        USCity("Greensboro", "NC", "27401"),
        USCity("Winston-Salem", "NC", "27101")
    ),
    "ND" to listOf(
        USCity("Fargo", "ND", "58101"),
        USCity("Bismarck", "ND", "58501"),
        USCity("Grand Forks", "ND", "58201")
    ),
    "NE" to listOf(
        USCity("Omaha", "NE", "68101"),
        USCity("Lincoln", "NE", "68501"),
        USCity("Bellevue", "NE", "68005")
    ),
    "NV" to listOf(
        USCity("Las Vegas", "NV", "89101"),
        USCity("Reno", "NV", "89501"),
        USCity("Henderson", "NV", "89002")
    ),
    "NH" to listOf(
        USCity("Manchester", "NH", "03101"),
        USCity("Nashua", "NH", "03060"),
        USCity("Concord", "NH", "03301")
    ),
    "NJ" to listOf(
        USCity("Newark", "NJ", "07101"),
        USCity("Jersey City", "NJ", "07302"),
        USCity("Paterson", "NJ", "07501"),
        USCity("Trenton", "NJ", "08601")
    ),
    "NM" to listOf(
        USCity("Albuquerque", "NM", "87101"),
        USCity("Santa Fe", "NM", "87501"),
        USCity("Las Cruces", "NM", "88001")
    ),
    "NY" to listOf(
        USCity("New York", "NY", "10001"),
        USCity("Buffalo", "NY", "14201"),
        USCity("Rochester", "NY", "14601"),
        USCity("Albany", "NY", "12201"),
        USCity("Syracuse", "NY", "13201")
    ),
    "OH" to listOf(
        USCity("Columbus", "OH", "43201"),
        USCity("Cleveland", "OH", "44101"),
        USCity("Cincinnati", "OH", "45201"),
        USCity("Toledo", "OH", "43601")
    ),
    "OK" to listOf(
        USCity("Oklahoma City", "OK", "73101"),
        USCity("Tulsa", "OK", "74101"),
        USCity("Norman", "OK", "73019")
    ),
    "OR" to listOf(
        USCity("Portland", "OR", "97201"),
        USCity("Eugene", "OR", "97401"),
        USCity("Salem", "OR", "97301"),
        USCity("Gresham", "OR", "97080")
    ),
    "PA" to listOf(
        USCity("Philadelphia", "PA", "19101"),
        USCity("Pittsburgh", "PA", "15201"),
        USCity("Allentown", "PA", "18101"),
        USCity("Erie", "PA", "16501")
    ),
    "RI" to listOf(
        USCity("Providence", "RI", "02901"),
        USCity("Warwick", "RI", "02886"),
        USCity("Cranston", "RI", "02920")
    ),
    "SC" to listOf(
        USCity("Columbia", "SC", "29201"),
        USCity("Charleston", "SC", "29401"),
        USCity("Greenville", "SC", "29601")
    ),
    "SD" to listOf(
        USCity("Sioux Falls", "SD", "57101"),
        USCity("Rapid City", "SD", "57701"),
        USCity("Aberdeen", "SD", "57401")
    ),
    "TN" to listOf(
        USCity("Nashville", "TN", "37201"),
        USCity("Memphis", "TN", "38101"),
        USCity("Knoxville", "TN", "37901"),
        USCity("Chattanooga", "TN", "37401")
    ),
    "TX" to listOf(
        USCity("Houston", "TX", "77001"),
        USCity("Dallas", "TX", "75201"),
        USCity("San Antonio", "TX", "78201"),
        USCity("Austin", "TX", "78701"),
        USCity("Fort Worth", "TX", "76101"),
        USCity("El Paso", "TX", "79901")
    ),
    "UT" to listOf(
        USCity("Salt Lake City", "UT", "84101"),
        USCity("Provo", "UT", "84601"),
        USCity("Ogden", "UT", "84401")
    ),
    "VT" to listOf(
        USCity("Burlington", "VT", "05401"),
        USCity("South Burlington", "VT", "05403"),
        USCity("Rutland", "VT", "05701")
    ),
    "VA" to listOf(
        USCity("Virginia Beach", "VA", "23451"),
        USCity("Norfolk", "VA", "23501"),
        USCity("Richmond", "VA", "23219"),
        USCity("Arlington", "VA", "22201")
    ),
    "WA" to listOf(
        USCity("Seattle", "WA", "98101"),
        USCity("Spokane", "WA", "99201"),
        USCity("Tacoma", "WA", "98401"),
        USCity("Bellevue", "WA", "98004")
    ),
    "WV" to listOf(
        USCity("Charleston", "WV", "25301"),
        USCity("Huntington", "WV", "25701"),
        USCity("Parkersburg", "WV", "26101")
    ),
    "WI" to listOf(
        USCity("Milwaukee", "WI", "53201"),
        USCity("Madison", "WI", "53701"),
        USCity("Green Bay", "WI", "54301")
    ),
    "WY" to listOf(
        USCity("Cheyenne", "WY", "82001"),
        USCity("Casper", "WY", "82601"),
        USCity("Laramie", "WY", "82070")
    ),
    "DC" to listOf(
        USCity("Washington", "DC", "20001")
    ),
    "PR" to listOf(
        USCity("San Juan", "PR", "00901")
    )
)
