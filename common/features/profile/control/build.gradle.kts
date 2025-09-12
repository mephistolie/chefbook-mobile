plugins {
  alias(libs.plugins.module.multiplatform.feature)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.sdk.profile.api.external)
      implementation(projects.common.sdk.auth.api.external)

      implementation(compose.material3)

      implementation(libs.decompose.core)
      implementation(libs.di.koin.core)
    }
  }
}
