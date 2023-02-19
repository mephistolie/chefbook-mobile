package com.mysty.chefbook.plugins

import com.android.build.gradle.BaseExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

internal fun Project.plugins(action: PluginContainer.() -> Unit) =
  plugins.run(action)

internal fun Project.java(action: JavaPluginExtension.() -> Unit) =
  (extensions["java"] as? JavaPluginExtension)?.run(action)

internal fun Project.android(action: BaseExtension.() -> Unit) =
  (extensions["android"] as? BaseExtension)?.run(action)

internal fun Project.kapt(action: KaptExtension.() -> Unit) =
  (extensions["kapt"] as? KaptExtension)?.run(action)

internal fun Project.ksp(action: KspExtension.() -> Unit) =
  (extensions["ksp"] as? KspExtension)?.run(action)
