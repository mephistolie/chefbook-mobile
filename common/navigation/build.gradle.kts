import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.configureComposeDestinations
import com.mysty.chefbook.plugins.android.setNamespace

plugins {
  id("common-android-module")
  id("ksp-module")
}

setNamespace("com.mysty.chefbook.navigation")
configureCompose()
configureComposeDestinations()

dependencies {
  implementation(project(Modules.Common.core))
  implementation(project(Modules.Common.coreAndroid))
  implementation(project(Modules.Common.design))
  implementation(project(Modules.Common.api))
}
