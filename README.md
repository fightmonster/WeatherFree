# WeatherFree 🌤️

A free Android weather application built with Kotlin, Jetpack Compose, and the [National Weather Service API](https://www.weather.gov/documentation/services-web-api).

## Features

- ✅ **Free & Open Source** - No API keys required
- ✅ **Real-time Weather Data** - From the official US National Weather Service
- ✅ **Modern UI** - Built with Jetpack Compose
- ✅ **Material You** - Material 3 design system
- ✅ **7-Day Forecast** - Extended weather predictions
- ✅ **State & City Selection** - Easy access with dropdown menus
- ✅ **Multiple Cities** - Support for 50 US States and 200+ Major US Cities

## Supported States & Cities

The app supports all 50 US States with major cities:

- Alabama - Birmingham, Montgomery, Huntsville, Mobile
- Alaska - Anchorage, Fairbanks, Juneau
- Arizona - Phoenix, Tucson, Mesa, Scottsdale
- Arkansas - Little Rock, Fort Smith, Fayetteville
- California - Los Angeles, San Francisco, San Diego, San Jose, Sacramento
- And 42 more states!

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

### 📥 Downloading APKs

You can download pre-built APKs from [GitHub Releases](https://github.com/fightmonster/WeatherFree/releases):

- **Signed Debug APK** - Signed with official WeatherFree signing key
- **Release APK** - Production build ready for installation

### ⚙️ Building from Source

Prerequisites:
- JDK 17 or higher
- Android SDK 35 (Android 16)
- Gradle 8.9

#### Quick Build (Debug APK)

```bash
# Clone repository
git clone https://github.com/fightmonster/WeatherFree.git
cd WeatherFree

# Build debug APK (uses auto-generated debug key)
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

#### Building Signed Release APK

**For developers** who want to build their own signed release:

```bash
# Clone repository
git clone https://github.com/fightmonster/WeatherFree.git
cd WeatherFree

# Generate your own keystore (for release signing)
keytool -genkey -v \
  -keystore my-release.keystore \
  -alias my-key-alias \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000

# Create keystore.properties file
cat > keystore.properties << 'EOF'
STORE_FILE=my-release.keystore
KEY_ALIAS=my-key-alias
STORE_PASSWORD=your-keystore-password
KEY_PASSWORD=your-key-password
'EOF

# Build release APK
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

## How to Use the App

1. **Launch the app**
2. **Select a State** - Choose from the dropdown menu
3. **Select a City** - Choose from the filtered city list or search
4. **View Weather** - See current conditions and 7-day forecast

## Contributing

Contributions are welcome! Feel free to submit issues and pull requests.

## License

This project is open source and available under the MIT License.

---

**Made with ❤️ by fightmonster**
