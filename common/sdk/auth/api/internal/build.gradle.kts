plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.network.ktor.client.auth)
    }
  }
}
