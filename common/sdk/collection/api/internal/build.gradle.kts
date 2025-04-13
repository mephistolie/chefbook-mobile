plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.collection.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.collection.api.internal"
