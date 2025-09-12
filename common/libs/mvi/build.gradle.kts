plugins {
  alias(libs.plugins.module.multiplatform.base)
}

kotlin {
  sourceSets.commonMain.dependencies {
    implementation(libs.decompose.core)
    implementation(libs.di.koin.core)
  }
}
