plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.squareup.sqldelight")
    kotlin("native.cocoapods")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).all {
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export("dev.icerock.moko:mvvm-core:0.16.1")
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

//    cocoapods {
//        pod("GoogleMaps") {
//            version = "8.2.0"
//        }
//    }

    cocoapods {
        // Required properties
        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
//        framework {
//          baseName = "shared"
//        }

        ios.deploymentTarget = "14.0"

        pod("GoogleMaps") {
            version = "8.2.0"
        }
//
//        // Optional properties
//        // Configure the Pod name here instead of changing the Gradle project name
//        name = "MyCocoaPod"
//
//        framework {
//            // Required properties
//            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
//            baseName = "MyFramework"
//
//            // Optional properties
//            // Specify the framework linking type. It's dynamic by default.
//            isStatic = false
//            // Dependency export
//            export(project(":anotherKMMModule"))
//            transitiveExport = false // This is default.
//            // Bitcode embedding
//            embedBitcode(BITCODE)
//        }
//
//        // Maps custom Xcode configuration to NativeBuildType
//        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
//        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation("com.squareup.sqldelight:runtime:1.5.5")
                implementation("com.squareup.sqldelight:coroutines-extensions:1.5.5")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:1.5.5")
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.activity:activity-compose:1.7.2")
                implementation("com.google.android.gms:play-services-maps:18.1.0")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:1.5.5")
                implementation("com.google.android.gms:play-services-maps:18.1.0")
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.realityexpander.contactscomposemultiplatform"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    database("ContactDatabase") {
        packageName = "com.realityexpander.contactscomposemultiplatform.database"
        sourceFolders = listOf("sqldelight")
    }
}

dependencies {
    implementation("androidx.core:core:1.10.1")
    commonMainApi("dev.icerock.moko:mvvm-core:0.16.1")
    commonMainApi("dev.icerock.moko:mvvm-compose:0.16.1")
    commonMainApi("dev.icerock.moko:mvvm-flow:0.16.1")
    commonMainApi("dev.icerock.moko:mvvm-flow-compose:0.16.1")
    implementation("com.google.maps.android:maps-compose:2.15.0")
}

val myAttribute = Attribute.of("myOwnAttribute", String::class.java)
// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("podDebugFrameworkIosArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "debug-arm64")
    }
}
configurations.named("releaseFrameworkIosArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "release-arm64")
    }
}
configurations.named("podDebugFrameworkIosFat").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "debug-fat2")
    }
}
configurations.named("releaseFrameworkIosFat").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "release-fat2")
    }
}
configurations.named("podDebugFrameworkIosSimulatorArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "debug-simulator-arm64")
    }
}
configurations.named("releaseFrameworkIosSimulatorArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "release-simulator-arm64")
    }
}
configurations.named("podDebugFrameworkIosX64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "debug-simulator-x64")
    }
}
configurations.named("releaseFrameworkIosX64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "release-simulator-x64")
    }
}
