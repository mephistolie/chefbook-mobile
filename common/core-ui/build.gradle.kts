plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mysty.chefbook.core.ui"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.version
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(Modules.Common.core))

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.Images.zxing)

    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.animation)

    implementation(Dependencies.composeShimmer)

    implementation(Dependencies.Network.okHttp)
}
