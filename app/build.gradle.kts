plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)

    // Gradle plugin de los servicios de Google
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.isaac.ehub"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.isaac.ehub"
        minSdk = 28
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
    buildFeatures {
        buildConfig = true
        viewBinding = true  // ViewBinding (reemplazo a 'findViewById()')
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // AppCompat
    implementation(libs.appcompat)
    implementation(libs.androidx.legacy.support.v4)
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Constraint
    implementation(libs.constraintlayout)
    // Material
    implementation(libs.material)
    // Fragment
    implementation(libs.androidx.fragment)
    // Activity
    implementation(libs.activity)
    // LifeCycle (LiveData)
    implementation(libs.androidx.lifecycle.livedata)
    // Lifecycle (Runtime)
    implementation(libs.androidx.lifecycle.lifecycle.runtime)
    // LifeCycle(ViewModel)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    // DaggerHilt
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)
    // Navigation Component
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.navigation.ui)
    // Firebase BoM + Auth + Store
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    // Firebase Credential Manager (Google)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    // Glide
    implementation(libs.glide)

    // Lottie
    //implementation(libs.lottie)

    // Retrofit
    // implementation(libs.retrofit)

    // Gson
    // implementation(libs.gson)
    // GsonConverter
    // implementation(libs.converter.gson)

}