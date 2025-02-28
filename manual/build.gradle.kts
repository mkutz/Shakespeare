plugins {
  alias(libs.plugins.asciidoctor)
  `shakespeare-style`
  java
  alias(libs.plugins.kotlinJvm)
}

val asciidoctorExt by configurations.creating

dependencies {
  asciidoctorExt(libs.asciidoctorBlockSwitch)

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
  testRuntimeOnly(libs.junitPlatformLauncher)
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

tasks.withType<org.asciidoctor.gradle.jvm.AsciidoctorTask> {
  baseDirFollowsSourceFile()
  configurations("asciidoctorExt")
}

asciidoctorj { modules { diagram.use() } }

tasks.withType<Test> { useJUnitPlatform() }
