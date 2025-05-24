plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.collection.api.external)

      api(projects.common.sdk.network.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.collection.api.internal"
