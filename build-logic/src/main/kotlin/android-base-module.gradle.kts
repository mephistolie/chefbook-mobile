import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("com.android.library")
  id("kotlin-parcelize")
  kotlin("android")
}

val libs = the<LibrariesForLibs>()

android {
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }
  compileOptions {
    sourceCompatibility = BuildConfig.javaVersion
    targetCompatibility = BuildConfig.javaVersion
  }
  kotlinOptions {
    jvmTarget = "${BuildConfig.javaVersion}"
  }
}

dependencies {
  implementation(libs.androidx.coreKtx)
  implementation(libs.androidx.appCompat)

  implementation(project(":common:libs:logger"))
  implementation(project(":common:libs:coroutines"))
  implementation(project(":common:libs:utils"))
}
