plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.medilinkapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.medilinkapp"
        minSdk = 25
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(libs.firebase.database.ktx)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Material Design 3
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation( "androidx.compose.material:material-icons-extended:1.5.4")

    // Google Sign-In
    implementation(libs.play.services.auth)

    // Google Fit API
    implementation("com.google.android.gms:play-services-fitness:21.2.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    // Firebase Authentication
    implementation ("com.google.firebase:firebase-auth-ktx:22.0.0")
    implementation("com.google.firebase:firebase-firestore")  // Firestore for doctor data


    // CameraX dependencies
    implementation ("androidx.camera:camera-camera2:1.2.0")
    implementation ("androidx.camera:camera-lifecycle:1.2.0")
    implementation ("androidx.camera:camera-view:1.2.0")
    implementation ("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    // Other dependencies...
    implementation ("androidx.core:core-splashscreen:1.0.0")
    implementation( "com.google.maps.android:maps-compose:6.1.0" )// Check for the latest version
    implementation ("com.google.android.gms:play-services-maps:19.1.0")
    implementation ("androidx.compose.ui:ui:1.7.8")

    implementation("io.coil-kt:coil-compose:2.2.2")

    implementation ( "com.google.accompanist:accompanist-systemuicontroller:0.30.1")




}
