plugins {
  alias(libs.plugins.asciidoctor)
  `shakespeare-style`
  java
  kotlin("jvm") version "2.0.20"
}

val asciidoctorExt by configurations.creating

dependencies {
  asciidoctorExt("io.spring.asciidoctor:spring-asciidoctor-extensions-block-switch:0.6.3")

  implementation(libs.jsr305)

  testImplementation(platform(libs.junitBom))
  testImplementation(project(":modules:core"))
  testImplementation(project(":modules:retrofit"))
  testImplementation(project(":modules:selenium"))
  testImplementation(libs.assertjCore)
  testImplementation(libs.jacksonModuleKotlin)
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testImplementation(libs.logbackClassic)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.slf4jApi)
  testImplementation(platform(libs.jacksonBom))

  testRuntimeOnly(libs.junitJupiterEngine)
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

tasks.withType<org.asciidoctor.gradle.jvm.AsciidoctorTask> {
  baseDirFollowsSourceFile()
  configurations("asciidoctorExt")
}

asciidoctorj { modules { diagram.use() } }

tasks.withType<Test> { useJUnitPlatform() }
