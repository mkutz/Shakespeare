plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  api(project(":modules:core"))

  api(libs.retrofit2)
  api(libs.okHttp3)
  api(libs.retrofit2ConverterJackson)
  implementation(platform(libs.jacksonBom))
  api(libs.retrofit2ConverterScalars)

  testImplementation(libs.mockwebserver)
  testImplementation(libs.mockOauth2Server)

  implementation(libs.jsr305)

  testImplementation(libs.slf4jApi)
  testImplementation(libs.logbackClassic)

  testImplementation(platform(libs.junitBom))
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testRuntimeOnly(libs.junitJupiterEngine)

  testImplementation(libs.assertjCore)
}
