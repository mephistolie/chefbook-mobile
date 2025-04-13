plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.profile.api.external)

      api(projects.common.libs.models)
      api(projects.common.sdk.core.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.profile.api.internal"
