package plugins.module.multiplatform

import utils.android.configureAndroidSdkVersions
import utils.gradle.BuildConfig
import utils.gradle.libs
import utils.kotlin.enableExplicitBackingFields

plugins {
  id("com.android.library")
  id("kotlin-multiplatform")
}

enableExplicitBackingFields()

configureAndroidSdkVersions()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  applyDefaultHierarchyTemplate()

  jvmToolchain(BuildConfig.JAVA_VERSION)

  androidTarget()

  val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())

  iosTargets.forEach {
    it.binaries.framework {
      baseName = "common"
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlin.coroutines.core)
      api(libs.kotlin.datetime)
    }
  }
}
