plugins {
  alias(libs.plugins.module.multiplatform.base)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.network.ktor.client.serialization)
    }
  }
}

android.namespace = "io.chefbook.libs.models"
