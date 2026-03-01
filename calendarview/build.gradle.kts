import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.dynamic.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace = "com.android.playground.calendarview"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
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
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}