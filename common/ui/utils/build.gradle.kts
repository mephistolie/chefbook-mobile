plugins {
  alias(libs.plugins.module.multiplatform.compose)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.shimmer)
    }
  }
}

compose.resources.publicResClass = true
