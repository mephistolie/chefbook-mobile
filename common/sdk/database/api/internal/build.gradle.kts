val modulePackage = "io.chefbook.sdk.database.api.internal"
val databaseName = "ChefBookDatabase"

plugins {
  alias(libs.plugins.module.multiplatform.sdk)
  alias(libs.plugins.sqldelight)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.libs.logging)

      api(libs.androidx.datastore.core.okio)
    }
  }
}

sqldelight {
  databases {
    create(databaseName) {
      packageName.set(modulePackage)
    }
  }
}
