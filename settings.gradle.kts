enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }
}

rootProject.name = "chefbook-mobile"

includeBuild("build-logic")

include(":android:app")

include(":common:umbrella")

include(":common:libs:models")
include(":common:libs:utils")
include(":common:libs:di")
include(":common:libs:coroutines")
include(":common:libs:exceptions")
include(":common:libs:logger")
include(":common:libs:encryption")

include(":common:sdk:core:api:internal")
include(":common:sdk:core:impl")

include(":common:sdk:network:api:internal")
include(":common:sdk:network:impl")

include(":common:sdk:database:api:internal")
include(":common:sdk:database:impl")

include(":common:sdk:file:api:internal")
include(":common:sdk:file:impl")

include(":common:sdk:settings:api:external")
include(":common:sdk:settings:api:internal")
include(":common:sdk:settings:impl")

include(":common:sdk:auth:api:external")
include(":common:sdk:auth:api:internal")
include(":common:sdk:auth:impl")

include(":common:sdk:profile:api:external")
include(":common:sdk:profile:api:internal")
include(":common:sdk:profile:impl")

include(":common:sdk:category:api:external")
include(":common:sdk:category:api:internal")
include(":common:sdk:category:impl")

include(":common:sdk:encryption:vault:api:external")
include(":common:sdk:encryption:vault:api:internal")
include(":common:sdk:encryption:vault:impl")

include(":common:sdk:encryption:recipe:api:internal")
include(":common:sdk:encryption:recipe:impl")

include(":common:sdk:recipe:core:api:external")
include(":common:sdk:recipe:core:api:internal")
include(":common:sdk:recipe:core:impl")

include(":common:sdk:recipe:crud:api:external")
include(":common:sdk:recipe:crud:api:internal")
include(":common:sdk:recipe:crud:impl")

include(":common:sdk:recipe:book:api:external")
include(":common:sdk:recipe:book:api:internal")
include(":common:sdk:recipe:book:impl")

include(":common:sdk:recipe:interaction:api:external")
include(":common:sdk:recipe:interaction:api:internal")
include(":common:sdk:recipe:interaction:impl")

include(":common:sdk:shopping-list:api:external")
include(":common:sdk:shopping-list:api:internal")
include(":common:sdk:shopping-list:impl")

include(":android:libs:mvi")

include(":android:core")
include(":android:design")
include(":android:ui")
include(":android:navigation")

include(":android:features:about")
include(":android:features:settings")
include(":android:features:auth")
include(":android:features:profile:control")
include(":android:features:profile:editing")
include(":android:features:encryption")
include(":android:features:recipe-book:dashboard")
include(":android:features:recipe-book:favourite")
include(":android:features:recipe-book:category")
include(":android:features:recipe-book:search")
include(":android:features:recipe:info")
include(":android:features:recipe:control")
include(":android:features:recipe:share")
include(":android:features:recipe:input")
include(":android:features:category")
include(":android:features:shopping-list:control")
include(":android:features:shopping-list:purchase-input")
