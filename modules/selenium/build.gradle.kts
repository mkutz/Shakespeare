plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  api(libs.seleniumJava)
  api(libs.webdrivermanager)
  api(project(":modules:core"))

  implementation(libs.bcprovJdk15on)
  implementation(libs.commonsIo)
  implementation(platform(libs.jacksonBom))

  testImplementation(platform(libs.junitBom))
  testImplementation(project(":modules:testutils"))
  testImplementation(libs.assertjCore)
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testImplementation(libs.logbackClassic)
  testImplementation(libs.slf4jApi)

  testRuntimeOnly(libs.junitJupiterEngine)
}
