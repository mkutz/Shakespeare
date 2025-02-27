plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  api(libs.jspecify)
  api(project(":modules:core"))

  testImplementation(platform(libs.junitBom))
  testImplementation(libs.assertjCore)
  testImplementation(libs.junitJupiterApi)

  testRuntimeOnly(libs.junitJupiterEngine)
  testRuntimeOnly(libs.junitPlatformLauncher)
}
