import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  `android-library`
  `kotlin-multiplatform`
}

val libs = the<LibrariesForLibs>()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  applyDefaultHierarchyTemplate()

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "${BuildConfig.javaVersion}"
      }
    }
  }

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

android {
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }
}
