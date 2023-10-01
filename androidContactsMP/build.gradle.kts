import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.realityexpander.contactscomposemultiplatform.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.realityexpander.contactscomposemultiplatform.android"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        // Deals with build error, todo: remove when fixed after upgrading to compileSdk/targetSdk 34
        // https://issuetracker.google.com/issues/295457468
        configurations.all {
            resolutionStrategy {
                force("androidx.emoji2:emoji2-views-helper:1.3.0")
                force("androidx.emoji2:emoji2:1.3.0")
            }
        }
    }
    @Suppress("UnstableApiUsage")
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8" // for kotlin 1.8.22
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // is this needed? Does `kotlinOptions jvmTarget = "17"` do the same thing?
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.5.2")
    implementation("androidx.compose.ui:ui-tooling:1.5.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.2")
    implementation("androidx.compose.foundation:foundation:1.5.2")
    implementation("androidx.compose.material:material:1.5.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation("com.google.maps.android:maps-compose:2.15.0")
}
