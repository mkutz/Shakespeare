plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
  implementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.15.0")
  implementation("com.diffplug.spotless:spotless-plugin-gradle:6.23.0")
}
