plugins {
  alias(libs.plugins.module.multiplatform.feature)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.libs.di)

      implementation(libs.decompose.core)
      implementation(libs.di.koin.core)
    }
  }
}

android.namespace = "io.chefbook.features.profile"
