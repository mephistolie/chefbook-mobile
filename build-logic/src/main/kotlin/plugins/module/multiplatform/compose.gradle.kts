package plugins.module.multiplatform

import org.gradle.kotlin.dsl.kotlin
import utils.android.defaultKmpNamespace

plugins {
  id("plugins.module.multiplatform.base")
  id("org.jetbrains.compose")
  kotlin("plugin.compose")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":common:libs:logging"))
      implementation(project(":common:libs:coroutines"))
      implementation(project(":common:libs:utils"))

      implementation(compose.foundation)
      implementation(compose.runtime)
      implementation(compose.components.resources)
    }
  }

  androidLibrary {
    experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
  }
}

compose.resources.packageOfResClass = project.defaultKmpNamespace
