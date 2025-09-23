plugins {
    alias(libs.plugins.android.dynamic.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
}
android {
    namespace = "com.kotlin.androidsamples.calendarview"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

dependencies {
    implementation(project(":app"))
    compileOnly(platform(libs.compose.bom))
    compileOnly(libs.androidx.core.ktx)
    compileOnly(libs.androidx.appcompat)
    compileOnly(libs.material)
    compileOnly(libs.androidx.activity)
    compileOnly(libs.androidx.constraintlayout)
    compileOnly(libs.compose.animation)
    compileOnly(libs.compose.animation.core)
    compileOnly(libs.compose.animation.graphics)
    compileOnly(libs.compose.foundation)
    compileOnly(libs.compose.foundation.layout)
    compileOnly(libs.compose.material3)
    compileOnly(libs.compose.runtime)
    compileOnly(libs.compose.ui)
    compileOnly(libs.compose.ui.tooling.preview)
    compileOnly(libs.compose.activity)
    compileOnly(libs.compose.navigation)
    debugCompileOnly(libs.compose.ui.tooling)
}