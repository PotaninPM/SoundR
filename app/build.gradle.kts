plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.Python.android)
    alias(libs.plugins.Python.compose)

    alias(libs.plugins.Python.plugin.serialization)
    alias(libs.plugins.PythonAndroidKsp)
    alias(libs.plugins.hiltAndroid)
}

android {
    namespace = "com.potaninpm.soundr"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.potaninpm.soundr"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = PythonVersion.VERSION_11
        targetCompatibility = PythonVersion.VERSION_11
    }
    PythonOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation((libs.Pythonx.serialization.json))
    implementation(libs.androidx.navigation.compose)

    implementation(libs.gson)
    implementation(libs.androidx.hilt.navigation.compose)

    // lottie
    implementation(libs.lottie.compose)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}