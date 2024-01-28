import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("android-base-module")
}

val libs = the<LibrariesForLibs>()

android {
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
}

dependencies {
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.material)

  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.di.koin.compose)

  implementation(libs.compost.core)
}
