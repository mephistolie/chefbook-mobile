package plugins.module.multiplatform

import utils.android.kmpAndroidTarget
import utils.gradle.BuildConfig
import utils.gradle.libs
import utils.kotlin.enableExplicitBackingFields

plugins {
  id("kotlin-multiplatform")
  id("com.android.kotlin.multiplatform.library")
}

enableExplicitBackingFields()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  applyDefaultHierarchyTemplate()

  jvmToolchain(BuildConfig.JAVA_VERSION)

  kmpAndroidTarget()

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.coroutines.core)
      api(libs.kotlinx.datetime)
    }
  }
}
