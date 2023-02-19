import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.setNamespace

plugins {
  id("common-android-module")
}

setNamespace("com.mysty.chefbook.core.android")
configureCompose(
  extraDependencies = listOf(
    Dependencies.Compose.animation,
    Dependencies.composeShimmer,
  )
)

dependencies {
  implementation(project(Modules.Common.core))

  implementation(Dependencies.Images.zxing)

  implementation(Dependencies.Network.okHttp)
}
