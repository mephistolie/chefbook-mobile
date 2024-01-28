plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.encryption.recipe.api.internal)

        implementation(projects.common.sdk.profile.api.internal)
        implementation(projects.common.sdk.recipe.core.api.internal)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.spongycastle.core)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.encryption.recipe.impl"
