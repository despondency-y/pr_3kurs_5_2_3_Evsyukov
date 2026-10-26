plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp") version "2.3.6"
}

android {
    namespace = "com.example.pr_3kurs_5_2_3_evsyukov"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.pr_3kurs_5_2_3_evsyukov"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation ("com.google.code.gson:gson:2.8.5")
    //implementation("androidx.room:room-runtime:2.5.0")
    implementation ("com.android.volley:volley:1.2.1")
    //implementation("androidx.room:room-ktx:2.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //ksp("androidx.room:room-compiler:2.5.0")

    implementation("androidx.room:room-runtime:2.8.5")
    implementation("androidx.room:room-ktx:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")


    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}