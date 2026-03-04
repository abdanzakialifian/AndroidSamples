pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "AndroidPlayground"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":app_launcher")
include(":mock_okhttp")
include(":chart")
include(":webview")
include(":calendar")
include(":wear")
include(":core:network")
include(":core:common")
include(":core:ui")
include(":phone")
