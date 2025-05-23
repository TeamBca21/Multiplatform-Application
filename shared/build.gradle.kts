import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library) // For Android target
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui.tooling.preview)

                // Voyager Dependencies
                implementation("cafe.adriel.voyager:voyager-navigator:1.0.0")
                implementation("cafe.adriel.voyager:voyager-transitions:1.0.0")
                // implementation("cafe.adriel.voyager:voyager-androidx:1.0.0") // For viewModelDelegate, if needed later

                // Kamel Image Loading
                implementation("io.kamel:kamel-image:0.8.3")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(libs.compose.html.core)
                implementation(libs.kotlin.wrappers.react.dom) // If using React-based wrappers
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // Use a specific version, align with project's coroutines if possible
            }
        }
    }
}

android {
    namespace = "com.example.shared" // Replace with your desired namespace
    compileSdk = 34 // Example, use a version from libs.versions.toml or define explicitly
    defaultConfig {
        minSdk = 24 // Example, use a version from libs.versions.toml or define explicitly
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Opt-in for experimental Compose HTML library if needed
@OptIn(ExperimentalComposeLibrary::class)
compose.experimental {
    web.application {}
}
