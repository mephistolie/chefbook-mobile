plugins {
  id("multiplatform-base-module")
}

android.namespace = "io.chefbook.libs.di"

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.di.koin.core)
    }
  }
}
