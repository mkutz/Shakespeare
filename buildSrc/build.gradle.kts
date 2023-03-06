plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
  implementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.9.11")
  implementation("com.diffplug.spotless:spotless-plugin-gradle:6.16.0")
}
