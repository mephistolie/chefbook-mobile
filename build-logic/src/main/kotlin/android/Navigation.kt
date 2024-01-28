import gradle.kotlin.dsl.accessors._3d3b47942ab04f2cc9c29cffc95a2b60.ksp
import org.gradle.api.Project

fun Project.composeDestinationsModuleName(name: String) {
  ksp.arg("compose-destinations.moduleName", name)
}
