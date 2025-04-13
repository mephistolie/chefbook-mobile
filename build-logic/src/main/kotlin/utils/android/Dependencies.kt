package utils.android

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.DependencyHandlerScope
import utils.gradle.implementation

fun DependencyHandlerScope.composeBaseDependencies(libs: LibrariesForLibs) {
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.material)
}
