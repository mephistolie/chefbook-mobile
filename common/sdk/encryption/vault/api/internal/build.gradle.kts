plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.encryption.vault.api.external)
        api(projects.common.libs.encryption)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.encryption.vault.api.internal"
