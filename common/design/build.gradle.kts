plugins {
  alias(libs.plugins.module.multiplatform.compose)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.core)

      implementation(compose.material3)
      implementation(compose.components.uiToolingPreview)

      implementation(libs.coil.compose)
      implementation(libs.coil.network.ktor)
    }
  }
}

val namespace = "io.chefbook.design"
compose.resources.packageOfResClass = namespace
android.namespace = namespace
