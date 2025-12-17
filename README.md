# AEM KMP Boilerplate

A Kotlin Multiplatform (KMP) boilerplate for migrating [AEM Edge Delivery Services (EDS)](https://www.aem.live/) sites to native **Android**, **iOS**, and **Desktop (JVM)** applications. This starter project provides a complete foundation for rendering EDS content natively using Compose Multiplatform.

## What It Does

This boilerplate fetches content from AEM EDS sites via JSON and renders it natively on all platforms. It includes:

- **Block Rendering System** - Native UI components for common EDS blocks (Hero, Cards, Columns)
- **Navigation** - Type-safe routing with deep linking support
- **Theming** - Material 3 design with customizable colors and typography
- **Push Notifications** - Cross-platform notifications using Firebase (Android/iOS) and KMPNotifier
- **Image Loading** - Efficient image handling with Coil
- **Network Layer** - Ktor-based HTTP client with platform-specific engines

## Quick Start

### Configure Your EDS Site

Update `composeApp/src/commonMain/kotlin/com/adobe/aem_kmp_boilerplate/data/EdsConfig.kt`:

```kotlin
val DefaultEdsConfig = EdsConfig(
    siteUrl = "https://your-site.aem.live",
    homePath = "",  // Optional: custom home page path (e.g., "emea/en/products")
)
```

### Run the App

**Android:**
```bash
./gradlew :composeApp:assembleDebug
```

**Desktop (JVM):**
```bash
./gradlew :composeApp:run
```

**iOS:**
```bash
open iosApp/iosApp.xcodeproj
# Run from Xcode
```

## 📁 Project Structure

- **[composeApp/src/commonMain](./composeApp/src/commonMain/kotlin)** - Shared code for all platforms
  - `blocks/` - EDS block renderers (Hero, Cards, Columns, etc.)
  - `data/` - Data models and EDS configuration
  - `navigation/` - Navigation routes and link handling
  - `network/` - HTTP client and API service
  - `screens/` - Screen composables
  - `theme/` - Material 3 theming
- **[composeApp/src/androidMain](./composeApp/src/androidMain)** - Android-specific code
- **[composeApp/src/iosMain](./composeApp/src/iosMain)** - iOS-specific code
- **[composeApp/src/jvmMain](./composeApp/src/jvmMain)** - Desktop-specific code
- **[iosApp](./iosApp)** - iOS app wrapper (SwiftUI entry point)

## 🔧 Customization

### Add Custom EDS Blocks

1. Create a new composable in `blocks/YourBlock.kt`
2. Add it to `blocks/BlockRenderer.kt`

### Update Branding

- **Colors**: Edit `theme/Color.kt`
- **Typography**: Edit `theme/Typography.kt`
- **App Name**: Update `AndroidManifest.xml` (Android), `Info.plist` (iOS), and `main.kt` (Desktop)

## 📚 Documentation

For detailed architecture, migration guides, and development instructions, see [CLAUDE.md](./CLAUDE.md).

## 🛠️ Tech Stack

- Kotlin 2.2.21 & Compose Multiplatform 1.10.0
- Ktor 3.3.3 (Networking)
- Koin 4.1.1 (Dependency Injection)
- Coil 3.3.0 (Image Loading)
- Navigation 3 (Type-safe Navigation)
- KMPNotifier 1.6.1 (Push Notifications)
- Firebase (Cloud Messaging & Analytics)

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [AEM Edge Delivery Services](https://www.aem.live/).