import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
  id("com.android.application")
  kotlin("android")
  id("com.google.devtools.ksp")
}

val libs = the<LibrariesForLibs>()

android {
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = BuildConfig.javaVersion
    targetCompatibility = BuildConfig.javaVersion
  }
  kotlinOptions {
    jvmTarget = "${BuildConfig.javaVersion}"
  }
  buildFeatures {
    buildConfig = true
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
}

kotlinExtension.sourceSets.forEach { sourceSet ->
  when (sourceSet.name) {
    "debug" -> sourceSet.kotlin.srcDir("build/generated/ksp/debug/kotlin")
    "release" -> sourceSet.kotlin.srcDir("build/generated/ksp/release/kotlin")
  }
}

dependencies {
  implementation(libs.androidx.coreKtx)
  implementation(libs.androidx.appCompat)

  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.animation)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.composeDestinations.core)

  implementation(libs.di.koin.core)
  implementation(libs.di.koin.android)
  implementation(libs.di.koin.compose)

  implementation(libs.spongycastle.prov)

  implementation(libs.network.ktor.client.okhttp)

  implementation(libs.imageCropper)

  implementation(project(":common:libs:logger"))
  implementation(project(":common:libs:coroutines"))
}
