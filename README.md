# WeatherFree 🌤️

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-SDK%2035-green.svg?style=flat&logo=android)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)

**WeatherFree** 是一款极简、现代且完全免费的 Android 天气应用。它直接连接到 [美国国家气象局 (NWS)](https://www.weather.gov/)，旨在为用户提供最纯粹的天气查询体验，无广告，无需登录，更无需任何私有的 API Key。

---

## ✨ 核心亮点

*   **真正的免费**: 100% 开源，利用美国政府公开数据。
*   **智能搜索**: 无论你是输入邮编、城市名，还是完整的街道地址，强大的组件都能帮你定位。
*   **动态加载**: 基于 `us_cities.json` 的城市库，支持全美 50 个州及 200+ 精选主要城市。
*   **隐私至上**: 不追踪位置，不收集个人信息。
*   **Material 3**: 深度适配 Material You 设计语言，平滑的动画与现代的层级感。

---

## 📸 应用预览

| 城市选择 | 天气详情 | 搜索功能 |
| :---: | :---: | :---: |
| ![Selection](https://via.placeholder.com/200x400?text=State+Selector) | ![Weather](https://via.placeholder.com/200x400?text=Weather+Details) | ![Search](https://via.placeholder.com/200x400?text=Smart+Search) |
> *请在 `docs/screenshots` 中替换为真实应用截图*

---

## 🛠️ 技术实现

我们的架构遵循 Android 最佳实践（Modern Android Development），确保代码的可维护性与性能：

| 领域 | 技术栈 |
|:---|:---|
| **核心** | Kotlin + Coroutines |
| **界面** | Jetpack Compose + Material 3 |
| **网络** | Retrofit 2 + OkHttp 4 |
| **数据** | Gson (静态数据解析) |
| **资源** | Coil (远程图片加载) |
| **API** | NWS Weather API, U.S. Census API, Nominatim |

---

## 🚀 快速开始

### 编译与安装

确保你拥有 **JDK 17** 及以上环境。

1.  **克隆代码**:
    ```bash
    git clone https://github.com/fightmonster/WeatherFree.git
    cd WeatherFree
    ```

2.  **一键编译 Debug APK**:
    ```bash
    ./gradlew assembleDebug
    ```

3.  **安装并运行**:
    如果你已经连接了真机或模拟器：
    ```bash
    ./gradlew installDebug
    adb shell am start -n com.fightmonster.weatherfree/.MainActivity
    ```

---

## 📖 数据源说明

本项目使用的地理数据和天气数据来自：
*   **地理编码**: [U.S. Census Bureau](https://geocoding.geo.census.gov/) 和 [OpenStreetMap Nominatim](https://nominatim.org/)。
*   **天气预报**: [National Weather Service (weather.gov)](https://api.weather.gov)。

---

## 🤝 贡献与反馈

如果你发现了 Bug 或者有好的 Feature 建议，请随时提交 [Issue](https://github.com/fightmonster/WeatherFree/issues)。

项目采用 **MIT** 协议。

---

**Made with ❤️ by [fightmonster](https://github.com/fightmonster)**
