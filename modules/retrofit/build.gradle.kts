@file:Suppress("UnstableApiUsage", "unused")

plugins {
  `java-library`
  jacoco
  `jvm-test-suite`
  `maven-publish`
}

java {
  withJavadocJar()
  withSourcesJar()
  toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
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
}

testing {
  suites {
    val test by
      getting(JvmTestSuite::class) {
        useJUnitJupiter()
        dependencies {
          implementation(libs.assertjCore)
          implementation(platform(libs.junitBom))
          implementation(libs.junitJupiterApi)
          implementation(libs.junitJupiterParams)
          implementation(libs.logbackClassic)
          implementation(libs.mockOauth2Server)
          implementation(libs.mockwebserver)
          implementation(libs.slf4jApi)

          runtimeOnly(libs.junitJupiterEngine)
          runtimeOnly(libs.junitPlatformLauncher)
        }
      }
  }
}

tasks.jacocoTestReport { reports { xml.required = true } }
