@file:Suppress("UnstableApiUsage", "unused")

plugins {
  java
  `java-test-fixtures`
  `jvm-test-suite`
  alias(libs.plugins.kotlinJvm)
  alias(libs.plugins.asciidoctor)
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

repositories { mavenCentral() }

val asciidoctorExt: Configuration by configurations.creating

dependencies {
  asciidoctorExt(libs.asciidoctorBlockSwitch)

  implementation(libs.jsr305)
}

testing {
  suites {
    val test by
      getting(JvmTestSuite::class) {
        useJUnitJupiter()
        dependencies {
          implementation(platform(libs.junitBom))
          implementation(project(":modules:core"))
          implementation(project(":modules:retrofit"))
          implementation(project(":modules:selenium"))
          implementation(libs.assertjCore)
          implementation(libs.jacksonModuleKotlin)
          implementation(libs.junitJupiterApi)
          implementation(libs.junitJupiterParams)
          implementation(libs.logbackClassic)
          implementation(libs.mockwebserver)
          implementation(libs.slf4jApi)
          implementation(platform(libs.jacksonBom))

          runtimeOnly(libs.junitPlatformLauncher)
          runtimeOnly(libs.junitJupiterEngine)
        }
      }
  }
}

tasks.withType<org.asciidoctor.gradle.jvm.AsciidoctorTask> {
  baseDirFollowsSourceFile()
  configurations("asciidoctorExt")
}

asciidoctorj { modules { diagram.use() } }
