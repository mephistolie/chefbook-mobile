plugins {
  alias(libs.plugins.module.multiplatform.base)
}

kotlin {

  sourceSets {
    iosMain.dependencies {

      // Libs

      api(projects.common.libs.models)
      api(projects.common.libs.utils)
      api(projects.common.libs.di)
      api(projects.common.libs.coroutines)
      api(projects.common.libs.exceptions)
      api(projects.common.libs.logger)

      // SDK

      api(projects.common.sdk.network.api.internal)
      api(projects.common.sdk.network.impl)

      api(projects.common.sdk.database.api.internal)
      api(projects.common.sdk.database.impl)

      api(projects.common.sdk.settings.api.external)
      api(projects.common.sdk.settings.api.internal)
      api(projects.common.sdk.settings.impl)

      api(projects.common.sdk.auth.api.external)
      api(projects.common.sdk.auth.api.internal)
      api(projects.common.sdk.auth.impl)

      api(projects.common.sdk.collection.api.external)
      api(projects.common.sdk.collection.api.internal)
      api(projects.common.sdk.collection.impl)
    }
  }
}

android.namespace = "io.chefbook.umbrella"

