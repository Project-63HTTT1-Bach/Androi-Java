plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
<<<<<<< HEAD:MyApplication/app/build.gradle.kts
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
=======
    namespace = "com.example.exampractice"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.exampractice"
        minSdk = 24
>>>>>>> df05a7a05c9586c55c67161ee8bf5ab7960d6bea:app/build.gradle.kts
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
<<<<<<< HEAD:MyApplication/app/build.gradle.kts
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
=======
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
>>>>>>> df05a7a05c9586c55c67161ee8bf5ab7960d6bea:app/build.gradle.kts
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}