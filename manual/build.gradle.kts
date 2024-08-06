plugins {
  alias(libs.plugins.asciidoctor)
  `shakespeare-style`
  java
}

dependencies {
  implementation(libs.jsr305)

  testImplementation(platform(libs.junitBom))
  testImplementation(project(":modules:core"))
  testImplementation(project(":modules:retrofit"))
  testImplementation(project(":modules:selenium"))
  testImplementation(libs.assertjCore)
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testImplementation(libs.logbackClassic)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.slf4jApi)

  testRuntimeOnly(libs.junitJupiterEngine)
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

tasks.withType<Test> { useJUnitPlatform() }

tasks.withType<org.asciidoctor.gradle.jvm.AsciidoctorTask> { baseDirFollowsSourceFile() }

asciidoctorj { modules { diagram.use() } }
