
import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.setNamespace

plugins {
  id("common-android-module")
}

setNamespace("com.mysty.chefbook.design")
configureCompose(
  extraDependencies = listOf(
    Dependencies.Compose.material,
    Dependencies.Compose.uiTooling,
    Dependencies.Compost.ui,
    Dependencies.Images.coilCompose,
  )
)

dependencies {
  implementation(project(Modules.Common.core))
  implementation(project(Modules.Common.coreAndroid))
}
