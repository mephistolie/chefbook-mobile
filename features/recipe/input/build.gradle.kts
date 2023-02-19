import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.configureComposeDestinations
import com.mysty.chefbook.plugins.android.setFeatureNamespace

plugins {
  id("feature-module")
  id("ksp-module")
}

setFeatureNamespace("recipe.input")
configureCompose(
  extraDependencies = listOf(
    Dependencies.Compose.material,
    Dependencies.Compose.uiTooling,
    Dependencies.Accompanist.flowLayout,
    Dependencies.Accompanist.pager,
    Dependencies.Accompanist.pagerIndicator,
    Dependencies.composeReorderable,
  )
)
configureComposeDestinations(moduleName = "recipe-input")

dependencies {
  implementation(Dependencies.Images.imageCropper)
}
