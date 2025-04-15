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
  api(project(":modules:core"))
}

testing {
  suites {
    val test by
      getting(JvmTestSuite::class) {
        useJUnitJupiter()
        dependencies {
          implementation(platform(libs.junitBom))
          implementation(libs.assertjCore)
          implementation(libs.junitJupiterApi)

          runtimeOnly(libs.junitPlatformLauncher)
          runtimeOnly(libs.junitJupiterEngine)
        }
      }
  }
}

tasks.jacocoTestReport { reports { xml.required = true } }
