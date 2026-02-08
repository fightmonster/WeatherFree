package com.fightmonster.weatherfree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.fightmonster.weatherfree.data.USLocations
import com.fightmonster.weatherfree.ui.WeatherScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load city data on startup
        lifecycleScope.launch {
            USLocations.load(this@MainActivity)
        }

        setContent {
            val dataLoaded by USLocations.isLoaded.collectAsState()

            WeatherFreeTheme {
                if (dataLoaded) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherFreeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(),
        content = content
    )
}
