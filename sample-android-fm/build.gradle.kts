plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

dependencies {
    implementation(project(":modo"))
    implementation(project(":modo-render-android-fm"))
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0-beta01")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.github.terrakok.modo.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}