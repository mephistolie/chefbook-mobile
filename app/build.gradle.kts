import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    kotlin("plugin.serialization") version "1.7.0"
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
    id("com.google.protobuf") version "0.8.18"
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.cactusknights.chefbook"
        minSdk = 21
        targetSdk = 33
        versionCode = 41
        versionName = "4.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {

    // Core
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")

    // Compose
    val composeVersion = "1.2.1"
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.compiler:compiler:1.3.2")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")

    // Navigation
    val navVersion = "2.5.1"
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Accompanist
    val accompanistVersion = "0.28.0"
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-material:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanistVersion")

    // Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("com.github.mephistolie:compost:0.0.3")

    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.2")

    // Dependency Injection
    val hiltVersion = "2.42"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // DataStore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.17.2")
    implementation("androidx.preference:preference-ktx:1.2.0")

    // Room
    val roomVersion = "2.4.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Network
    val okHttpVersion = "4.9.0"
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // Images
    implementation("io.coil-kt:coil:2.2.2")
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("com.github.CanHub:Android-Image-Cropper:4.0.0")
    implementation("id.zelory:compressor:3.0.1")

    // QR
    val zxingVersion = "3.4.1"
    implementation("com.google.zxing:core:$zxingVersion")

    // Google Play
    implementation("com.google.android.play:core-ktx:1.8.1")
    implementation("com.android.billingclient:billing:5.0.0")

    // Firebase
    implementation("com.google.firebase:firebase-ads:21.1.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.2.12")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
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