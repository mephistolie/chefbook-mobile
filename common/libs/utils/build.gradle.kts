plugins {
  id("multiplatform-base-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.common.libs.logger)
      }
    }
  }
}

android.namespace = "io.chefbook.libs.utils"
