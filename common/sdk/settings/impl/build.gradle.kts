plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.settings.api.internal)

      implementation(projects.common.sdk.core.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.settings.impl"
