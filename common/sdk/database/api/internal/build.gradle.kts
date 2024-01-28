val modulePackage = "io.chefbook.sdk.database.api.internal"
val databaseName = "ChefBookDatabase"

plugins {
  id("multiplatform-base-module")
  alias(libs.plugins.sqldelight)
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.libs.logger)
      }
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
