import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.js)
    alias(libs.plugins.compose)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting { // Changed from wasmJsMain to jsMain
            dependencies {
                implementation(project(":shared"))
                implementation(libs.compose.web.core) // Use compose.web.core for Compose for Web
            }
        }
    }
}

// Opt-in for experimental Compose HTML library if needed
@OptIn(ExperimentalComposeLibrary::class)
compose.experimental {
    web.application {}
}

tasks.named("run", org.jetbrains.kotlin.gradle.tasks.KotlinJsRun::class.java) {
    outputFileName = "webApp.js" // Example output file name
}

// Optional: Configure webpack tasks if needed (e.g., for custom dev server settings)
// tasks.withType<org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack> {
//     // ...
// }
