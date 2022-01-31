import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("com.google.protobuf") version "0.8.18"
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.cactusknights.chefbook"
        minSdk = 21
        targetSdk = 31
        versionCode = 41
        versionName = "4.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-beta03"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Core
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0-alpha02")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    // Views
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.4.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Dependencies Injection
    val hiltVersion = "2.40"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    // Navigation Component
    val navVersion = "2.3.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // DataStore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.17.2")
    implementation("androidx.preference:preference-ktx:1.2.0")

    // Room
    val roomVersion = "2.4.0-beta01"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Network
    val okHttpVersion = "4.9.0"
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Google Play
    implementation("com.google.android.play:core-ktx:1.8.1")
    implementation("com.android.billingclient:billing:4.0.0")

    // Firebase
    implementation("com.google.firebase:firebase-ads:20.5.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.2.7")
    implementation("com.google.firebase:firebase-analytics-ktx:20.0.2")

    // Images
    implementation("io.coil-kt:coil:1.4.0")
    implementation("com.github.CanHub:Android-Image-Cropper:4.0.0")
    implementation("id.zelory:compressor:3.0.1")

    // QR
    val zxingVersion = "3.4.1"
    implementation("com.google.zxing:core:$zxingVersion")

    // Shimmer Effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
kapt {
    correctErrorTypes = true
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.9.2:osx-x86_64"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}