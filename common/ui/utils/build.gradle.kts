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

val namespace = "io.chefbook.utils"

compose.resources {
  publicResClass = true
  packageOfResClass = namespace
}

android.namespace = namespace
