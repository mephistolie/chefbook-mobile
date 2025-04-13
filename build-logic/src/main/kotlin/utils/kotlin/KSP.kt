package utils.kotlin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import utils.gradle.libs

internal fun Project.configureKsp() {
  apply(plugin = libs.plugins.ksp.get().pluginId)

  kotlinExtension.sourceSets.forEach { sourceSet ->
    sourceSet.kotlin.srcDir("build/generated/ksp/${sourceSet.name}/kotlin")
  }
}
