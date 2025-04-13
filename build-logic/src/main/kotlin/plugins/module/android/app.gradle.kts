package plugins.module.android

import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project
import utils.android.configureAndroidCommon
import utils.android.configureCompose
import utils.gradle.BuildType
import utils.gradle.libs
import utils.kotlin.configureKsp
import utils.kotlin.enableExplicitBackingFields

plugins {
  id("com.android.application")
  kotlin("plugin.compose")
}

configureAndroidCommon()
configureCompose()

configureKsp()
enableExplicitBackingFields()

android {
  defaultConfig.targetSdk = libs.versions.targetSdk.get().toInt()

  packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

  buildTypes.getByName(BuildType.RELEASE) {
    isDebuggable = false
    isMinifyEnabled = true
    isShrinkResources = true
    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
  }
  buildFeatures.buildConfig = true
}

dependencies {
  implementation(libs.androidx.compose.animation)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.lifecycle.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.composeDestinations.core)

  implementation(libs.di.koin.core)
  implementation(libs.di.koin.android)
  implementation(libs.di.koin.compose)

  implementation(libs.tinkAndroid)

  implementation(libs.network.ktor.client.okhttp)

  implementation(libs.coil)
  implementation(libs.imageCropper)

  implementation(project(":common:libs:logger"))
  implementation(project(":common:libs:coroutines"))
}
