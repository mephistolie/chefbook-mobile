plugins {
  alias(libs.plugins.module.multiplatform.feature)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.libs.di)

      implementation(projects.common.sdk.settings.api.external)
      implementation(projects.common.sdk.auth.api.external)

      implementation(projects.common.features.auth)
      implementation(projects.common.features.profile.control)

      implementation(libs.decompose.core)
      implementation(libs.decompose.extensions.compose)
      implementation(libs.di.koin.core)
      implementation(libs.di.koin.compose)
    }
  }
}
