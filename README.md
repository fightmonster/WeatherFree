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

### 📥 Downloading APKs

You can download pre-built APKs from [GitHub Releases](https://github.com/fightmonster/WeatherFree/releases):

- **Debug APK** - For testing and development
- **Release APK** - Signed production build ready for installation

### 🔨 Building from Source

Prerequisites:
- JDK 17 or higher
- Android SDK 35 (Android 16)
- Gradle 8.9

#### Option 1: Quick Build (Debug APK)

```bash
# Clone the repository
git clone https://github.com/fightmonster/WeatherFree.git
cd WeatherFree

# Build debug APK (uses auto-generated debug key)
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

#### Option 2: Build Signed Release APK

**For developers** who want to build their own signed release:

```bash
# Clone the repository
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
cat > keystore.properties << EOF
STORE_FILE=my-release.keystore
KEY_ALIAS=my-key-alias
STORE_PASSWORD=your-keystore-password
KEY_PASSWORD=your-key-password
EOF

# Build release APK
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

**Note:** The `keystore.properties` file is already in `.gitignore`, so it won't be committed to the repository.

### 🚀 Using GitHub Actions

You can also use GitHub Actions to build the APK:

1. **Fork this repository**
2. **Push changes to your fork** - The workflow will automatically trigger
3. **Download the APK** - Go to Actions → Latest run → Artifacts

**For release builds with your own signing:**

1. Go to your fork's **Settings → Secrets and variables → Actions**
2. Add the following secrets:
   - `KEYSTORE_BASE64` - Your keystore file encoded in base64 (`cat your-release.keystore | base64 -w 0`)
   - `KEYSTORE_PASSWORD` - Your keystore password
   - `KEY_PASSWORD` - Your key password
3. Create a tag to trigger the release workflow:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

## 🔐 About APK Signing

### Official Releases

Official releases from this repository are signed with the WeatherFree signing key. This ensures:
- Authenticity - The APK comes from this repository
- Update compatibility - Future updates will work seamlessly
- Trust - Users can verify the source

### Developer Builds

When building locally:
- **Debug APK** - Uses an auto-generated debug key (no setup required)
- **Release APK** - You must use your own keystore (see above)

**Important:** Never share your keystore or passwords! If you lose your keystore, you won't be able to update your app with the same signature.

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
