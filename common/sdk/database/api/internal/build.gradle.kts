val modulePackage = "io.chefbook.sdk.database.api.internal"
val databaseName = "ChefBookDatabase"

plugins {
  alias(libs.plugins.module.multiplatform.sdk)
  alias(libs.plugins.sqldelight)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.libs.logger)

      api(libs.androidx.datastore.core.okio)
    }
  }
}

android.namespace = modulePackage

sqldelight {
  databases {
    create(databaseName) {
      packageName.set(modulePackage)
    }
  }
}
