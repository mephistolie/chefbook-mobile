plugins {
  alias(libs.plugins.module.multiplatform.base)
}

android.namespace = "io.chefbook.libs.di"

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.di.koin.core)
    }
  }
}
