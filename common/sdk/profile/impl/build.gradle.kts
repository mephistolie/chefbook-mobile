plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.profile.api.internal)

      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.file.api.internal)

      implementation(projects.common.libs.io)

      implementation(libs.androidx.datastore.core.okio)
    }
  }
}

android.namespace = "io.chefbook.sdk.profile.impl"
