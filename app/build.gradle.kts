plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.finance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.finance"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


dependencies {
    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito:mockito-inline:5.8.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("org.hamcrest:hamcrest:2.2")

    // Android Test dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")

    implementation ("com.google.android.material:material:1.7.0")  // Update to the latest version
    implementation ("androidx.appcompat:appcompat:1.6.0")  // Update to the latest version

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")  // Retrofit library
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")  // Gson converter for Retrofit
    testImplementation ("org.mockito:mockito-inline:5.8.0")




}

