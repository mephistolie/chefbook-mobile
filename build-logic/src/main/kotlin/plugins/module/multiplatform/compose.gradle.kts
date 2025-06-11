package plugins.module.multiplatform

import utils.gradle.libs
import org.gradle.kotlin.dsl.kotlin

plugins {
  id("plugins.module.multiplatform.base")
  id("org.jetbrains.compose")
  kotlin("plugin.compose")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":common:libs:logger"))
      implementation(project(":common:libs:coroutines"))
      implementation(project(":common:libs:utils"))

      implementation(compose.foundation)
      implementation(compose.runtime)
      implementation(compose.components.resources)
    }
  }
}
