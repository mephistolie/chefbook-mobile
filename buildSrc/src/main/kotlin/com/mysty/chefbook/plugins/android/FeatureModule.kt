package com.mysty.chefbook.plugins.android

import com.mysty.chefbook.plugins.android
import com.mysty.chefbook.plugins.plugins
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


class FeatureModule : CommonAndroidModule() {

  override fun apply(project: Project) {
    project.run {
      super.apply(this)
      includeCommonModules()
    }
  }

}

fun Project.includeCommonModules() {
  dependencies {
    add("implementation", project(Modules.Common.core))
    add("implementation", project(Modules.Common.coreAndroid))
    add("implementation", project(Modules.Common.api))
    add("implementation", project(Modules.Common.navigation))
    add("implementation", project(Modules.Common.design))
    add("implementation", project(Modules.Common.ui))
  }
}

fun Project.setFeatureNamespace(featureName: String) {
  setNamespace("com.mysty.chefbook.features.$featureName")
}
