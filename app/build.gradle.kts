plugins {
    alias(libs.plugins.androidApplication)
}

android {
<<<<<<< HEAD
    namespace = "com.example.quiz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quiz"
        minSdk = 26
=======
    namespace = "com.example.gkquizhindi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gkquizhindi"
        minSdk = 23
>>>>>>> ae55638b6c2f84b2b929a4dec0f612f8860f150a
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
<<<<<<< HEAD
    buildFeatures{
        viewBinding=true
    }
=======
>>>>>>> ae55638b6c2f84b2b929a4dec0f612f8860f150a
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}