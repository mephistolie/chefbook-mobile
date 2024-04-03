plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.encryption.vault.api.internal)

      implementation(projects.common.sdk.profile.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.encryption.vault.impl"
