plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  api(project(":modules:core"))
  api(libs.jspecify)
  api(libs.okHttp3)
  api(libs.retrofit2)
  api(libs.retrofit2ConverterJackson)
  api(libs.retrofit2ConverterScalars)

  implementation(libs.jsr305)
  implementation(platform(libs.jacksonBom))

  testImplementation(libs.assertjCore)
  testImplementation(platform(libs.junitBom))
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testImplementation(libs.logbackClassic)
  testImplementation(libs.mockOauth2Server)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.slf4jApi)

  testRuntimeOnly(libs.junitJupiterEngine)
}
