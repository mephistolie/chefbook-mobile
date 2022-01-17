plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.cactusknights.chefbook"
        minSdk = 21
        targetSdk = 31
        versionCode = 41
        versionName = "3.2"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
//            isMinifyEnabled = true
//            isShrinkResources = true
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-beta03"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    val kotlinVersion = "1.5.31"
    val composeVersion = "1.0.5"

    val navVersion = "2.3.5"
    val hiltVersion = "2.40"
    val roomVersion = "2.4.0-beta01"
    val okHttpVersion = "4.9.0"
    val retrofitVersion = "2.9.0"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.4.0")
    implementation("androidx.work:work-runtime:2.7.1")
    implementation("com.google.android.material:material:1.6.0-alpha01")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.google.android.gms:play-services-auth:20.0.1")
    implementation("com.google.android.play:core:1.10.2")
    implementation("com.google.android.play:core-ktx:1.8.1")
    implementation("com.google.firebase:firebase-ads:20.5.0")
    implementation("com.android.billingclient:billing:4.0.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.2.6")
    implementation("com.google.firebase:firebase-analytics-ktx:20.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")

    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.core:core-splashscreen:1.0.0-alpha02")

    implementation("io.coil-kt:coil:1.4.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    implementation("com.github.CanHub:Android-Image-Cropper:4.0.0")

    implementation("id.zelory:compressor:3.0.1")

    implementation("com.google.zxing:core:3.4.1")
}

kapt {
    correctErrorTypes = true
}