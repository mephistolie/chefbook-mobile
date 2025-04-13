plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.libs.models)
    }
  }
}

android.namespace = "io.chefbook.sdk.settings.api.external"
