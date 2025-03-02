plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.stydyrussian"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.stydyrussian"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    val nav_version = "2.7.7"
    val room_version = "2.6.1"
    val viewmodel_version = "2.8.1"
    val dagger_version = "2.51.1"

    // Room dependencies
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Kotlin extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")


    // Dagger Hilt
    implementation ("com.google.dagger:hilt-android:$dagger_version")
    kapt ("com.google.dagger:hilt-compiler:$dagger_version")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$viewmodel_version")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$viewmodel_version")

    //Worker
    implementation ("androidx.work:work-runtime-ktx:2.9.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

kapt {
    correctErrorTypes = true
}