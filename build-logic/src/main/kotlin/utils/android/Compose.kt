package utils.android

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.Companion.IntrinsicRemember
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.Companion.OptimizeNonSkippingGroups
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.Companion.StrongSkipping
import utils.android.composeBaseDependencies
import utils.gradle.androidTestImplementation
import utils.gradle.implementation
import utils.gradle.libs

internal fun Project.configureCompose() {
  apply(plugin = libs.plugins.compose.compiler.get().pluginId)

  androidExtension?.configureCompose()

  dependencies {
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(composeBom)
    androidTestImplementation(composeBom)

    composeBaseDependencies(libs)
  }

  extensions.configure<ComposeCompilerGradlePluginExtension> {
    featureFlags = setOf(
      StrongSkipping,
      IntrinsicRemember,
      OptimizeNonSkippingGroups
    )
  }
}


private fun CommonExtension<*, *, *, *, *, *>.configureCompose() {
  buildFeatures {
    compose = true
  }
}
