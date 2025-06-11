package plugins.module.multiplatform

import utils.gradle.libs
import org.gradle.kotlin.dsl.kotlin

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

      implementation(project(":common:core"))
      implementation(project(":common:design"))

      implementation(libs.di.koin.core)
      implementation(libs.decompose.core)
    }
  }
}
