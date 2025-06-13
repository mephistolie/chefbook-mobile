plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.profile.api.internal)

      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.file.api.internal)

      implementation(libs.androidx.datastore.core.okio)
    }
  }
}
