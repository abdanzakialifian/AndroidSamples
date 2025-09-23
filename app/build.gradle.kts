plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.kotlin.androidsamples"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kotlin.androidsamples"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
    dynamicFeatures += setOf(":dynamicapplauncher", ":mockresponseretrofit", ":androidchart", ":webviewcallback", ":calendarview")
}

dependencies {
    // This dependency is downloaded from the Google’s Maven repository.
    // Make sure you also include that repository in your project's build.gradle file.
    implementation(libs.feature.delivery)
    // For Kotlin users, also import the Kotlin extensions library for Play Feature Delivery:
    implementation(libs.feature.delivery.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(platform(libs.koin.bom))
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.logging.interceptor)
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.animation)
    implementation(libs.compose.animation.core)
    implementation(libs.compose.animation.graphics)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)
    debugImplementation(libs.compose.ui.tooling)
}