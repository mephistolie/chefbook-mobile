plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = Project.compileSdk

    defaultConfig {
        applicationId = Project.applicationId
        minSdk = Project.minSdk
        targetSdk = Project.targetSdk
        versionCode = Project.version
        versionName = Project.versionName
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(BuildTypes.release) {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.version
    }

    kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_11}"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(Modules.Common.core))
    implementation(project(Modules.Common.coreUi))
    implementation(project(Modules.Common.design))
    implementation(project(Modules.Common.core))
    implementation(project(Modules.api))

    // Core
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appCompat)

    implementation(Dependencies.timber)

    // Compose
    implementation(Dependencies.Compose.compiler)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiTooling)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.animation)

    implementation(Dependencies.Accompanist.systemUiController)
    implementation(Dependencies.Accompanist.flowLayout)
    implementation(Dependencies.Accompanist.navigationMaterial)
    implementation(Dependencies.Accompanist.navigationAnimation)
    implementation(Dependencies.Accompanist.pager)
    implementation(Dependencies.Accompanist.pagerIndicator)

    implementation(Dependencies.Compost.core)

    implementation(Dependencies.AndroidX.composeActivity)
    implementation(Dependencies.AndroidX.composeViewModel)
    implementation(Dependencies.AndroidX.composeNavigation)

    implementation(Dependencies.composeReorderable)

    // DI
    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Koin.android)
    implementation(Dependencies.Koin.androidCompose)

    // Images
    implementation(Dependencies.Images.coil)
    implementation(Dependencies.Images.coilCompose)
    implementation(Dependencies.Images.imageCropper)
    implementation(Dependencies.Images.imageCompressor)

    implementation(Dependencies.SpongyCastle.prov)

    // Analytics
    implementation(Dependencies.Firebase.crashlytics)

    // Tests
    testImplementation(Dependencies.Tests.jUnit)
    androidTestImplementation(Dependencies.Tests.jUnitAndroid)
    androidTestImplementation(Dependencies.Tests.espresso)

    coreLibraryDesugaring(Dependencies.desugar)
}

kapt {
    correctErrorTypes = true
}
