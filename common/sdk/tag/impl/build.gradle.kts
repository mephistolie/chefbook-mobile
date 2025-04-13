plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.tag.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.tag.impl"
