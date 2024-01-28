plugins {
  id("multiplatform-base-module")
}

android.namespace = "io.chefbook.libs.di"

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.di.koin.core)
      }
    }
  }
}
