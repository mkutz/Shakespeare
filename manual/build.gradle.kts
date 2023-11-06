plugins {
  id("org.asciidoctor.jvm.convert") version "3.3.2"
  `shakespeare-style`
  java
}

dependencies {
  testImplementation(project(":modules:core"))
  testImplementation(project(":modules:selenium"))
  testImplementation(project(":modules:retrofit"))

  implementation("com.google.code.findbugs:jsr305:3.0.2")

  testImplementation("org.slf4j:slf4j-api:2.0.9")
  testImplementation("ch.qos.logback:logback-classic:1.4.11")

  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testImplementation("org.assertj:assertj-core:3.24.2")

  testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

tasks.withType<Test> { useJUnitPlatform() }

tasks.withType<org.asciidoctor.gradle.jvm.AsciidoctorTask> { baseDirFollowsSourceFile() }

asciidoctorj { modules { diagram.use() } }
