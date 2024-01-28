plugins {
  id("multiplatform-base-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {}
  }
}

android.namespace = "io.chefbook.sdk.core.api.internal"
