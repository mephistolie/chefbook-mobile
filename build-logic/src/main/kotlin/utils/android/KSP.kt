package utils.android

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

fun Project.composeDestinationsModuleName(name: String) =
  extensions.findByType(KspExtension::class)?.arg("compose-destinations.moduleName", name)
