package com.mysty.chefbook.plugins.android

import com.mysty.chefbook.plugins.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

open class KspModule : Plugin<Project> {

  override fun apply(project: Project) = project.configureKspModule()

  private fun Project.configureKspModule() {
    plugins {
      apply("com.google.devtools.ksp")
    }

    kotlinExtension.sourceSets.forEach { sourceSet ->
      when (sourceSet.name) {
        "debug" -> sourceSet.kotlin.srcDir("build/generated/ksp/debug/kotlin")
        "release" -> sourceSet.kotlin.srcDir("build/generated/ksp/release/kotlin")
      }
    }
  }

}
