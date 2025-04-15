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
  api(libs.jspecify)
  api(libs.seleniumJava)
  api(libs.webdrivermanager)
  api(project(":modules:core"))

  implementation(libs.bcprovJdk15on)
  implementation(libs.commonsIo)
  implementation(platform(libs.jacksonBom))
}

testing {
  suites {
    val test by
      getting(JvmTestSuite::class) {
        useJUnitJupiter()
        dependencies {
          implementation(platform(libs.junitBom))
          implementation(project(":modules:testutils"))
          implementation(libs.assertjCore)
          implementation(libs.junitJupiterApi)
          implementation(libs.junitJupiterParams)
          implementation(libs.logbackClassic)
          implementation(libs.slf4jApi)

          runtimeOnly(libs.junitPlatformLauncher)
          runtimeOnly(libs.junitJupiterEngine)
        }
      }
  }
}

tasks.jacocoTestReport { reports { xml.required = true } }
