import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.realm.db)
}

android {
    namespace = "com.example.cameracolorpickercompose"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.cameracolorpickercompose"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget =
                JvmTarget.fromTarget(JvmTarget.JVM_11.target)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

// noinspection UseTomlInstead
dependencies {

    // camerax
    val cameraxVersion = "1.5.3"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-compose:$cameraxVersion")
//    implementation("androidx.camera:camera-video:${cameraxVersion}")

    // lottie compose
    implementation("com.airbnb.android:lottie-compose:6.7.1")

    // nav compose
    implementation("androidx.navigation:navigation-compose:2.9.7")

    // perms
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // graphics
//    implementation("androidx.compose.ui:ui-graphics:1.10.0")
//    implementation(libs.androidx.ui.graphics) // it is already included in the proj

    // blur
//    val hazVersion = "1.7.1"
//    implementation("dev.chrisbanes.haze:haze:$hazVersion")
//    implementation("dev.chrisbanes.haze:haze-jetpack-compose:0.7.0")
//    implementation("dev.chrisbanes.haze:haze-materials:$hazVersion")

    // icons
    implementation("androidx.compose.material:material-icons-extended")

    // realm db
    implementation("io.realm.kotlin:library-base:3.0.0") // For a version of Realm Kotlin without sync features, install version 3.0.0+

    // DI
    val koinVersion = "4.1.1"
    implementation(platform("io.insert-koin:koin-bom:$koinVersion"))
    implementation("io.insert-koin:koin-android")
    implementation("io.insert-koin:koin-androidx-compose")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
}