import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.dynamic.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
}
android {
    namespace = "com.kotlin.androidsamples.mockresponseretrofit"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
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
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":app"))
    compileOnly(platform(libs.koin.bom))
    compileOnly(libs.androidx.core.ktx)
    compileOnly(libs.androidx.appcompat)
    compileOnly(libs.material)
    compileOnly(libs.androidx.activity)
    compileOnly(libs.androidx.constraintlayout)
    compileOnly(libs.okhttp)
    compileOnly(libs.retrofit)
    compileOnly(libs.gson)
    compileOnly(libs.kotlinx.coroutines.core)
    compileOnly(libs.kotlinx.coroutines.android)
    compileOnly(libs.logging.interceptor)
    compileOnly(libs.koin.core)
    compileOnly(libs.koin.android)
}