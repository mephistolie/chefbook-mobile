plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.file.api.internal)

      implementation(projects.common.libs.io)
    }
    androidMain.dependencies {
      implementation(libs.compressor)
    }
  }
}

android.namespace = "io.chefbook.sdk.file.impl"
