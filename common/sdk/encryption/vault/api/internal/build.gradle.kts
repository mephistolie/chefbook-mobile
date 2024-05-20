plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.encryption.vault.api.external)
      api(projects.common.libs.encryption)
    }
  }
}

android.namespace = "io.chefbook.sdk.encryption.vault.api.internal"
