pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "KMMProject"
include(":android")
include(":shared")
// include(":iosApp") // There is no iosApp yet
// include(":webApp") // There is no webApp yet

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")