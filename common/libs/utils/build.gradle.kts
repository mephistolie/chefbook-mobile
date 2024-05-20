plugins {
  id("multiplatform-base-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.libs.logger)
    }
  }
}

android.namespace = "io.chefbook.libs.utils"
