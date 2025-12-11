@file:Suppress("DEPRECATION")

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.googleServices)
}

kotlin {
    // Suppress deprecation warnings for Preview annotation (KMP uses org.jetbrains namespace)
    compilerOptions {
        freeCompilerArgs.add("-Xwarning-level=DEPRECATION:disabled")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // Export KMPNotifier for iOS Swift access
            export(libs.kmpnotifier)
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Ktor - Android engine
            implementation(libs.ktor.client.okhttp)

            // Koin - Android
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Material 3 Adaptive (Window Size Classes)
            implementation(libs.compose.material3.adaptive)

            // Material Icons Core
            implementation(libs.compose.material.icons.core)

            // Navigation 3 (KMP-compatible)
            implementation(libs.navigation3.ui)

            // Ktor - HTTP Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Coil - Image Loading
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Koin - Dependency Injection
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Serialization & Utilities
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlin.datetime)
            implementation(libs.kotlinx.coroutines.core)

            // KMPNotifier (Push & Local Notifications)
            api(libs.kmpnotifier)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        iosMain.dependencies {
            // Ktor - iOS engine
            implementation(libs.ktor.client.darwin)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            // Ktor - JVM engine
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.adobe.aem_kmp_boilerplate"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.adobe.aem_kmp_boilerplate"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    
    // Firebase (using BOM for version management)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
}

compose.desktop {
    application {
        mainClass = "com.adobe.aem_kmp_boilerplate.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.adobe.aem_kmp_boilerplate"
            packageVersion = "1.0.0"
        }
    }
}
