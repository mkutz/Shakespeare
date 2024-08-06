plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  api(project(":modules:core"))

  api(libs.seleniumJava)

  api(libs.webdrivermanager)
  implementation(libs.commonsIo)
  implementation(platform(libs.jacksonBom))
  implementation(libs.bcprovJdk15on)

  testImplementation(libs.slf4jApi)
  testImplementation(libs.logbackClassic)

  testImplementation(project(":modules:testutils"))

  testImplementation(platform(libs.junitBom))
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testRuntimeOnly(libs.junitJupiterEngine)

  testImplementation(libs.assertjCore)
}
