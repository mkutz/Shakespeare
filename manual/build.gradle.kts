plugins {
  id("org.asciidoctor.jvm.convert") version "3.3.2"
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  testImplementation(project(":modules:core"))
  testImplementation(project(":modules:selenium"))
  testImplementation(project(":modules:retrofit"))

  implementation("com.google.code.findbugs:jsr305:3.0.2")

  testImplementation("org.slf4j:slf4j-api:2.0.6")
  testImplementation("ch.qos.logback:logback-classic:1.4.5")

  testImplementation(platform("org.junit:junit-bom:5.9.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testImplementation("org.assertj:assertj-core:3.24.2")

  testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

tasks.withType<Test> { useJUnitPlatform() }

asciidoctorj { modules { diagram.use() } }
