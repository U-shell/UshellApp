plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"
    id("kotlin-kapt")
}

android {
    namespace = "ru.ushell.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.ushell.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 6
        versionName = "5.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "36.0.0"
//    buildToolsVersion '33.0.0'
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.constraintlayout.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.accompanist.themeadapter.material3)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation (libs.androidx.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    ksp(libs.androidx.room.room.compiler)
    ksp(libs.dagger.compiler)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.materialWindow)

    implementation(libs.accompanist.swiperefresh)
    implementation(libs.androidx.constraintlayout.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)

    implementation(libs.core)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.espresso.core)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.logging.interceptor)
    implementation(libs.hilt.android)
}
