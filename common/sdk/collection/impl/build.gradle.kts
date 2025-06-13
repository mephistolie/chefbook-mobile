plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.collection.api.internal)

      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.profile.api.internal)
    }
  }
}

