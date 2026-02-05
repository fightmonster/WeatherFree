# WeatherFree 🌤️

A free Android weather application built with Kotlin, Jetpack Compose, and the [National Weather Service API](https://www.weather.gov/documentation/services-web-api).

## Features

- ✅ **Free & Open Source** - No API keys required
- ✅ **Real-time Weather Data** - From the official US National Weather Service
- ✅ **Modern UI** - Built with Jetpack Compose
- ✅ **Material You** - Material 3 design system
- ✅ **7-Day Forecast** - Extended weather predictions
- ✅ **Multiple Cities** - Support for major US cities and ZIP codes

## Supported Cities

The app supports searching for major US cities:
- New York (NYC)
- Los Angeles (LA)
- Chicago
- Houston
- Phoenix
- Philadelphia
- San Antonio
- San Diego
- Dallas
- San Jose

You can also search by ZIP codes (e.g., 10001, 90210).

## Technology Stack

| Component | Technology |
|-----------|------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose (Material 3) |
| **Networking** | Retrofit + OkHttp |
| **Coroutines** | Kotlin Coroutines |
| **ViewModel** | Jetpack ViewModel |
| **Build** | Gradle (Kotlin DSL) |
| **CI/CD** | GitHub Actions |

## Getting Started

### Building the APK

The easiest way to build the APK is to use GitHub Actions:

1. **Fork or clone** this repository
2. **Push to GitHub** - The workflow will automatically trigger
3. **Download the APK** - Go to Actions → Latest run → Artifacts

### Building Locally

Prerequisites:
- JDK 17 or higher
- Android SDK 35 (Android 16)
- Gradle 8.9

```bash
# Clone the repository
git clone https://github.com/fightmonster/WeatherFree.git
cd WeatherFree

# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

## National Weather Service API

This app uses the official NWS API, which is:
- ✅ **Completely Free** - No API keys or rate limits for personal use
- ✅ **Official** - Data from US government
- ✅ **Accurate** - High-quality weather data
- ✅ **No Authentication** - No signup required

### API Documentation
https://www.weather.gov/documentation/services-web-api

## Project Structure

```
app/src/main/kotlin/com/fightmonster/weatherfree/
├── data/
│   ├── WeatherApi.kt          # Retrofit interface
│   ├── WeatherModels.kt       # Data models
│   └── WeatherRepository.kt   # Repository layer
├── ui/
│   └── WeatherScreen.kt       # Compose UI
├── viewmodel/
│   └── WeatherViewModel.kt    # ViewModel with StateFlow
└── MainActivity.kt            # Entry point
```

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Feel free to submit issues and pull requests.

---

**Made with ❤️ by fightmonster**
