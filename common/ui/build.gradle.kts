import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.configureComposeDestinations
import com.mysty.chefbook.plugins.android.setNamespace

plugins {
  id("common-android-module")
  id("ksp-module")
}

setNamespace("com.mysty.chefbook.ui.common")
configureCompose(
  extraDependencies = listOf(
    Dependencies.Compose.uiTooling,
    Dependencies.Compost.ui,
    Dependencies.Compost.ui,
    Dependencies.Accompanist.pager,
    Dependencies.Accompanist.pagerIndicator,
    Dependencies.Images.coilCompose,
  )
)
configureComposeDestinations("ui")

dependencies {
  implementation(project(Modules.Common.core))
  implementation(project(Modules.Common.coreAndroid))
  implementation(project(Modules.Common.api))
  implementation(project(Modules.Common.navigation))
  implementation(project(Modules.Common.design))
}
