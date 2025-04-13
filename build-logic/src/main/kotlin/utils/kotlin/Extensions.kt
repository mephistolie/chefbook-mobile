package utils.kotlin

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal fun Project.enableExplicitBackingFields() {
  kotlinExtension.sourceSets.all {
    languageSettings.enableLanguageFeature("ExplicitBackingFields")
  }
}
