plugins {
  alias(libs.plugins.module.multiplatform.base)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.libs.logger)
    }
  }
}

android.namespace = "io.chefbook.libs.utils"
