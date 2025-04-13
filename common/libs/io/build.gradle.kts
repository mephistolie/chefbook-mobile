plugins {
  alias(libs.plugins.module.multiplatform.base)
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.okio)

      implementation(libs.di.koin.core)
    }
  }
}

android.namespace = "io.chefbook.libs.io"
