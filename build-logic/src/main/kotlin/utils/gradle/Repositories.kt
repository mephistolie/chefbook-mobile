package utils.gradle

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.jitPack() =
  maven(url = "https://jitpack.io")
