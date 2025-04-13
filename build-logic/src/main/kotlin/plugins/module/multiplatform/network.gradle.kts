package plugins.module.multiplatform

import utils.gradle.libs

plugins {
  id("plugins.module.multiplatform.sdk")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.network.ktor.client.core)
    }
  }
}
