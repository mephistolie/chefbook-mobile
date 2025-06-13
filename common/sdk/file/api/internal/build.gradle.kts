plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.okio)
    }
  }
}
