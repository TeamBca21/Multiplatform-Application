# KMM E-Commerce Application

## 1. Project Overview

This is a Kotlin Multiplatform Mobile (KMM) E-Commerce application targeting Android, iOS, and Web platforms. It demonstrates a shared codebase approach for building cross-platform applications with a focus on sharing business logic and UI.

**Key Features:**
*   **Product List:** Displays a list of products fetched from a remote API.
*   **Product Details:** Shows detailed information for a selected product.
*   **Cross-Platform UI:** Utilizes Compose Multiplatform for rendering UI across Android, iOS (experimental), and Web.

The core principle is to maximize code reuse by writing platform-agnostic code in the `shared` module using Kotlin, while platform-specific implementations are handled in their respective modules (`android`, `iosApp`, `web`).

## 2. Project Structure

The project is organized into the following main modules:

*   **`shared`**:
    *   This is the heart of the KMM application, containing code common to all platforms.
    *   **Business Logic:** ViewModels (`ProductListViewModel`, `ProductDetailsViewModel`), use cases, and data management.
    *   **UI:** Compose Multiplatform screens (`ProductListScreen`, `ProductDetailsScreen`) and components (`ProductItem`).
    *   **Data Layer:**
        *   Data models (`Product`, `Rating`) using Kotlinx Serialization.
        *   Networking with Ktor client for fetching data from the FakeStoreAPI.
        *   Repository pattern (`ProductRepository`) to abstract data sources.
    *   **Navigation:** Shared navigation logic using Voyager.
    *   **Image Loading:** Asynchronous image loading with Kamel.

*   **`android`**:
    *   The Android-specific application module.
    *   Contains the `MainActivity` which hosts the Compose Multiplatform UI from the `shared` module.
    *   Includes Android-specific configurations, resources (`AndroidManifest.xml`, `strings.xml`, icons).

*   **`iosApp`**:
    *   The iOS-specific application module, structured as an Xcode project.
    *   Contains the `iOSApp` entry point and `ContentView` (SwiftUI) which integrates the Compose Multiplatform UI via `ComposeUIViewController`.
    *   Includes iOS-specific configurations (`Info.plist`, assets).

*   **`web`**:
    *   The Web-specific application module built using Kotlin/JS with Compose for Web.
    *   Contains the `main.kt` entry point that renders the Compose for Web UI.
    *   Includes web-specific resources like `index.html`.

**Key Technologies Used:**
*   **Kotlin:** Primary programming language.
*   **Kotlin Multiplatform (KMM):** Framework for sharing code between platforms.
*   **Compose Multiplatform:** Declarative UI framework for Android, iOS (experimental), and Web.
*   **Ktor:** Asynchronous HTTP client for networking.
*   **Voyager:** Shared navigation library for Compose Multiplatform.
*   **Kamel:** Asynchronous image loading library for Compose Multiplatform.
*   **Kotlinx Serialization:** For JSON parsing and data serialization.
*   **Gradle:** Build system with Kotlin DSL.

## 3. `expect`/`actual` Implementations

The `expect`/`actual` pattern is a core KMM mechanism for declaring platform-specific APIs in common code (`expect`) and providing their concrete implementations in platform-specific modules (`actual`).

In this project, direct custom `expect`/`actual` implementations are minimal. This is because many platform-specific requirements are handled internally by the libraries used:
*   **Ktor:** Provides different HTTP client engines for Android, iOS, and JS environments, configured within the shared module.
*   **Compose Multiplatform:** Handles rendering UI components natively on each target platform.
*   **Kamel:** Manages image loading using platform-specific capabilities.

**Conceptual Example:**
If we needed to get the current platform's name, we might define it in `shared/src/commonMain/kotlin`:
```kotlin
// In shared/src/commonMain/kotlin/com/example/utils/Platform.kt
expect fun getPlatformName(): String
```
And then provide `actual` implementations:
*   `shared/src/androidMain/kotlin/com/example/utils/Platform.kt`:
    ```kotlin
    actual fun getPlatformName(): String = "Android"
    ```
*   `shared/src/iosMain/kotlin/com/example/utils/Platform.kt`:
    ```kotlin
    actual fun getPlatformName(): String = "iOS"
    ```
*   `shared/src/jsMain/kotlin/com/example/utils/Platform.kt`:
    ```kotlin
    actual fun getPlatformName(): String = "Web (JS)"
    ```
This project currently relies on the libraries to abstract these differences, promoting a more streamlined shared codebase.

## 4. Building and Running the Project

### Prerequisites:
*   **JDK:** Version 17 or 18 (Amazon Corretto or OpenJDK recommended).
*   **Android Studio:** Latest stable version (e.g., Hedgehog, Iguana).
*   **Xcode:** Latest stable version (e.g., 15.x, for iOS development).
*   **Node.js:** (Optional, for web) While the Kotlin/JS Gradle plugin handles most web build tasks, having Node.js (LTS version) can be useful for certain JS tooling or if you intend to extend the web part with custom JS scripts.

### Common Setup:
1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd KMM-E-Commerce-App # Or your project directory name
    ```
2.  **Import into Android Studio:**
    *   Open Android Studio.
    *   Select "Open" and navigate to the cloned project's root directory.
3.  **Gradle Sync:**
    *   Allow Android Studio to perform a Gradle sync. This will download dependencies and configure the project. This might take some time on the first import.

### Android:
1.  In Android Studio, select the `android` run configuration from the dropdown menu.
2.  Choose a target Android device or emulator.
3.  Click the "Run" button (green play icon) or use the shortcut `Shift + F10`.

### iOS:
1.  **Initial Framework Build (if needed):**
    *   The shared KMM module needs to be compiled into a framework that Xcode can understand. Android Studio's Gradle sync usually triggers tasks like `linkDebugFrameworkIosX64` or `linkReleaseFrameworkIosArm64`.
    *   You can also run these tasks manually from the Gradle panel in Android Studio or via the terminal:
        ```bash
        ./gradlew :shared:linkDebugFrameworkIosX64 # For x64 simulators
        ./gradlew :shared:linkDebugFrameworkIosArm64 # For ARM64 simulators/devices
        ```
2.  **Open Xcode:**
    *   Navigate to the `iosApp` directory in your project.
    *   Open `iosApp.xcworkspace` in Xcode. (Always use `.xcworkspace` for projects with CocoaPods or KMM frameworks).
3.  **Select Target and Device/Simulator:**
    *   In Xcode, select the `iosApp` scheme.
    *   Choose a target iOS simulator or a connected device.
4.  **Run:**
    *   Click the "Run" button (play icon) in Xcode or use the shortcut `Cmd + R`.

### Web:
The Kotlin/JS Gradle plugin provides tasks to run and build the web application.

1.  **Development Server:**
    *   To run the web app with a development server (with hot reloading):
        ```bash
        ./gradlew :web:jsBrowserDevelopmentRun
        ```
    *   This will typically start a server (e.g., on `http://localhost:8080`). Open this URL in your browser. The specific port might vary. Check the Gradle output.

2.  **Production Build (Distribution):**
    *   To create a production-ready build:
        ```bash
        ./gradlew :web:jsBrowserDistribution
        ```
    *   The output files (HTML, JS, CSS, assets) will be generated in `web/build/distributions/`.
    *   You can then deploy these files to any static web hosting service. The main entry point is `index.html`.

    *(Note: If using WasmJs target, the tasks would be `:web:wasmJsBrowserDevelopmentRun` and `:web:wasmJsBrowserDistribution` respectively, and output in `web/build/dist/wasmJs/productionExecutable/`)*

## 5. Key Libraries Used

*   **Kotlin Multiplatform (KMM):** Core framework for code sharing.
*   **Compose Multiplatform:** For building declarative UIs across Android, iOS, and Web from a single codebase.
*   **Ktor:** Networking client for making HTTP requests to the backend API. Supports platform-specific engines.
*   **Voyager:** Navigation library for Compose Multiplatform, simplifying screen transitions and state management in a shared navigation model.
*   **Kamel:** Image loading library for Compose Multiplatform, handling asynchronous fetching and display of images from URLs.
*   **Kotlinx Serialization:** For serializing/deserializing Kotlin objects to/from JSON.
*   **Kotlinx Coroutines:** For managing asynchronous operations.
*   **Gradle (Kotlin DSL):** Build automation system.

## 6. Further Development/Improvements

This project serves as a foundational example. Potential areas for future development include:

*   **State Management:** Explore more advanced state management solutions if the app complexity grows (e.g., MVI-Kotlin, Decompose, or integrating Voyager's ViewModel features more deeply).
*   **Error Handling:** Implement more robust and user-friendly error display across all layers (UI, ViewModel, Repository).
*   **Caching:** Add data caching capabilities to the repository layer for offline support and reduced network calls.
*   **Comprehensive Testing:**
    *   Expand unit tests for ViewModels and Repositories.
    *   Implement UI tests for Compose screens on each platform.
    *   Explore KMM testing frameworks for integration tests.
*   **Dependency Injection:** Integrate a KMM-compatible dependency injection framework (e.g., Koin, Kodein-DI) for better modularity and testability.
*   **CI/CD:** Set up Continuous Integration and Continuous Deployment pipelines for automated builds, tests, and releases.
*   **Platform-Specific Enhancements:** Add platform-specific features or UI refinements where necessary.
*   **User Authentication & Cart Functionality:** Implement actual e-commerce features like user accounts, shopping cart, and checkout.

---

This README provides a comprehensive overview of the KMM E-Commerce application.
