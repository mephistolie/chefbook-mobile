plugins {
  alias(libs.plugins.module.multiplatform.feature)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.libs.di)

      implementation(projects.common.features.auth)
      implementation(projects.common.features.profile)

      implementation(libs.decompose.core)
      implementation(libs.di.koin.core)
    }
  }
}
