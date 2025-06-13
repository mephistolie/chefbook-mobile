plugins {
  alias(libs.plugins.module.multiplatform.compose)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.ui.utils)

      implementation(compose.material3)
      implementation(compose.components.uiToolingPreview)

      implementation(libs.coil.compose)
      implementation(libs.coil.network.ktor)
    }
  }
}
