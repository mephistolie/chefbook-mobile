package plugins.module.multiplatform

import utils.gradle.libs
import org.gradle.kotlin.dsl.kotlin

plugins {
  id("plugins.module.multiplatform.base")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":common:libs:models"))
      implementation(project(":common:libs:utils"))
      implementation(project(":common:libs:di"))
      implementation(project(":common:libs:coroutines"))
      implementation(project(":common:libs:exceptions"))
      implementation(project(":common:libs:logger"))

      implementation(libs.di.koin.core)
      implementation(libs.network.ktor.client.serialization)
    }
  }
}
