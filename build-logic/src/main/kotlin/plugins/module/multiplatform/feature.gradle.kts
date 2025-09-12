package plugins.module.multiplatform

import utils.gradle.libs
import org.gradle.kotlin.dsl.kotlin
import utils.android.defaultKmpNamespace

plugins {
  id("plugins.module.multiplatform.compose")
  id("org.jetbrains.compose")
  kotlin("plugin.compose")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":common:libs:models"))
      implementation(project(":common:libs:di"))
      implementation(project(":common:libs:exceptions"))
      implementation(project(":common:libs:mvi"))

      implementation(project(":common:ui:utils"))
      implementation(project(":common:ui:design"))
      implementation(project(":common:ui:common"))

      implementation(libs.di.koin.core)
      implementation(libs.decompose.core)
    }
  }
}
