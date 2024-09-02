plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android.gradle.plugin)
}

android {
    namespace = "com.apps.omdbmovie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.apps.omdbmovie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.apps.omdbmovie.CustomTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.bundles.network)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.paging)
    implementation(libs.bundles.coroutines)
    kapt(libs.androidx.room.compiler)
    implementation(libs.bundles.room)
    implementation(libs.androidx.room.paging) // Add this line
    implementation(libs.coil.compose)
    implementation(libs.accompanist.placeholder.material)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.kotlinx.coroutines.core)
    testImplementation (libs.mockk)
    testImplementation (libs.junit.jupiter.api)
    testImplementation (libs.junit.jupiter.engine)
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.androidx.paging.common)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.turbine)
    testImplementation (libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing.v244)
    kaptAndroidTest(libs.hilt.android.compiler)



}