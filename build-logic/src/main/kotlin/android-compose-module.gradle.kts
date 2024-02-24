import gradle.kotlin.dsl.accessors._3d3b47942ab04f2cc9c29cffc95a2b60.implementation
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

  implementation(libs.androidx.lifecycle.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.di.koin.compose)

  implementation(libs.compost.core)
}
