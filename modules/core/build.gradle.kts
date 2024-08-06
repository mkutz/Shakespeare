plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  implementation(libs.jsr305)
  implementation(libs.slf4jApi)

  testImplementation(project(":modules:testutils"))
  testImplementation(libs.assertjCore)
  testImplementation(libs.jqwik)
  testImplementation(platform(libs.junitBom))
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testImplementation(libs.logbackClassic)

  testRuntimeOnly(libs.junitJupiterEngine)
}
