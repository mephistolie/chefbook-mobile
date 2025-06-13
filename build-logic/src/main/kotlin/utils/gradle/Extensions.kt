package utils.gradle

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.support.delegates.DependencyHandlerDelegate
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

val Project.libs
  get(): LibrariesForLibs = the<LibrariesForLibs>()

fun DependencyHandlerDelegate.implementation(provider: Provider<MinimalExternalModuleDependency>) =
  add("implementation", provider.get())

fun DependencyHandlerDelegate.androidTestImplementation(provider: Provider<MinimalExternalModuleDependency>) =
  add("androidTestImplementation", provider.get())
