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

  implementation(libs.jsr305)
  implementation(libs.slf4jApi)
}

testing {
  suites {
    val test by
      getting(JvmTestSuite::class) {
        useJUnitJupiter()
        dependencies {
          implementation(project(":modules:testutils"))
          implementation(libs.assertjCore)
          implementation(libs.jqwik)
          implementation(platform(libs.junitBom))
          implementation(libs.junitJupiterApi)
          implementation(libs.junitJupiterParams)
          implementation(libs.logbackClassic)

          runtimeOnly(libs.junitPlatformLauncher)
          runtimeOnly(libs.junitJupiterEngine)
        }
      }
  }
}

tasks.jacocoTestReport { reports { xml.required = true } }
