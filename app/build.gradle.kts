plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.assignment1"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.assignment1"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // Provide paths to your keystore file and credentials here
            storeFile = file("path/to/your/keystore.jks") // Replace with actual keystore path
            storePassword = "yourKeystorePassword"      // Replace with actual password
            keyAlias = "myAppReleaseKey"                   // Replace with actual key alias
            keyPassword = "yourKeyPassword"             // Replace with actual key password
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release") // Corrected reference here
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation (libs.material)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.ion)
    implementation("org.mindrot:jbcrypt:0.4")

}
