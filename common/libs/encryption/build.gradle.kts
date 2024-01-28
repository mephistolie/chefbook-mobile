plugins {
  id("multiplatform-base-module")
}

kotlin {
  sourceSets {
    val androidMain by getting {
      dependencies {
        implementation(libs.spongycastle.core)
        implementation(libs.spongycastle.prov)
      }
    }
  }
}

android.namespace = "io.chefbook.libs.encryption"

