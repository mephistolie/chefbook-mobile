package com.mysty.chefbook.plugins.java

import com.mysty.chefbook.plugins.java
import com.mysty.chefbook.plugins.plugins
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

open class CommonJavaModule : Plugin<Project> {

  override fun apply(project: Project) = project.configureAndroidModule()

  private fun Project.configureAndroidModule() {
    plugins {
      apply("java-library")
      apply("org.jetbrains.kotlin.jvm")
      apply("kotlin-kapt")
    }

    java {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11
    }
    addBaseDependencies()
  }

  private fun Project.addBaseDependencies() {
    dependencies {
      add("implementation", Dependencies.Coroutines.core)
    }
  }

}
