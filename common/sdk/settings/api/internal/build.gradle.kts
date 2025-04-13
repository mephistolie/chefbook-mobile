plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.settings.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.settings.api.internal"
