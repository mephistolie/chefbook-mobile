plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.file.api.internal)
    }
    androidMain.dependencies {
      implementation(libs.compressor)
    }
  }
}
